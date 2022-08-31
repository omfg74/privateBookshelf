package com.omfgdevelop.privatebookshelf.filtr;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthorFilter {

    String text;
}
