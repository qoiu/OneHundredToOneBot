package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.presenter.GamePlayer;

public class GamePlayerToUserMessagedMapper implements Mapper<GamePlayer, UserMessaged> {
    @Override
    public UserMessaged map(GamePlayer data) {
        return new UserMessaged(data.getName(),data.getId(),"");
    }
}
