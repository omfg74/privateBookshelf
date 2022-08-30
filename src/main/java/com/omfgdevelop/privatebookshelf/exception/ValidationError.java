package com.omfgdevelop.privatebookshelf.exception;

public enum ValidationError {
    GENRE_IS_NOT_SET(1000,"Select genre from the list"),
    AUTHOR_IS_NOT_SET(1001,"Select author from the list"),
    BOOK_NAME_IS_NOT_SET(1002,"Enter book name");
    public final int code;
    public final String description;

    ValidationError(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
