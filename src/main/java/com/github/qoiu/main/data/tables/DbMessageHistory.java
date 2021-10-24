package com.github.qoiu.main.data.tables;

import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbMessageHistory extends DatabaseBase implements MessageHistoryDatabase {
    @Override
    public List<Pair<Long, Integer>> getDisconnectedMessages() {
        ResultSet set = executeQuery("SELECT id,message FROM lostMessages");
        ArrayList<Pair<Long, Integer>> list = new ArrayList<>();
        try {
            while (set.next()){
                list.add(new Pair<>(set.getLong("id"), set.getInt("message")));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMsg(Pair<Long, Integer> pair) {
        execute("INSERT INTO lostMessages (id, message) VALUES (?,?)",pair.getKey(),pair.getValue());
    }

    @Override
    public void deletedMsg(Pair<Long, Integer> pair) {
        execute("DELETE FROM lostMessages WHERE  id=? AND message =?",  pair.getKey(),pair.getValue());
    }
}
