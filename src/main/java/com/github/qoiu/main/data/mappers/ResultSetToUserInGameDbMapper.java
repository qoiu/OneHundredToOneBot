package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.PlayerDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserInGameDbMapper extends DbMapper.Result<PlayerDb, ResultSet> {

    public ResultSetToUserInGameDbMapper(String sql) {
        super(sql);
    }

    @Override
    public PlayerDb map(java.sql.ResultSet set) {
        try {
            long id = set.getLong("id");
            long gameId = set.getLong("gameId");
            int statusGame = set.getInt("statusGame");
            String name = set.getString("name");
            return new PlayerDb(id, gameId, statusGame, name);
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
