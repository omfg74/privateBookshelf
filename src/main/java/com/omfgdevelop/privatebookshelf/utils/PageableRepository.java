package com.omfgdevelop.privatebookshelf.utils;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.management.Query;
import java.util.List;

public interface PageableRepository<T, F> {

    Page<T> findPage(com.vaadin.flow.data.provider.Query<T, F> query, Sort.Direction sortDirection, List<String> sortingFields);
}
