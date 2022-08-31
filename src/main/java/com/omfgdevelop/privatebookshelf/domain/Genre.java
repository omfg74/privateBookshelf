package com.omfgdevelop.privatebookshelf.domain;


import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Genre implements Serializable {

    private Long id;

    private String name;

}
