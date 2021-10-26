package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.UserInGameDb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetToListUserInGameMapper implements DbMapper<List<UserInGameDb>, ResultSet> {

    @Override
    public List<UserInGameDb> map(ResultSet set) {
        List<UserInGameDb> list = new ArrayList<>();
        try {
            while (set.next()) {
                list.add(new ResultSetToUserInGameDbMapper().map(set));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
