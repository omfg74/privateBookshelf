package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import com.omfgdevelop.privatebookshelf.exception.BusinessError;
import com.omfgdevelop.privatebookshelf.exception.BusinessException;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.BookRepository;
import com.omfgdevelop.privatebookshelf.utils.Domain;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.omfgdevelop.privatebookshelf.utils.PageableRepository;
import com.omfgdevelop.privatebookshelf.utils.SearchProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final EntityManager entityManager;

    private final UberMapper mapper;

    private final BookFileService bookFileServise;

    private final static int MAX_PAGE_SIZE = 10;

    public BookEntity getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Book create(Book book) throws BusinessException {
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
        if (book.getFiles().stream().map(BookFile::getId).filter(Objects::nonNull).toList().size() > 0) {
            throw new BusinessException(BusinessError.WRONG_FILE_PARAMS_SET);
        }
        if (book.getId() != null) {
            book.getFiles().addAll(bookFileServise.findAllByBookId(book.getId()));
        }

//        book.getFiles().forEach(bookFile -> {
//            if (bookFile.getId() != null) {
//                var entity = entityManager.getReference(BookFileEntity.class, bookFile.getId());
//                bookFile.setId(entity.getId());
//                bookFile.setName(entity.getName());
//                bookFile.setFileExtension(entity.getFileExtension());
//            }
//        });

        return mapper.map(bookRepository.saveAndFlush(mapper.map(book)));
    }


    private Specification<BookEntity> getWhereClose(BookFilter filter) {

        return Domain.<BookEntity>byString(BookEntity.Fields.name, filter.getText());
    }

    private List<Book> pageProcessor(Page<BookEntity> list) {
        return list.stream().distinct()
                .map(mapper::map)
                .toList();
    }

    public Page<Book> findPage(FilteredQueryWithPagingRequest<BookFilter> request) {
        request.setSortingFields(List.of(BookEntity.Fields.name));
        request.setSortDirection(Sort.Direction.ASC);
        return SearchProcessor.findPage(request,
                bookRepository,
                this::getWhereClose,
                this::pageProcessor,
                MAX_PAGE_SIZE);
    }

    public int count(BookFilter filter) {
        return (int) bookRepository.count(getWhereClose(filter));
    }

    public void deleteBooks(List<Book> bookList) {
        bookRepository.deleteAll(bookList.stream().map(mapper::map).toList());
    }
    public void delete(Book book) {
        bookRepository.delete(mapper.map(book));
    }
}
