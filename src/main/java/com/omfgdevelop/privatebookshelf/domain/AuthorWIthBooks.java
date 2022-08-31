package com.omfgdevelop.privatebookshelf.domain;


import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorWIthBooks extends Author{
    private Set<Book> books;

}
