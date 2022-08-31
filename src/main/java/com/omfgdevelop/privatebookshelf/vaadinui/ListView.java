package com.omfgdevelop.privatebookshelf.vaadinui;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.service.*;
import com.omfgdevelop.privatebookshelf.utils.DataProviderFactory;
import com.omfgdevelop.privatebookshelf.utils.FilterService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.function.Function;


@PageTitle("Books")
@Route(value = "")
public class ListView extends VerticalLayout {

    private final Grid<Book> grid = new Grid<>(Book.class);
    private final TextField filter = new TextField();

    private final FileProcessingService fileProcessingService;
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private static final int PAGE_SIZE = 50;

    private final ConfigurableFilterDataProvider<Book, Void, String> provider;

    private final FilterService filterService;

    private final VersionService versionService;

    public ListView(BookService bookService, FileProcessingService fileProcessingService, AuthorService authorService, GenreService genreService, DataProviderFactory dataProviderFactory, FilterService filterService, VersionService versionService) throws BusinessException {
        this.bookService = bookService;
        this.fileProcessingService = fileProcessingService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.filterService = filterService;
        this.versionService = versionService;
        addClassName("book-create-dialog");
        setSizeFull();


        provider = getBookProvider();

        configureGrid();
        add(getToolbar(), grid);
        var versionLayout = new HorizontalLayout();
        versionLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        versionLayout.add(versionService.getVersion());
        versionLayout.setWidthFull();
        versionLayout.setSpacing(true);
        add(versionLayout);


    }

//    private Span getVersion() {
////        Text version = new Text("");
//        Span version = new Span("");
//        version.getElement().getThemeList().add("badge");
//        try {
//            version.setText( Utils.getVersion());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return version;
//    }


    private Button createAuthorDialogButton() {
        return new Button("Add new author", e -> {
            AuthorDialog authorDialog = new AuthorDialog(authorService);
            authorDialog.open();
        });

    }

    private Button createGenreDialogButton() {
        return new Button("Add new genre", e -> {
            GenreDialog genreDialog = new GenreDialog(genreService);
            genreDialog.open();
        });

    }

    private Component getToolbar() {
        filter.setPlaceholder("Find by..");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> provider.setFilter(e.getValue()));

        var button = new Button("Find");
        var toolbar = new HorizontalLayout(filter, button);
        button.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> provider.setFilter(filter.getValue()));
        toolbar.addClassName("toolbar");
        toolbar.add(createUploadButton());
        toolbar.add(createAuthorDialogButton());
        toolbar.add(createGenreDialogButton());

        return toolbar;
    }

    private Button createUploadButton() {
        var uploadBtn = new Button("Upload");
        uploadBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            UploadDialog uploadDialog = new UploadDialog(authorService, genreService, fileProcessingService, bookService);
            uploadDialog.open();
        });
        return uploadBtn;
    }

    private ConfigurableFilterDataProvider<Book, Void, String> getBookProvider() {
        return DataProvider.fromFilteringCallbacks((CallbackDataProvider.FetchCallback<Book, String>) query -> {
                    Page<Book> page = bookService.findPage(filterService.getFilter(query, BookFilter.builder().text(query.getFilter().orElse(null)).build()));
                    return page.stream();
                }, (CallbackDataProvider.CountCallback<Book, String>) query -> bookService
                        .count(BookFilter.builder()
                                .text(query.getFilter().orElse(null))
                                .build()))
                .withConfigurableFilter();
    }

    private void configureGrid() {
        grid.addClassName("grid");
        grid.setPageSize(PAGE_SIZE);
        grid.setDataProvider(provider);
        grid.setColumns("name");
//        grid.sort(List.of(new GridSortOrder<Book>(grid.getColumnByKey("name"), SortDirection.ASCENDING)));
        grid.getColumns().forEach(it -> it.setSortable(false));
        grid.addColumn(book -> Arrays.toString(book.getAuthor().stream().map(it -> it.getFirstName() + " " + it.getLastName()).toArray()).replace("[", "").replace("]", "")).setHeader("Author");
        grid.addColumn(book -> Arrays.toString(book.getGenres().stream().map(Genre::getName).toArray()).replace("[", "").replace("]", "")).setHeader("Genres");
        grid.addColumn(new ComponentRenderer<>(ButtonAggregator::new, (SerializableBiConsumer<ButtonAggregator, Book>) (buttonAggregator, book) -> book.getFiles().forEach(it ->
                buttonAggregator.addButton(it.getFileExtension(), "download",
                        () -> new StreamResource(book.getName() + "." + it.getFileExtension(),
                                () -> fileProcessingService.getStream(it)).setContentType(it.getFileExtension()))))).setHeader("Download");
        grid.addColumn(new ComponentRenderer((SerializableSupplier<Component>) () -> {
            var icon = new Icon("lumo", "cross");
            return new Button(icon);
        }, (SerializableBiConsumer<Button, Book>) (button, book) -> button.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            ComponentProvider.getConfirmDialog("Are you sure you want to delete book", confirmEvent -> {
                bookService.delete(book);
                provider.refreshAll();
                return null;
            }, (Function<ConfirmDialog.CancelEvent, Void>) cancelEvent -> null).open();
        }))).setHeader("Delete");
    }


}
