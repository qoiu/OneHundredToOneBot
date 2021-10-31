package com.github.qoiu.main.presenter;

import com.github.qoiu.main.data.GameObject;

public interface PlayerNotifier {
    void notifyGamePlayersChanged(int gameId);
    void notifyGameCanceled(GameObject game);
}
