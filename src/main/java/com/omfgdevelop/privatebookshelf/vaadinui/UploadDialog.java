package com.omfgdevelop.privatebookshelf.vaadinui;


import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.filtr.AuthorFilter;
import com.omfgdevelop.privatebookshelf.filtr.GenreFilter;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.springframework.data.domain.Page;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    MultiselectComboBox<Genre> genreComboBox;
    private final DataProvider<Author, String> authorProvider;
    private final DataProvider<Genre, String> genreProvider;

    private final Set<Genre> selectedGenres = new HashSet<>();


    public UploadDialog(AuthorService authorService, GenreService genreService, FileProcessingService fileProcessingService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.fileProcessingService = fileProcessingService;
        this.bookService = bookService;

        this.setHeaderTitle("Upload book");

        authorProvider = getAuthorProvider();
        genreProvider = getGenreProvider();

        okBtn = addOkBtn();
        bookName = createBookNameTextField();
        bookName.setLabel("Book name");
        authorComboBox = createAuthorCombobox();
        authorComboBox.setLabel("Author");
        genreComboBox = createGenreCombobox();
        genreComboBox.setLabel("Genre");
        selectedGenres.clear();
        prepareUploadComponent();
        addViews();
    }

    private void addViews() {
        this.getHeader().add(addCloseBtn());
        this.add(singleFileUpload);
        var verticalLayout = new VerticalLayout();
        verticalLayout.add(bookName);
        verticalLayout.add(authorComboBox);
        verticalLayout.add(genreComboBox);
        this.add(verticalLayout);
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
                .genres(selectedGenres)
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
        Button closeButton = new Button("Close", (e) -> this.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return closeButton;
    }


    private Button addOkBtn() {
        Button okBtn = new Button("OK");
        return okBtn;
    }

    private ComboBox<Author> createAuthorCombobox() {
        ComboBox<Author> authorsComboBox = new ComboBox<>();
        ConfigurableFilterDataProvider<Author, Void, String>
                wrapper = authorProvider.withConfigurableFilter();

        authorsComboBox.setDataProvider(authorProvider);
        authorsComboBox.setPlaceholder("Select author");
        authorsComboBox.addValueChangeListener(e -> wrapper.setFilter(e.getValue().getLastName() + " " + e.getValue().getFirstName()));
        authorsComboBox.setItemLabelGenerator(it -> it.getLastName() + " " + it.getFirstName());
        return authorsComboBox;
    }

    private MultiselectComboBox<Genre> createGenreCombobox() {
        MultiselectComboBox<Genre> genreComboBox = new MultiselectComboBox<>();
        ConfigurableFilterDataProvider<Genre, Void, String>
                wrapper = genreProvider.withConfigurableFilter();

        genreComboBox.setDataProvider(genreProvider);
        genreComboBox.setPlaceholder("Select genres");
        genreComboBox.setItemLabelGenerator(Genre::getName);
        genreComboBox.addValueChangeListener(e -> {
            wrapper.setFilter(e.getValue().stream().reduce((f, s) -> s).orElse(new Genre()).getName());
            selectedGenres.clear();
            selectedGenres.addAll(e.getValue().stream().toList());
        });
        return genreComboBox;
    }

    private DataProvider<Author, String> getAuthorProvider() {
        return DataProvider.fromFilteringCallbacks((CallbackDataProvider.FetchCallback<Author, String>) query -> {
            Page<Author> page = authorService.findPage(query);
            return page.stream();
        }, (CallbackDataProvider.CountCallback<Author, String>) query ->
                authorService.getCount(AuthorFilter.builder().text(query.getFilter().orElse(null)).build()));


    }

    private DataProvider<Genre, String> getGenreProvider() {
        return DataProvider.fromFilteringCallbacks((CallbackDataProvider.FetchCallback<Genre, String>) query -> {
            Page<Genre> page = genreService.findPage(query);
            return page.stream();
        }, (CallbackDataProvider.CountCallback<Genre, String>) query ->
                genreService.getCount(GenreFilter.builder().text(query.getFilter().orElse(null)).build()));
    }
}
