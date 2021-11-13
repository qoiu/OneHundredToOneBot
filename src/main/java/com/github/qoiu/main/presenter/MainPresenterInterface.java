package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.data.UserMessaged;

import java.util.List;
import java.util.Set;

public interface MainPresenterInterface {
    List<BotMessage> getDisconnectedMessages();
    Set<Long> getAllUsers();
    void saveMsg(BotMessage msg);
    void deletedMsg(BotMessage msg);
    void receiveMessage(UserMessaged userMessaged);
    void receiveCallback(UserMessaged userMessaged);
}
