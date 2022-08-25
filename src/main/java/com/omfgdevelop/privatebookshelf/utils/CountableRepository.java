package com.omfgdevelop.privatebookshelf.utils;

public interface CountableRepository<T> {

    int count(T filter);
}
