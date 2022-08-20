package com.omfgdevelop.privatebookshelf.domain;


import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    private Long id;

    private String name;

}
