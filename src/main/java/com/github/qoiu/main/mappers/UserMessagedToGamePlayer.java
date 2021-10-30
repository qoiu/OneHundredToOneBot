package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.UserMessaged;
import com.github.qoiu.main.presenter.GamePlayer;

public class UserMessagedToGamePlayer implements Mapper<UserMessaged, GamePlayer> {
    @Override
    public GamePlayer map(UserMessaged data) {
        return new GamePlayer(data.getId(),data.getName(),0);
    }
}
