package com.github.qoiu.main.presenter.mappers;

import com.github.qoiu.main.bot.TelegramBtn;
import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.GameObject;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.data.mappers.DbMapperAllGames;
import org.telegram.telegrambots.meta.api.methods.send.*;

import java.util.List;

import static com.github.qoiu.main.StateStatus.*;

public class SendMapperListOfGames extends SendMapper.Base{
    public SendMapperListOfGames(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public SendMessage map(UserMessaged userMessaged) {
        List<GameObject> games = new DbMapperAllGames(db).map(null);
        TelegramBtn btn = new TelegramBtn();
        if(games.size()>0){
            for (GameObject game:games) {
                btn.addColumn(game.getName(),CMD_CONNECT+":"+game.getId());
            }
            btn.addColumn("Меню",CMD_MENU);
            return base(userMessaged.getId(), "Список игр:",btn);
        }else {
            btn.addColumn("Новая игра", CMD_NEW_GAME);
            btn.addColumn("Меню",CMD_MENU);
            return base(userMessaged.getId(), "Нет начатых игр. \nБудьте первым!",btn);
        }
    }
}
