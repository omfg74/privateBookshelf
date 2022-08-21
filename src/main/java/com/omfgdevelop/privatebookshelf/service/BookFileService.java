package com.omfgdevelop.privatebookshelf.service;

import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import com.omfgdevelop.privatebookshelf.mapper.UberMapper;
import com.omfgdevelop.privatebookshelf.repository.BookFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookFileService {

    private final BookFileRepository bookFileRepository;

    private final UberMapper mapper;


    public List<BookFile> findAllByBookId(Long bookId) {
        List<BookFileEntity> bookFiles = bookFileRepository.findAllByBookId(bookId);
        return bookFiles.stream().map(mapper::map).toList();
    }
}
