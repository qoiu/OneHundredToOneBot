package com.github.qoiu.main.data.tables;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUsers extends DatabaseBase implements UserDatabase {

    private static final String ERROR = "Error";

    @Override
    public void addUser(long id, String name) {
        execute("INSERT INTO users (id, name, state) VALUES (?,?,0)", id, name);
    }

    private String userSelect(String type, long id) {
        ResultSet result = executeQuery("SELECT*FROM users WHERE id = " + id);
        try {
            return result.getString(type);
        } catch (SQLException |NullPointerException e) {
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
}
