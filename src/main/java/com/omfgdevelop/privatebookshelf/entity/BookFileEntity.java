package com.omfgdevelop.privatebookshelf.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFileEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "book_id")
    private Long bookId;

}
