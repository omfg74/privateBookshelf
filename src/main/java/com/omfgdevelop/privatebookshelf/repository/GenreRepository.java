package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GenreRepository extends JpaRepository<GenreEntity, Long>, JpaSpecificationExecutor<GenreEntity> {
}
