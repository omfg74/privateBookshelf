package com.omfgdevelop.privatebookshelf.utils;


import com.vaadin.flow.data.provider.SortDirection;
import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilteredQueryWithPagingRequest<T> {

    private T filter;

    private int pageNumber;
    private Integer pageSize;

    private List<String> sortingFields;

    private Sort.Direction sortDirection;

}

