package com.omfgdevelop.privatebookshelf.vaadinui;


import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.exception.ValidationError;
import com.omfgdevelop.privatebookshelf.filtr.AuthorFilter;
import com.omfgdevelop.privatebookshelf.filtr.GenreFilter;
import com.omfgdevelop.privatebookshelf.service.AuthorService;
import com.omfgdevelop.privatebookshelf.service.BookService;
import com.omfgdevelop.privatebookshelf.service.FileProcessingService;
import com.omfgdevelop.privatebookshelf.service.GenreService;
import com.omfgdevelop.privatebookshelf.utils.Validator;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.FocusNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import org.springframework.data.domain.Page;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.omfgdevelop.privatebookshelf.exception.ValidationError.*;
import static com.omfgdevelop.privatebookshelf.utils.Validator.*;

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
    private final Set<Genre> selectedGenres = new HashSet<>();


    public UploadDialog(AuthorService authorService, GenreService genreService, FileProcessingService fileProcessingService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.fileProcessingService = fileProcessingService;
        this.bookService = bookService;

        this.setHeaderTitle("Upload book");

        authorProvider = getAuthorProvider();

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
            });

        });

    }


    private void onOkPressed(InputStream fileData, String fileName, String mimeType, int contentLength) {
        BookFile bookFile;
        try {
            bookFile = fileProcessingService.createBookFile(fileData, fileName, mimeType, contentLength);
        } catch (IOException e) {
            ComponentProvider.getErrorNotification("Unexpected Error");
            throw new RuntimeException(e);
        } finally {
            try {
                fileData.close();
            } catch (IOException e) {
                ComponentProvider.getErrorNotification("Unexpected Error");
                throw new RuntimeException(e);
            }
        }
        final Set<ValidationError> errors = new HashSet<>();

        errors.add(validateNotNull(authorComboBox.getValue(), AUTHOR_IS_NOT_SET));
        errors.add(validateString(bookName.getValue(), BOOK_NAME_IS_NOT_SET));
        errors.add(validateNotEmpty(genreComboBox.getValue(), GENRE_IS_NOT_SET));


        if (bookFile == null) {
            ComponentProvider.getErrorNotification("File not uploaded");
        }

        if (errors.contains(BOOK_NAME_IS_NOT_SET)) {
            bookName.setInvalid(true);
            bookName.setErrorMessage("Enter book name");
        }

        if (errors.contains(AUTHOR_IS_NOT_SET)) {
            authorComboBox.setInvalid(true);
            authorComboBox.setErrorMessage("Select author from the list");
        }
        if (errors.contains(GENRE_IS_NOT_SET)) {
            genreComboBox.setInvalid(true);
            genreComboBox.setErrorMessage("Select genre from the list");
        }


        try {
            if (errors.stream().filter(Objects::nonNull).collect(Collectors.toSet()).isEmpty()) {
                var book = Book.builder()
                        .name(bookName.getValue())
                        .author(Collections.singleton(authorComboBox.getValue()))
                        .genres(selectedGenres)
                        .files(Collections.singletonList(bookFile))
                        .build();
                bookService.create(book);
                ComponentProvider.getSuccessNotification(String.format("Book added %s!", book.getName()));

                close();
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            ComponentProvider.getErrorNotification("Book already exists");
            throw new RuntimeException(e);
        } catch (BusinessException e) {
            ComponentProvider.getErrorNotification(e.getMessage());
            throw new RuntimeException(e);
        } catch (Exception e) {
            ComponentProvider.getErrorNotification("Unexpected Error");
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
        return new Button("OK");
    }

    private ComboBox<Author> createAuthorCombobox() {
        ComboBox<Author> authorsComboBox = new ComboBox<>();
        ConfigurableFilterDataProvider<Author, Void, String>
                wrapper = authorProvider.withConfigurableFilter();

        authorsComboBox.setErrorMessage(null);
        authorsComboBox.setInvalid(false);
        authorsComboBox.addFocusListener((ComponentEventListener<FocusNotifier.FocusEvent<ComboBox<Author>>>) event -> {
            authorsComboBox.setErrorMessage(null);
            authorsComboBox.setInvalid(false);
        });
        authorsComboBox.setDataProvider(authorProvider);
        authorsComboBox.setPlaceholder("Select author");
        authorsComboBox.addValueChangeListener(e -> wrapper.setFilter(e.getValue() != null ? e.getValue().getLastName() + " " + e.getValue().getFirstName() : null));
        authorsComboBox.setItemLabelGenerator(it -> it.getLastName() + " " + it.getFirstName());
        return authorsComboBox;
    }

    private MultiselectComboBox<Genre> createGenreCombobox() {
        MultiselectComboBox<Genre> genreComboBox = new MultiselectComboBox<>();
//        ConfigurableFilterDataProvider<Genre, Void, String>
//                wrapper = genreProvider.withConfigurableFilter();

        genreComboBox.setItems(genreService.findAll());
        genreComboBox.setPlaceholder("Select genres");
        genreComboBox.setItemLabelGenerator(Genre::getName);

        genreComboBox.setErrorMessage(null);
        genreComboBox.setInvalid(false);
        genreComboBox.addSelectionListener((MultiSelectionListener<MultiselectComboBox<Genre>, Genre>) event -> {
            genreComboBox.setErrorMessage(null);
            genreComboBox.setInvalid(false);
        });

        genreComboBox.addValueChangeListener(e -> {
//            wrapper.setFilter(e.getValue().stream().reduce((f, s) -> s).orElse(new Genre()).getName());
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
