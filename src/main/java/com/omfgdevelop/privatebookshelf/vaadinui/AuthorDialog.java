package com.omfgdevelop.privatebookshelf.vaadinui;


import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.service.AuthorService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.hibernate.exception.ConstraintViolationException;


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
        comment.setVisible(false);
        authorNameTextField = new TextField();
        authorNameTextField.setLabel("Author first name");

        authorNameTextField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            authorNameTextField.setInvalid(false);
            authorNameTextField.setErrorMessage(null);
            comment.setVisible(false);
        });
        authorNameTextField.addFocusListener((ComponentEventListener<FocusNotifier.FocusEvent<TextField>>) event -> {
            authorNameTextField.setInvalid(false);
            authorNameTextField.setErrorMessage(null);
            comment.setVisible(false);
        });

        authorLastNameTextField = new TextField();
        authorLastNameTextField.setLabel("Author last name");

        authorLastNameTextField.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) event -> {
            authorLastNameTextField.setInvalid(false);
            authorLastNameTextField.setErrorMessage(null);
            comment.setVisible(false);
        });

        authorLastNameTextField.addFocusListener((ComponentEventListener<FocusNotifier.FocusEvent<TextField>>) event -> {
            authorLastNameTextField.setInvalid(false);
            authorLastNameTextField.setErrorMessage(null);
            comment.setVisible(false);
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
                comment.setVisible(true);
            }catch (org.springframework.dao.DataIntegrityViolationException e){
                comment.setText("This author already exists");
                comment.setVisible(true);
            }catch (Exception e) {
                comment.setText(e.getMessage());
                comment.setVisible(true);
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
