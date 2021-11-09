package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperDeletePlayer;
import com.github.qoiu.main.data.mappers.DbMapperGameIdByPlayerId;
import com.github.qoiu.main.presenter.PlayerNotifier;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperMainMenu extends SendMapper.Base {
    PlayerNotifier notifier;

    public SendMapperMainMenu(DatabaseInterface.Executor db, PlayerNotifier notifier) {
        super(db);
        this.notifier = notifier;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        Integer game = new DbMapperGameIdByPlayerId(db).map(userMessaged.getId());
        if (game != null) {
            new DbMapperDeletePlayer(db).map(userMessaged.getId());
            notifier.notifyGamePlayersChanged(game);
        }
        TelegramBtn btn = new TelegramBtn();
        btn.addColumn("Новая игра", "/newGame");
        btn.addColumn("Присоединиться", "/connecting");
        btn.addColumn("Участвовать в опросе", "/quiz");
        return base(userMessaged.getId(),
                "Привет, рад тебя видеть!\nТы уже готов начать новую игру?",
                btn);
    }
}
