package com.omfgdevelop.privatebookshelf.service

import com.omfgdevelop.privatebookshelf.RepositorySpecBase
import com.omfgdevelop.privatebookshelf.domain.BookFilter
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity
import com.omfgdevelop.privatebookshelf.entity.BookEntity
import com.omfgdevelop.privatebookshelf.entity.BookFile
import com.omfgdevelop.privatebookshelf.entity.GenreEntity
import com.omfgdevelop.privatebookshelf.exception.BusinessException
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page

import javax.transaction.Transactional

class BookEntityServiceTest extends RepositorySpecBase {

    @Autowired
    BookService bookService;

    @Transactional
    def 'can find book by id'() {
        given:
        def id = 100
        when:
        BookEntity book = bookService.getBookById(id)

        then:
        book.id == 100
        book.name == "Lru"
        book.author.stream().toList().get(0).id == 10L
    }

    @Transactional
    def 'can create book'() {
        given:

        var book = BookEntity
                .builder()
                .name("Tom Soyer")
                .outlet("Питер")
                .files(new ArrayList<>(Collections.singletonList(
                        BookFile.builder().id(10L)
                                .build())))
                .genres(new HashSet<>(Collections.singletonList(GenreEntity.builder().name("Fiction").build())))
                .author(new HashSet<>(Collections.singletonList(AuthorEntity
                        .builder()
                        .firstName("Mark")
                        .lastName("Twain")
                        .build())))
                .build()

        when:
        BookEntity created = bookService.create(book)

        then:
        created.id != null
        created.name == book.name
        created.files.get(0).name == book.files.get(0).name
        created.outlet == book.outlet
        created.author.stream().toList().get(0).getFirstName() == book.author.stream().toList().get(0).getFirstName()
        created.author.stream().toList().get(0).getLastName() == book.author.stream().toList().get(0).getLastName()
    }

    @Transactional
    def 'cant create book no file id error'() {
        given:

        var book = BookEntity
                .builder()
                .name("Tom Soyer")
                .outlet("Питер")
                .genres(new HashSet<>(Collections.singletonList(GenreEntity.builder().name("Fiction").build())))
                .author(new HashSet<>(Collections.singletonList(AuthorEntity
                        .builder()
                        .firstName("Mark")
                        .lastName("Twain")
                        .build())))
                .build()

        when:
        BookEntity created = bookService.create(book)

        then:
        thrown BusinessException
    }

    @Transactional
    def 'cant create book No author'() {
        given:

        var book = BookEntity
                .builder()
                .name("Tom Soyer")
                .outlet("Питер")
                .files(new ArrayList<>(Collections.singletonList(
                        BookFile.builder().id(10L)
                                .build())))
                .genres(new HashSet<>(Collections.singletonList(GenreEntity.builder().name("Fiction").build())))
                .build()

        when:
        BookEntity created = bookService.create(book)

        then:
        thrown BusinessException
    }

    @Transactional
    def 'cant create book no genre'() {
        given:

        var book = BookEntity
                .builder()
                .name("Tom Soyer")
                .outlet("Питер")
                .files(new ArrayList<>(Collections.singletonList(
                        BookFile.builder().id(10L)
                                .build())))
                .author(new HashSet<>(Collections.singletonList(AuthorEntity
                        .builder()
                        .firstName("Mark")
                        .lastName("Twain")
                        .build())))
                .build()

        when:
        BookEntity created = bookService.create(book)

        then:
        thrown BusinessException
    }

    @Transactional
    def 'can find page of books'() {
        given:

        def filter = BookFilter.builder()
                .text("Tom").build()
        def request = FilteredQueryWithPagingRequest<BookFilter>.builder()
                .filter(filter)
                .pageNumber(10)
                .pageNumber(0)
                .build()

        when:
        Page page = bookService.getBookPage(request);


        then:

        page != null

        page.getTotalElements() == 1
        page.getContent().stream().toList().get(0).getAuthor().stream().toList().get(0).getId() == 10
        page.getContent().get(0).getName() == "Tom Soyer"
        page.getContent().get(0).getId()== 101
    }


}
