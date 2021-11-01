package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.UserMessaged;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperSimpleText extends SendMapper.Base {
    public SendMapperSimpleText() {
        super(null);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        return base(userMessaged.getId(),
                userMessaged.getMessage());
    }
}
