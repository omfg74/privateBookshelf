package com.omfgdevelop.privatebookshelf.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookFile {
    private Long id;

    private String name;

    private String fileExtension;

}
