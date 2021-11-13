package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperDeletePlayer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import static com.github.qoiu.main.StateStatus.CMD_MENU;

public class SendMapperGameCancelledClient extends SendMapper.Base {
    public SendMapperGameCancelledClient(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        new DbMapperDeletePlayer(db).map(userMessaged.getId());
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Меню", CMD_MENU);
        return base(userMessaged.getId(), "Хост завершил игру", btn);
    }
}
