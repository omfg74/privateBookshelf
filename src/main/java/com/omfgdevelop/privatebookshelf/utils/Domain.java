package com.omfgdevelop.privatebookshelf.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class Domain {


    public static <T> Specification<T> byString(String fieldForSearch, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.equals("")) return null;
            query.distinct(true);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldForSearch)), "%" + value.toLowerCase() + "%");
        };
    }

    public static <T> Specification<T> orderBy(String field) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.orderBy(criteriaBuilder.asc(root.get(field)));
                return criteriaBuilder.conjunction();
            }
        };
    }
}
