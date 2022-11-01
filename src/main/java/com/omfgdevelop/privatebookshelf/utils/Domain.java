package com.omfgdevelop.privatebookshelf.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class Domain {


    public static <T> Specification<T> byString(String fieldForSearch, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.equals("")) return null;
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldForSearch)), "%" + value.toLowerCase() + "%");
        };
    }

    public static <T, JoinColumn> Specification<T> byStringJoin(String joinColumnName, String fieldForSearch, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.equals("")) return null;
            Join<JoinColumn, T> join = root.join(joinColumnName, JoinType.LEFT);
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(join.get(fieldForSearch)), "%" + value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> orderBy(String field) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(field)));
            return criteriaBuilder.conjunction();
        };
    }
}
