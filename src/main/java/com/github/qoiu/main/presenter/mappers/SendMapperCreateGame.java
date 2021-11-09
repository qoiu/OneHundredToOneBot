package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.StateStatus;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperCreateGameForPlayer;
import com.github.qoiu.main.mappers.UserMessagedToUserDb;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperCreateGame extends SendMapper.Base{
    public SendMapperCreateGame(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        new DbMapperCreateGameForPlayer(db).map(new UserMessagedToUserDb(StateStatus.PLAYER_IN_GAME).map(userMessaged));
        return new SendMapperWaitingForPlayersHost(db).map(userMessaged);
    }
}
