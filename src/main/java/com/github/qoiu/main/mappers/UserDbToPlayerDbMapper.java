package com.github.qoiu.main.mappers;

import com.github.qoiu.main.Mapper;
import com.github.qoiu.main.data.PlayerDb;
import com.github.qoiu.main.data.UserDb;

public class UserDbToPlayerDbMapper implements Mapper<UserDb, PlayerDb> {

    private long gameId;
    public UserDbToPlayerDbMapper(long gameId){
        this.gameId = gameId;
    }

    @Override
    public PlayerDb map(UserDb data) {
        return new PlayerDb(data.getId(),gameId,data.getStatusId());
    }
}
