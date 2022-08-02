package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity,Long> {
}
