package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.data.MessageDb;

public class MessageDbToBotMessageMapper implements Mapper<MessageDb, BotMessage> {
    @Override
    public BotMessage map(MessageDb data) {
        return new BotMessage(data.getPlayerId(),data.getMessageId());
    }
}
