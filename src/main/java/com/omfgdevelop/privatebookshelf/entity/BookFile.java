package com.omfgdevelop.privatebookshelf.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "file")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFile {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "file_extension")
    private String fileExtension;


}
