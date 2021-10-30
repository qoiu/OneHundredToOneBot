package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.bot.BotMessage;
import com.github.qoiu.main.data.MessageDb;

import java.util.ArrayList;
import java.util.List;

public class MessagesDbToBotMessagesMapper implements Mapper<List<MessageDb>,List<BotMessage>> {
    @Override
    public List<BotMessage> map(List<MessageDb> data) {
        List<BotMessage> messages = new ArrayList<>();
        for (MessageDb message:data) {
            messages.add(new MessageDbToBotMessageMapper().map(message));
        }
        return messages;
    }
}
