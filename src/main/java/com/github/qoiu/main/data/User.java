package com.github.qoiu.main.data;

public interface User {
    void add(long id, String name);
    String getById(long id);
    void changeState(long id, int state);
    Integer getState(long id);
}
