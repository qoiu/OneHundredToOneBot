package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.BotChatMessage;
import com.github.qoiu.main.presenter.GameMessage;

public class GameMessageToSendChatMessageMapper implements Mapper<GameMessage, BotChatMessage> {

    public GameMessageToSendChatMessageMapper() {
    }

    @Override
    public BotChatMessage map(GameMessage data) {
        return new BotChatMessage(data.getHeader(),data.getText(),data.getFrom());
    }
}
