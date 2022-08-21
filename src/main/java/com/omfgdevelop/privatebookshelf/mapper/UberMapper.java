package com.omfgdevelop.privatebookshelf.mapper;

import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UberMapper {

    @Mapping(target = "id", source = "id")
    Book map(BookEntity source);

    BookEntity map(Book source);

    Genre map(GenreEntity source);


    Author map(AuthorEntity authorEntity);

    BookFile map(BookFileEntity bookFile);

    BookFile map(BookFile source);
}
