package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;

import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DbMapperGetAllUsersId extends DbMapper.Base<Set<Long>, Null>{
    public DbMapperGetAllUsersId(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public Set<Long> map(Null object) {
        Set<Long> result = new HashSet<>();
        String sql = "SELECT id FROM users";
        ResultSet set = db.executeQuery(sql);
        try {
            while (set.next()) {
                result.add(set.getLong("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
}
