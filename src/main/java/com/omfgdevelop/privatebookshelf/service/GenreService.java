package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.domain.Book;
import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import com.omfgdevelop.privatebookshelf.filtr.AuthorFilter;
import com.omfgdevelop.privatebookshelf.filtr.GenreFilter;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.GenreRepository;
import com.omfgdevelop.privatebookshelf.utils.Domain;
import com.omfgdevelop.privatebookshelf.utils.FilteredQueryWithPagingRequest;
import com.omfgdevelop.privatebookshelf.utils.SearchProcessor;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private final UberMapper mapper;

    public List<Genre> findAll() {
        return genreRepository.findAll().stream().map(mapper::map).toList();
    }

    public Page<Genre> findPage(Query<Genre, String> query) {
        FilteredQueryWithPagingRequest<GenreFilter> request = new FilteredQueryWithPagingRequest<>();
        request.setFilter(GenreFilter.builder().text(query.getFilter().orElse(null)).build());
        request.setSortingFields(List.of(GenreEntity.Fields.name));
        request.setSortDirection(Sort.Direction.ASC);
        request.setPageNumber(query.getPage());
        request.setPageSize(query.getPageSize());
        return SearchProcessor.findPage(request, genreRepository, this::getWhereClose, this::pageProcessor, 50);
    }

    private List<Genre> pageProcessor(Page<GenreEntity> author) {
        return author.stream().map(mapper::map).toList();
    }

    private Specification<GenreEntity> getWhereClose(GenreFilter filter) {
        return Domain.byString(GenreEntity.Fields.name, filter.getText());
    }

    public int getCount(GenreFilter filter) {
        return (int) genreRepository.count();
    }
}
