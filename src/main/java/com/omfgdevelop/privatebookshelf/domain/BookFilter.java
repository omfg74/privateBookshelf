package com.omfgdevelop.privatebookshelf.domain;

import com.omfgdevelop.privatebookshelf.utils.TextFiltering;
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
