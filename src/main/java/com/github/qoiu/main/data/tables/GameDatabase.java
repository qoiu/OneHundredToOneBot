package com.github.qoiu.main.data.tables;

import com.github.qoiu.main.data.GameObject;

import java.util.List;

public interface GameDatabase {
    void createGameForPlayer(long id, String gameName);
    int getGameId(long id);
    List<GameObject> getAllGames();
    GameObject getGame(int gameId);
    void changeStatus(Long id, int state);

    void connectPlayer(int gameId, long playerId);
}
