package com.omfgdevelop.privatebookshelf.exception;

public enum BusinessError {
    NO_GENRES_SET_TO_BOOK(1000, "Книге не присвоен ни один жанр"),
    NO_AUTHORS_SET_TO_BOOK(1001, "Книге не присвоен ни один автор"),

    NO_FILES_SET_TO_BOOK(1002, "Книге не присвоен ни один файл"),

    WRONG_FILE_PARAMS_SET(1003,"Указан несуществующий файл");


    public final int code;
    public final String description;

    BusinessError(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
