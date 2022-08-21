package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Genre;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
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
}
