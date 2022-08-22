package com.omfgdevelop.privatebookshelf.vaadinui;


import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.service.AuthorService;
import com.omfgdevelop.privatebookshelf.service.BookService;
import com.omfgdevelop.privatebookshelf.service.FileProcessingService;
import com.omfgdevelop.privatebookshelf.service.GenreService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class UploadDialog extends Dialog {
    private final AuthorService authorService;
    private final GenreService genreService;

    private final FileProcessingService fileProcessingService;

    private final BookService bookService;

    private Upload singleFileUpload;

    MemoryBuffer memoryBuffer = new MemoryBuffer();

    Button okBtn;

    TextField bookName;

    ComboBox<Author> authorComboBox;
    ComboBox<Genre> genreComboBox;


    public UploadDialog(AuthorService authorService, GenreService genreService, FileProcessingService fileProcessingService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.fileProcessingService = fileProcessingService;
        this.bookService = bookService;

        this.setHeaderTitle("Upload book");

        okBtn = addOkBtn();
        bookName = createBookNameTextField();
        authorComboBox = createAuthorCombobox();
        genreComboBox = createGenreCombobox();

        prepareUploadComponent();
        addViews();
    }

    private void addViews() {
        this.getHeader().add(addCloseBtn());
        this.add(singleFileUpload);
        this.add(bookName);
        this.add(authorComboBox);
        this.add(genreComboBox);
        this.getFooter().add(okBtn);
    }


    private void prepareUploadComponent() {

        singleFileUpload = new Upload(memoryBuffer);
        singleFileUpload.setAcceptedFileTypes("application/pdf", ".pdf", "application/epub+zip", ".epub", "application/fb2", ".fb2");

        singleFileUpload.addSucceededListener((ComponentEventListener<SucceededEvent>) uploadEvent -> {
            final InputStream fileData = memoryBuffer.getInputStream();
            final String fileName = uploadEvent.getFileName();
            final long contentLength = uploadEvent.getContentLength();
            final String mimeType = uploadEvent.getMIMEType();

            okBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) okEvent -> {
                onOkPressed(fileData, fileName, mimeType, (int) contentLength);
                close();
            });

        });

    }


    private void onOkPressed(InputStream fileData, String fileName, String mimeType, int contentLength) {
        BookFile bookFile;
        try {
            bookFile = fileProcessingService.createBookFile(fileData, fileName, mimeType, contentLength);
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
                .author(Collections.singleton(authorComboBox.getValue()))
                .genres(Collections.singleton(genreComboBox.getValue()))
                .files(Collections.singletonList(bookFile))
                .build();
        try {
            bookService.create(book);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
    }

    private TextField createBookNameTextField() {
        TextField bookName = new TextField();
        bookName.setPlaceholder("New book name");
        return bookName;
    }

    private Button addCloseBtn() {
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeButton;
    }


    private Button addOkBtn() {
        Button okBtn = new Button("OK");
        return okBtn;
    }

    private ComboBox<Author> createAuthorCombobox() {
        ComboBox<Author> authorsComboBox = new ComboBox<>();
        authorsComboBox.setItems(authorService.findAll());
        authorsComboBox.setPlaceholder("Select author");
        authorsComboBox.setItemLabelGenerator(it -> it.getLastName() + " " + it.getFirstName());
        return authorsComboBox;
    }

    private ComboBox<Genre> createGenreCombobox() {
        ComboBox<Genre> genreComboBox = new ComboBox<>();
        genreComboBox.setItems(genreService.findAll());
        genreComboBox.setPlaceholder("Select genres");
        genreComboBox.setItemLabelGenerator(Genre::getName);
        return genreComboBox;
    }
}
