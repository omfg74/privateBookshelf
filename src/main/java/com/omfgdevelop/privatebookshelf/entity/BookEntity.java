package com.omfgdevelop.privatebookshelf.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_to_author",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<AuthorEntity> author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_to_genre",
            joinColumns = {@JoinColumn(name = "book_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<GenreEntity> genres;

    @Column(name = "image_url")
    private String image;

    @Column(name = "outlet")
    private String outlet;

    @OneToMany
    @JoinColumn(name = "book_id")
    private List<BookFile> files;
}
