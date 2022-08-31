package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.domain.BookFile;
import com.omfgdevelop.privatebookshelf.entity.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookFileRepository extends JpaRepository<BookFileEntity, Long> {

    List<BookFileEntity> findAllByBookId(@Param("book_id") Long booId);

}
