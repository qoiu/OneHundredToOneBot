package com.github.qoiu.main.data.tables;

public interface UserDatabase {
    void addUser(long id, String name);
    Boolean isUserExists(long id);
    String getUserNameById(long id);
    void changeUserState(long id, int state);
    Integer getUserState(long id);
}
