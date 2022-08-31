package com.omfgdevelop.privatebookshelf.utils;

import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataProviderFactory {

    public <T, Q, F, R> DataProvider<T, F> getProvider(Class<T> clazz, PageableRepository<T, F> repository, CountableRepository<T> countableRepository, Function<T, R> preParefilter) {

        if (clazz.isInstance(Book.class)) {
            return DataProvider.fromFilteringCallbacks((CallbackDataProvider.FetchCallback<T, F>) query -> {
                Page<T> page = repository.findPage(query, null, null);
                return page.stream();
            }, (CallbackDataProvider.CountCallback<T, F>) query -> countableRepository
                    .count(null));

        }
        return null;
    }


    private <T, E> FilteredQueryWithPagingRequest<T> getFilter(Query<E, String> s, T filter) {
        FilteredQueryWithPagingRequest<T> request = new FilteredQueryWithPagingRequest<>();
        request.setFilter(filter);
        request.setPageNumber(s.getPage());
        request.setPageSize(s.getPageSize());
        return request;
    }
}
