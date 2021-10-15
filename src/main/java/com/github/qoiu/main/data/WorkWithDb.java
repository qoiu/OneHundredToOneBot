package com.github.qoiu.main.data;

public interface WorkWithDb  {
    void addUser(long id, String name);
    Boolean isUserExists(long id);
    String getUser(long id);
    void changeUserState(long id, int state);
    Integer getUserState(long id);
    void createGameForPlayer(long id);
    int getGameId(long id);
}
