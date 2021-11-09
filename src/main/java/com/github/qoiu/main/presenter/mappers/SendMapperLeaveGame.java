package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperClearGame;
import com.github.qoiu.main.presenter.GamePlayer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperLeaveGame extends SendMapper.Base {
    GamePlayer player;

    public SendMapperLeaveGame(DatabaseInterface.Executor db, GamePlayer player) {
        super(db);
        this.player = player;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        new DbMapperClearGame(db).map(player.getId());
        btn.addColumn("В меню", "/menu");
        return base(userMessaged.getId(),
                "Очень жаль",
                btn);
    }
}
