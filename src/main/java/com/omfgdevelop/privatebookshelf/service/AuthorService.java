package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.Author;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final UberMapper mapper;

    public List<Author> findAll(){
        return authorRepository.findAll().stream().map(mapper::map).toList();
    }
}
