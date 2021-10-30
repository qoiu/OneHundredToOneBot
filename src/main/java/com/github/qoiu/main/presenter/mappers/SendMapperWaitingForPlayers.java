package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.StateStatus;
import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseBase;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperCreateGameForPlayer;
import com.github.qoiu.main.mappers.UserMessagedToUserDb;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperWaitingForPlayers extends SendMapper.Base{
    public SendMapperWaitingForPlayers(DatabaseBase db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        TelegramBtn btn = new TelegramBtn();
        btn.addCollumn("Начать игру", "/startGame");
        new DbMapperCreateGameForPlayer(db).map(new UserMessagedToUserDb(StateStatus.PLAYER_IN_GAME).map(userMessaged));
        return base(userMessaged.getId(), "Ожидание других игроков", btn);
    }
}
