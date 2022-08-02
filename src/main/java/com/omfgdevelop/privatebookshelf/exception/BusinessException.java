package com.omfgdevelop.privatebookshelf.exception;

import lombok.Getter;

public class BusinessException extends Exception {


    @Getter
    private BusinessError error;

    public BusinessException(BusinessError error) {
        this.error = error;
    }
}
