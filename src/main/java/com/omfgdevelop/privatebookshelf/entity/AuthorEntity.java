package com.omfgdevelop.privatebookshelf.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "author")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany
    private Set<BookEntity> books;

}