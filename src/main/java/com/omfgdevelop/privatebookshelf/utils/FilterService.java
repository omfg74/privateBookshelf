package com.omfgdevelop.privatebookshelf.utils;

import com.vaadin.flow.data.provider.Query;
import org.springframework.stereotype.Service;

@Service
public class FilterService {
    public <T, E> FilteredQueryWithPagingRequest<T> getFilter(Query<E, String> s, T filter) {
        FilteredQueryWithPagingRequest<T> request = new FilteredQueryWithPagingRequest<>();
        request.setFilter(filter);
        request.setPageNumber(s.getPage());
        request.setPageSize(s.getPageSize());
        return request;
    }
}
