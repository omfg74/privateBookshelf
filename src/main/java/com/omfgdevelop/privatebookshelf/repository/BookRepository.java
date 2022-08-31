package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<BookEntity,Long> , JpaSpecificationExecutor<BookEntity> {
}
