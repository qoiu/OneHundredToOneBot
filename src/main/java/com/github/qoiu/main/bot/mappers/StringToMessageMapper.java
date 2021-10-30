package com.github.qoiu.main.bot.mappers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StringToMessageMapper extends ToMessageMapper.Base<String>{
    public StringToMessageMapper(String id) {
        super(id);
    }

    @Override
    public SendMessage map(String data) {
        return base(data);
    }
}
