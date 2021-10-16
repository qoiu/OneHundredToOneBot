package com.github.qoiu.main;

import javafx.util.Pair;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface MainPresenterInterface {
    List<Pair<Long,Integer>> getDisconnectedMessages();
    void saveMsg(Pair<Long,Integer> pair);
    void deletedMsg(Pair<Long,Integer> pair);
    void receiveMessage(User user, String message);
    void receiveCallback(User user, String message);
}
