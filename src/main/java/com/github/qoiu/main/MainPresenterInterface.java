package com.github.qoiu.main;

import org.telegram.telegrambots.meta.api.objects.User;

public interface MainPresenterInterface {
    void receiveMessage(User user, String message);
    void receiveCallback(User user, String message);
}
