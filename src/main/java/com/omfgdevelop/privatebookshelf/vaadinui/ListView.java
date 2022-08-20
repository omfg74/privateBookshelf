package com.omfgdevelop.privatebookshelf.vaadinui;

import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import com.omfgdevelop.privatebookshelf.service.BookService;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.logging.Filter;


@PageTitle("Books")
@Route(value = "")
public class ListView extends VerticalLayout {

    Grid<Book> grid = new Grid<>(Book.class);
    TextField filter = new TextField();

    private final BookService bookService;


    public ListView(BookService bookService) {
        this.bookService = bookService;
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
        return toolbar;
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
