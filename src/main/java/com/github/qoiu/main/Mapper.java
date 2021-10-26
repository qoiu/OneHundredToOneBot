package com.github.qoiu.main;

public interface Mapper<T,S> {
    S map(T data);
}
