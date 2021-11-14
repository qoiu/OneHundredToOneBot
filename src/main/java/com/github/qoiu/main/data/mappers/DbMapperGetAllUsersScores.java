package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.UserDb;

import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMapperGetAllUsersScores extends DbMapper.Base<List<UserDb>, Null>{
    public DbMapperGetAllUsersScores(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public List<UserDb> map(Null nullable) {
        List<UserDb> result = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY highScore DESC";
        ResultSet set = db.executeQuery(sql);
        try {
            while (set.next()) {
                result.add(new ResultSetToUserDbMapper(sql).map(set));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
