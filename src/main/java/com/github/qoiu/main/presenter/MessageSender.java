package com.github.qoiu.main.presenter;

import com.github.qoiu.main.bot.BotChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendMessage(SendMessage sendMessage);
    void sendChatMessage(BotChatMessage chatMessage);
    void clearChat(long  id);


}
