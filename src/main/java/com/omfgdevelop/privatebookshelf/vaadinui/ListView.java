package com.omfgdevelop.privatebookshelf.vaadinui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Books")
@Route(value = "")
public class ListView extends VerticalLayout {

    Grid<String> grid = new Grid<>(String.class);
    TextField filter = new TextField();

    public ListView() {
        addClassName("list-view");
        setSizeFull();
        grid.setColumns();

        configureGrid();
        add(getToolbar(), grid);
    }

    private Component getToolbar() {
        filter.setPlaceholder("Find by..");
        filter.setClearButtonVisible(true);
        filter.setValueChangeMode(ValueChangeMode.LAZY);

        var button = new Button("Add");
        var toolbar = new HorizontalLayout(filter, button);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void configureGrid() {
        grid.addClassName("grid");
    }
}
