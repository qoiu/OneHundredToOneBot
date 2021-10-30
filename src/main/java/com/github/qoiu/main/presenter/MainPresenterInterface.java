package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.data.UserMessaged;

import java.util.List;

public interface MainPresenterInterface {
    List<BotMessage> getDisconnectedMessages();
    void saveMsg(BotMessage msg);
    void deletedMsg(BotMessage msg);
    void receiveMessage(UserMessaged userMessaged);
    void receiveCallback(UserMessaged userMessaged);
}
