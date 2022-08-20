package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import com.omfgdevelop.privatebookshelf.entity.BookFile;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import com.omfgdevelop.privatebookshelf.exception.BusinessError;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.mapper.Mapper;
import com.omfgdevelop.privatebookshelf.repository.BookRepository;
import com.omfgdevelop.privatebookshelf.utils.Domain;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.omfgdevelop.privatebookshelf.utils.SearchProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final EntityManager entityManager;

    private final Mapper mapper;

    private final static int MAX_PAGE_SIZE = 20;

    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public BookEntity create(BookEntity book) throws BusinessException {
        if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            book.getAuthor().forEach(authorEntity -> {
                if (authorEntity.getId() != null) {
                    var entity = entityManager.getReference(AuthorEntity.class, authorEntity.getId());
                    authorEntity.setId(entity.getId());
                    authorEntity.setFirstName(entity.getFirstName());
                    authorEntity.setFirstName(entity.getLastName());
                }
            });
        } else throw new BusinessException(BusinessError.NO_AUTHORS_SET_TO_BOOK);

        if (book.getGenres() != null) {
            book.getGenres().forEach(genreEntity -> {
                if (genreEntity.getId() != null) {
                    var entity = entityManager.getReference(GenreEntity.class, genreEntity.getId());
                    genreEntity.setId(entity.getId());
                    genreEntity.setName(entity.getName());
                }
            });
        } else throw new BusinessException(BusinessError.NO_GENRES_SET_TO_BOOK);

        if (book.getFiles() == null) {
            throw new BusinessException(BusinessError.NO_FILES_SET_TO_BOOK);
        }
        if (book.getFiles().stream().map(BookFile::getId).filter(Objects::isNull).toList().size() > 0) {
            throw new BusinessException(BusinessError.WRONG_FILE_PAREMS_SET);
        }

        book.getFiles().forEach(bookFile -> {
            var entity = entityManager.getReference(BookFile.class, bookFile.getId());
            bookFile.setId(entity.getId());
            bookFile.setName(entity.getName());
            bookFile.setFileExtension(entity.getFileExtension());
        });

        return bookRepository.save(book);
    }


    private Specification<BookEntity> getWhereClose(BookFilter filter) {

        return Domain.byString(BookEntity.Fields.name, filter.getText());
    }

    private List<Book> pageProcessor(Page<BookEntity> list) {
        return list.stream()
                .map(mapper::map)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    public Page<Book> getBookPage(FilteredQueryWithPagingRequest<BookFilter> request) {

        Page<Book> page= SearchProcessor.findPage(request,
                bookRepository,
                this::getWhereClose,
                this::pageProcessor,
                MAX_PAGE_SIZE);
        return page;
    }
}
