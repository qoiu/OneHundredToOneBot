package com.github.qoiu.main.data.tables;

import java.sql.*;

public abstract class DatabaseBase {

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

    int executeUpdate(String sql, Object... args){
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            int id = statement.executeUpdate();
            statement.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

    public static void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
