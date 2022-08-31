package com.omfgdevelop.privatebookshelf.domain;

import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Book {

    private Long id;

    private String name;

    private Set<Author> author;

    private Set<Genre> genres;

    private String image;

    private String outlet;

    private List<BookFile> files;
}
