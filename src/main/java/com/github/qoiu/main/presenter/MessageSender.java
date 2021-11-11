package com.github.qoiu.main.presenter;

import com.github.qoiu.main.Question;

public interface MessageSender {
    void sendMessage(GameMessage sendMessage);
    void sendChatMessage(GameMessage chatMessage);
    void clearChat(long  id);
    void updateQuestion(Question question, GameMessage message);


}
