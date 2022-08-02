package com.omfgdevelop.privatebookshelf.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genre")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class GenreEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    private Set<BookEntity> books;
}
