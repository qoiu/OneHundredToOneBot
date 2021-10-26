package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;
import com.github.qoiu.main.data.UserInGameDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserInGameDbMapper implements DbMapper<UserInGameDb, ResultSet> {
    @Override
    public UserInGameDb map(ResultSet set) {

        try {
            long id = set.getLong("id");
            long gameId = set.getLong("gameId");
            int statusGame = set.getInt("statusGame");
            return new UserInGameDb(id,gameId,statusGame);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
