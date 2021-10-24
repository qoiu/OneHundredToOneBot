package com.github.qoiu.main.presenter;

import com.github.qoiu.main.data.GameObject;

import java.util.List;

public interface MainPresenterMessageSenderInterface {
    int getGameId(long id);
    GameObject getGameByHostId(long id);
    List<GameObject> getAllGames() ;
}
