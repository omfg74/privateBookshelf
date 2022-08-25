package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.BookFilter;
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import com.omfgdevelop.privatebookshelf.filtr.AuthorFilter;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.AuthorRepository;
import com.omfgdevelop.privatebookshelf.utils.Domain;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.omfgdevelop.privatebookshelf.utils.SearchProcessor;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final UberMapper mapper;

    public List<Author> findAll() {
        return authorRepository.findAll().stream().map(mapper::map).toList();
    }

    public Page<Author> findPage(Query<Author, String> query) {
        FilteredQueryWithPagingRequest<AuthorFilter> request = new FilteredQueryWithPagingRequest<>();
        request.setFilter(AuthorFilter.builder().text(query.getFilter().orElse(null)).build());
        request.setSortingFields(List.of(Author.Fields.lastName, Author.Fields.lastName));
        request.setSortDirection(Sort.Direction.ASC);
        request.setPageSize(query.getPageSize());
        request.setPageNumber(query.getPage());
        return SearchProcessor.findPage(request, authorRepository, this::getWhereClose, this::pageProcessor, 50);
    }

    private List<Author> pageProcessor(Page<AuthorEntity> author) {
        return author.stream().map(mapper::map).toList();
    }

    private Specification<AuthorEntity> getWhereClose(AuthorFilter filter) {
        return Domain.<AuthorEntity>byString(AuthorEntity.Fields.lastName, filter.getText()).or(Domain.byString(AuthorEntity.Fields.firstName, filter.getText()));
    }

    public int getCount(AuthorFilter filter) {
        return (int) authorRepository.count(getWhereClose(filter));
    }
}
