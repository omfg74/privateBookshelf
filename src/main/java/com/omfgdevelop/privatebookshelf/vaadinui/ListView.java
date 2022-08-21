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
                UploadDialog uploadDialog = new UploadDialog(authorService, genreService, fileProcessingService, bookService);
                uploadDialog.open();
            }
        });
        return uploadBtn;
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
