package com.omfgdevelop.privatebookshelf.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFilter {

    private String text;

    private Author author;

    private Genre genre;

}
