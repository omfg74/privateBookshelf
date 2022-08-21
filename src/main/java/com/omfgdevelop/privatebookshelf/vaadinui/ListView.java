package com.omfgdevelop.privatebookshelf.vaadinui;

import com.omfgdevelop.privatebookshelf.domain.*;
import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.service.AuthorService;
import com.omfgdevelop.privatebookshelf.service.BookService;
import com.omfgdevelop.privatebookshelf.service.FileProcessingService;
import com.omfgdevelop.privatebookshelf.service.GenreService;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@PageTitle("Books")
@Route(value = "")
public class ListView extends VerticalLayout {

    Grid<Book> grid = new Grid<>(Book.class);
    TextField filter = new TextField();

    private final FileProcessingService fileProcessingService;
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private Map<String, Author> authorMap = new ConcurrentHashMap<>();

    private Map<String, Genre> genreMap = new ConcurrentHashMap<>();


    public ListView(BookService bookService, FileProcessingService fileProcessingService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.fileProcessingService = fileProcessingService;
        this.authorService = authorService;
        this.genreService = genreService;
        addClassName("list-view");
        setSizeFull();
        grid.setColumns("name");
        grid.addColumn(book -> Arrays.toString(book.getAuthor().stream().map(it -> it.getFirstName() + " " + it.getLastName()).toArray()).replace("[", "").replace("]", "")).setHeader("Author");
        grid.addColumn(book -> Arrays.toString(book.getGenres().stream().map(Genre::getName).toArray()).replace("[", "").replace("]", "")).setHeader("Genres");

        updateList(null);
        configureGrid();
        add(getToolbar(), grid);

    }


    private Component getToolbar() {
        filter.setPlaceholder("Find by..");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(value -> updateList(value.getValue()));

        var button = new Button("Find");
        var toolbar = new HorizontalLayout(filter, button);
        button.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> updateList(filter.getValue()));
        toolbar.addClassName("toolbar");
        toolbar.add(createUploadButton());
        return toolbar;
    }

    private Button createUploadButton() {
        var uploadBtn = new Button("Upload");
        uploadBtn.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                MemoryBuffer memoryBuffer = new MemoryBuffer();
                Upload singleFileUpload = new Upload(memoryBuffer);
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Upload book");

                TextField bookName = new TextField();
                bookName.setPlaceholder("New book name");
                ComboBox<Author> authorsComboBox = new ComboBox<>();
                authorsComboBox.setItems(authorService.findAll());
                authorsComboBox.setItemLabelGenerator(it -> it.getLastName() + " " + it.getFirstName());

                ComboBox<Genre> genreComboBox = new ComboBox<>();
                genreComboBox.setItems(genreService.findAll());
                genreComboBox.setItemLabelGenerator(Genre::getName);

                Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                Button okBtn = new Button("OK");
                dialog.getHeader().add(closeButton);
                dialog.add(singleFileUpload);
                dialog.add(bookName);
                dialog.add(authorsComboBox);
                dialog.add(genreComboBox);
                dialog.getFooter().add(okBtn);

                singleFileUpload.addSucceededListener(uploadEvent -> {
                    // Get information about the uploaded file
                    final InputStream fileData = memoryBuffer.getInputStream();
                    final String fileName = uploadEvent.getFileName();
                    final long contentLength = uploadEvent.getContentLength();
                    final String mimeType = uploadEvent.getMIMEType();


                    okBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) okEvent -> {
                        BookFile bookFile;
                        try {
                            bookFile = fileProcessingService.createBookFile(fileData, fileName, mimeType, (int) contentLength);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                fileData.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        var book = Book.builder()
                                .name(bookName.getValue())
                                .author(Collections.singleton(authorsComboBox.getValue()))
                                .genres(Collections.singleton(genreComboBox.getValue()))
                                .files(Collections.singletonList(bookFile))
                                .build();
                        try {
                            Book created = bookService.create(book);
                            dialog.close();
                        } catch (BusinessException e) {
                            throw new RuntimeException(e);
                        }

                    });


                    System.out.println(mimeType);
                    // Do something with the file data
                    // processFile(fileData, fileName, contentLength, mimeType);
                });

                dialog.open();
            }
        });
        return uploadBtn;
    }

    private void updateAuthorComboBox(ComboBox<String> comboBox) {
        authorMap.clear();
        authorMap.putAll(authorService.findAll().stream().collect(Collectors.toMap(it -> it.getLastName() + " " + it.getFirstName(), it -> it)));

        comboBox.setItems(authorMap.keySet());


    }

    private void updateGenreComboBox(ComboBox<String> comboBox) {
        authorMap.clear();
        genreMap.putAll(genreService.findAll().stream().collect(Collectors.toMap(Genre::getName, it -> it)));

        comboBox.setItems(authorMap.keySet());

    }


    private void updateList(String value) {
        var filter = BookFilter.builder()
                .text(value).build();
        FilteredQueryWithPagingRequest<BookFilter> request = new FilteredQueryWithPagingRequest<>();
        request.setFilter(filter);
        request.setPageNumber(0);
        request.setPageSize(100);

        Page<Book> page = bookService.getBookPage(request);
        grid.setItems(page.stream().toList());
    }


    private void configureGrid() {
        grid.addClassName("grid");
    }
}
