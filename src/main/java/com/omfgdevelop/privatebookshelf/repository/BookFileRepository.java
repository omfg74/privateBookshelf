package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Long> {
}
