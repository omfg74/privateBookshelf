package com.omfgdevelop.privatebookshelf.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.function.Function;


public class SearchProcessor {


    public static <E, V, F> PageImpl<V> findPage(FilteredQueryWithPagingRequest<F> request,
                                                 JpaSpecificationExecutor<E> repository,
                                                 Function<F, Specification<E>> getWhereCLose,
                                                 Function<Page<E>, List<V>> pageProcessor,
                                                 int maxPageSize) {

        Specification<E> whereClose = getWhereCLose.apply(request.getFilter());
        final int actualPageSize = request.getPageSize() != null ? request.getPageSize() : maxPageSize;
        PageRequest pageRequest = PageRequest.of(Math.max(request.getPageNumber(), 0), actualPageSize);
        if (request.getSortingFields() != null && !request.getSortingFields().isEmpty()) {
            pageRequest = PageRequest.of(Math.max(request.getPageNumber(), 0), actualPageSize,
                    Sort.by(request.getSortDirection(), request.getSortingFields().toArray(new String[0])));
        }
        final Page<E> page = repository.findAll(whereClose, pageRequest);

        return new PageImpl<>(pageProcessor.apply(page), pageRequest, page.getTotalPages());
    }
}
