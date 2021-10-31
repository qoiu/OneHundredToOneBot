package com.github.qoiu.main.bot;

import com.github.qoiu.main.presenter.MainPresenterInterface;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotInterface {
    void setPresenter(MainPresenterInterface presenter);

    void sendMessage(SendMessage message);

    void startGame(long id);

    void sendChatMessage(BotChatMessage chatMessage);

    void clearChat(long  id);
}
