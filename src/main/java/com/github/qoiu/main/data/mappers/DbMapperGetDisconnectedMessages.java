package com.github.qoiu.main.data.mappers;

import com.github.qoiu.main.data.DatabaseInterface;
import com.github.qoiu.main.data.MessageDb;

import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMapperGetDisconnectedMessages extends DbMapper.Base<List<MessageDb>, Null> {

    public DbMapperGetDisconnectedMessages(DatabaseInterface.Executor db) {
        super(db);
    }

    @Override
    public List<MessageDb> map(Null object) {
        ResultSet set = db.executeQuery("SELECT id,message FROM lostMessages");
        ArrayList<MessageDb> list = new ArrayList<>();
        try {
            while (set.next()){
                list.add(new MessageDb(set.getLong("id"), set.getInt("message")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
