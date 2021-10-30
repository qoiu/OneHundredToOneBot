package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.data.MessageDb;

public class BotMessageToMessageDbMapper implements Mapper<BotMessage, MessageDb> {
    @Override
    public MessageDb map(BotMessage data) {
        return new MessageDb(data.getChatId(),data.getMessageId());
    }
}
