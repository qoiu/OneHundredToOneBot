package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperGameByPlayerId;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperGameCancelledClient extends SendMapper.Base {
    public SendMapperGameCancelledClient(DatabaseBase db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        int gameId =new DbMapperGameByPlayerId(db).map(userMessaged.getId());
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Отменить", "/menu");
        return base(userMessaged.getId(), "Хост завершил игру", btn);
    }
}
