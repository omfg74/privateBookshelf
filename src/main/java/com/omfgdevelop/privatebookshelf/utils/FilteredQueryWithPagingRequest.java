package com.omfgdevelop.privatebookshelf.utils;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilteredQueryWithPagingRequest<T> {

    private T filter;

    private int pageNumber;
    private Integer pageSize;

}

