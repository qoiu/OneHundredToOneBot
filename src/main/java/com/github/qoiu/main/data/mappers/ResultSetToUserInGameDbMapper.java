package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.PlayerDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserInGameDbMapper implements DbMapper<PlayerDb, ResultSet> {
    @Override
    public PlayerDb map(ResultSet set) {

        try {
            long id = set.getLong("id");
            long gameId = set.getLong("gameId");
            int statusGame = set.getInt("statusGame");
            String name = set.getString("name");
            return new PlayerDb(id,gameId,statusGame,name);
        } catch (SQLException e) {
            e.printStackTrace();
            return new PlayerDb(0,0,0);
        }
    }
}
