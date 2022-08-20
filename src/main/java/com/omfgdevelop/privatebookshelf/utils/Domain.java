package com.omfgdevelop.privatebookshelf.utils;

import org.springframework.data.jpa.domain.Specification;

public class Domain {


    public static <T> Specification<T> byString(String fieldForSearch, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null||value.equals("")) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldForSearch)), "%" + value.toLowerCase() + "%");
        };
    }
}
