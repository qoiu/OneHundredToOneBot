package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperAddPlayer;
import com.github.qoiu.main.mappers.UserMessagedToUserDb;
import com.github.qoiu.main.presenter.PlayerNotifier;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMapperAddPlayerToGame extends SendMapper.Base {
    private PlayerNotifier notifier;
    public SendMapperAddPlayerToGame(DatabaseInterface.Executor db, PlayerNotifier notifier) {
        super(db);
        this.notifier = notifier;
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        try {
            String[] msg = userMessaged.getMessage().split(":");
            int gameId = Integer.parseInt(msg[1]);
            new DbMapperAddPlayer(db, gameId).map(new UserMessagedToUserDb(3).map(userMessaged));
            notifier.notifyGamePlayersChanged(gameId);
            return null;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return base(userMessaged.getId(), "Неверный код игры");
        }
    }
}
