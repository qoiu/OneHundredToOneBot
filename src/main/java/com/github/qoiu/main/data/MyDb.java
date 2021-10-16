package com.github.qoiu.main.data;

import javafx.util.Pair;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyDb extends DatabaseBase implements WorkWithDb {

    private static final String ERROR = "Error";

    @Override
    public void addUser(long id, String name) {
        execute("INSERT INTO users (id, name, state) VALUES (?,?,0)", id, name);
    }

    private String userSelect(String type, long id) {
        ResultSet result = executeQuery("SELECT*FROM users WHERE id = " + id);
        try {
            return result.getString(type);
        } catch (SQLException|NullPointerException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public Boolean isUserExists(long id) {
        String name = userSelect("name", id);
        return (!name.equals(ERROR)) && !name.isEmpty();
    }

    public String getUserNameById(long id) {
        String name = userSelect("name",id);
        return (name.equals(ERROR))?"":name;
    }

    public void changeUserState(long id, int state) {
        executeUpdate("UPDATE users SET state = ? WHERE id = ?",state,id);
    }

    public Integer getUserState(long id) {
        ResultSet result = executeQuery("SELECT id,name,state FROM users WHERE id = ?;", id);
        try {
            return result==null?0:result.getInt("state");
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void createGameForPlayer(long id, String gameName) {
        try {
            execute("DELETE FROM userInGame WHERE userId = " + id);
            execute("DELETE FROM game WHERE userId = " + id);
            execute("INSERT INTO game(gameName,hostDialogId) VALUES (\"newGame\"," + id + ")");
            int gameId = executeQuery("SELECT id FROM game ORDER BY id DESC LIMIT 1").getInt("id");
            execute("INSERT INTO userInGame (gameId, userId) VALUES (?,?)",gameId,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getGameId(long playerId){
        try {
            return executeQuery("SELECT id FROM game WHERE hostDialogId = " + playerId).getInt("id");
        } catch (SQLException e) {
            return 0;
        }
    }

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
