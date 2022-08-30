package com.omfgdevelop.privatebookshelf.utils;

import com.omfgdevelop.privatebookshelf.exception.ValidationError;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validator {

    public static <T> Function<T, Optional<ValidationError>> validateFunc(Predicate<T> predicate, ValidationError validationError) {
        return t -> predicate.test(t) ? Optional.empty() : Optional.of(validationError);
    }


    public static <T> ValidationError validate(T obj, Function<T, Optional<ValidationError>> function) {
        return function.apply(obj).orElse(null);
    }

    public static ValidationError validateString(String obj, ValidationError validationError) {
        return validate(obj, validateFunc(s -> obj != null && !obj.isEmpty(), validationError));
    }

    public static <T> ValidationError validateNotNull(T obj, ValidationError validationError) {
        return validate(obj, validateFunc(s -> obj != null, validationError));
    }

    public static <T> ValidationError validateNotEmpty(Collection<T> obj, ValidationError validationError) {
        return validate(obj, validateFunc(s -> !obj.isEmpty(), validationError));
    }
}
