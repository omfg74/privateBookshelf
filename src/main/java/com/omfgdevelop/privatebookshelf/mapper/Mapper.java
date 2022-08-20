package com.omfgdevelop.privatebookshelf.mapper;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {

    @Mapping(target = "id", source = "id")
    Book map(BookEntity source);

    BookEntity map(Book source);

    Genre map(GenreEntity source);


}
