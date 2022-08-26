package com.omfgdevelop.privatebookshelf.vaadinui;


import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.service.AuthorService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;


/**
 * Author add dialog
 */
public class AuthorDialog extends Dialog {

    private TextField authorNameTextField;
    private TextField authorLastNameTextField;

    private Button okBtn;
    private final AuthorService authorService;

    private final H4 comment = new H4();

    public AuthorDialog(AuthorService authorService) {
        this.authorService = authorService;
        this.setHeaderTitle("Add new Author");
        addClassName("author-create-dialog");
        createViews();
        addViews();
    }

    private void createViews() {
        authorNameTextField = new TextField();
        authorNameTextField.setLabel("Author first name");
        authorNameTextField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            authorNameTextField.setInvalid(false);
            authorNameTextField.setErrorMessage(null);
        });
        authorLastNameTextField = new TextField();
        authorLastNameTextField.setLabel("Author last name");

        authorLastNameTextField.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
                authorLastNameTextField.setInvalid(false);
                authorLastNameTextField.setErrorMessage(null);
            }
        });
        okBtn = new Button("Add");
        okBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if (authorNameTextField.getValue().trim().isEmpty()) {
                authorNameTextField.setErrorMessage("Enter author first name");
                authorNameTextField.setInvalid(true);

                return;
            }
            if (authorLastNameTextField.getValue().trim().isEmpty()) {
                authorLastNameTextField.setErrorMessage("Enter author last name");
                authorLastNameTextField.setInvalid(true);
                return;
            }
            try {
                Author author = authorService.create(Author.builder().firstName(authorNameTextField.getValue()).lastName(authorLastNameTextField.getValue()).build());
                comment.setText("Success! Added " + author.getFirstName() + " " + author.getLastName());
                authorNameTextField.clear();
                authorLastNameTextField.clear();
            } catch (Exception e) {
                comment.setText(e.getMessage());
            }
        });

    }


    private void addViews() {
        this.getHeader().add(addCloseBtn());
        var verticalLayout = new VerticalLayout();
        verticalLayout.add(comment);
        verticalLayout.add(authorNameTextField);
        verticalLayout.add(authorLastNameTextField);
        verticalLayout.add(okBtn);
        this.add(verticalLayout);

    }

    private Button addCloseBtn() {
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeButton;
    }

}
