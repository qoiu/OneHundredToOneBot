package com.github.qoiu.main.data;

import javafx.util.Pair;

import java.util.List;

public interface WorkWithDb  {
    void addUser(long id, String name);
    Boolean isUserExists(long id);
    String getUserNameById(long id);
    void changeUserState(long id, int state);
    Integer getUserState(long id);
    void createGameForPlayer(long id, String gameName);
    int getGameId(long id);
    List<Pair<Long,Integer>> getDisconnectedMessages();
    void saveMsg(Pair<Long,Integer> pair);
    void deletedMsg(Pair<Long,Integer> pair);
}
