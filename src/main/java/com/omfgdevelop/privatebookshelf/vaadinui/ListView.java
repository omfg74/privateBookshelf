package com.omfgdevelop.privatebookshelf.vaadinui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.awt.*;

@PageTitle("Books")
@Route(value = "")
public class ListView extends VerticalLayout {

    Grid<String> grid = new Grid<>(String.class);
    TextField filter = new TextField();

    public ListView() {
        addClassName("list-view");
        setSizeFull();

    }


    private void configureGrid(){
        grid.addClassName("grid");
    }
}
