package com.omfgdevelop.privatebookshelf.vaadinui;

import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.service.GenreService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class GenreDialog extends Dialog {

    private final GenreService genreService;

    private TextField genreNameTextFiled;

    private Button addBtn;


    public GenreDialog(GenreService genreService) {
        this.genreService = genreService;
        createViews();
        addViews();
    }

    private void createViews() {
        genreNameTextFiled = new TextField();
        genreNameTextFiled.setLabel("Name");
        addBtn = new Button("Add");
        addBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if (genreNameTextFiled.getValue().trim().isEmpty()) {
                genreNameTextFiled.setErrorMessage("Add genre name");
                genreNameTextFiled.setInvalid(true);
                return;
            }
            try {
                Genre genre = genreService.create(Genre.builder().name(genreNameTextFiled.getValue()).build());

                ComponentProvider.getSuccessNotification(String.format("Success! new genre %s added!", genre.getName()));

                genreNameTextFiled.clear();
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                ComponentProvider.getErrorNotification("This genre already exists");
                genreNameTextFiled.setInvalid(true);
            } catch (Exception e) {
                ComponentProvider.getErrorNotification("Unexpected error while adding genre");
            }
        });
    }

    private void addViews() {
        this.getHeader().add(addCloseBtn());
        this.setHeaderTitle("New genre");
        var verticalLayout = new VerticalLayout();
        verticalLayout.add(genreNameTextFiled);
        verticalLayout.add(addBtn);
        this.add(verticalLayout);
    }

    private Button addCloseBtn() {
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeButton;
    }

}
