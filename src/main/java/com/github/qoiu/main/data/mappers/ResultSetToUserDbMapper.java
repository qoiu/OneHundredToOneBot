package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserDbMapper extends DbMapper.Result<UserDb> {

    public ResultSetToUserDbMapper(String sql) {
        super(sql);
    }

    @Override
    public UserDb map(ResultSet set) {
        try {
            long id = set.getLong("id");
            int status = set.getInt("state");
            String name = set.getString("name");
            int gamePlayed = set.getInt("gamesPlayed");
            int gameWin = set.getInt("gamesWin");
            int gameHighScore = set.getInt("highScore");
            return new UserDb(id,status,name,gamePlayed,gameWin,gameHighScore);
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
