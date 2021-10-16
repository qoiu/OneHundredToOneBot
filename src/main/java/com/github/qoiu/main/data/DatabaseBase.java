package com.github.qoiu.main.data;

import java.sql.*;

abstract class DatabaseBase implements DbService{

    private static Connection connection;

    void execute(String sql, Object... args){
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet executeQuery(String sql, Object...args){
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void executeUpdate(String sql, Object... args){
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement statementWithArgs(String sql, Object... args) throws SQLException {
        PreparedStatement statement =connection.prepareStatement(sql);
        if(args.length!=0) {
            int count = 1;
            for (Object arg : args) {
                if (arg instanceof Integer) {
                    statement.setInt(count, (Integer) arg);
                    count++;
                }
                if (arg instanceof String) {
                    statement.setString(count, (String) arg);
                    count++;
                }
                if (arg instanceof Long) {
                    statement.setLong(count, (Long) arg);
                    count++;
                }
            }
        }
        return statement;
    }

    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
            execute("UPDATE users SET state = 0");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
