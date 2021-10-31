package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperClearGame;
import com.github.qoiu.main.data.mappers.DbMapperGetGameByHostId;
import com.github.qoiu.main.presenter.PlayerNotifier;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperMainMenu extends SendMapper.Base {
    PlayerNotifier notifier;

    public SendMapperMainMenu(DatabaseBase db, PlayerNotifier notifier) {
        super(db);
        this.notifier = notifier;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        GameObject game = new DbMapperGetGameByHostId(db).map(userMessaged.getId());
        if (game != null) {
            notifier.notifyGameCanceled(game);
            new DbMapperClearGame(db).map(userMessaged.getId());
        }
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Новая игра", "/start");
        btn.addColumn("Присоединиться", "/connecting");
        btn.addColumn("Участвовать в опросе", "/quiz");
        return base(userMessaged.getId(),
                "Привет, рад тебя видеть!\nТы уже готов начать новую игру?",
                btn);
    }
}
