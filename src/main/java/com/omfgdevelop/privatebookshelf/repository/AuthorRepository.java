package com.omfgdevelop.privatebookshelf.repository;

import com.omfgdevelop.privatebookshelf.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorRepository extends JpaRepository<AuthorEntity,Long>, JpaSpecificationExecutor<AuthorEntity> {
}
