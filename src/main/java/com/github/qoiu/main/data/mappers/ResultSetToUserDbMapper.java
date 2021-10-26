package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserDb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToUserDbMapper implements DbMapper<UserDb, ResultSet> {
    @Override
    public UserDb map(ResultSet set) {

        try {
            long id = set.getLong("id");
            int status = set.getInt("state");
            String name = set.getString("name");
            return new UserDb(id,status,name);
        } catch (SQLException e) {
            e.printStackTrace();
            return new UserDb(0,0,"");
        }
    }
}
