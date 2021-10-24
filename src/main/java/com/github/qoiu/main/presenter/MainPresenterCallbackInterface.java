package com.github.qoiu.main.presenter;

import org.telegram.telegrambots.meta.api.objects.User;

public interface MainPresenterCallbackInterface {
    void startGame(User user);
    void startGameForAll(User user);
    void connectPlayer(int gameId, long playerId);
}
