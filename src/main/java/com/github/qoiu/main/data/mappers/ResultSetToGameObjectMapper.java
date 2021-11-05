package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.GameObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetToGameObjectMapper extends DbMapper.Result<GameObject, ResultSet> {

    public ResultSetToGameObjectMapper(String sql) {
        super(sql);
    }

    @Override
    public GameObject map(ResultSet set) {
        try {
            String name = set.getString("gameName");
            long hostId = set.getLong("hostDialogId");
            int id = set.getInt("id");
            return new GameObject(name, hostId, id);
        } catch (SQLException e) {
            return exception(e);
        }
    }
}
