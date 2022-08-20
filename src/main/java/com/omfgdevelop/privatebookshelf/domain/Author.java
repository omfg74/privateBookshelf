package com.omfgdevelop.privatebookshelf.domain;

import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private Long id;

    private String firstName;

    private String lastName;

}
