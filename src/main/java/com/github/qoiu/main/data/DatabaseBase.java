package com.github.qoiu.main.data;

import java.sql.*;

public class DatabaseBase {

    private static Connection connection;
    private static Statement statement;
    public static final String ERROR = "Error";

    public DatabaseBase(String url) {
        start(url);
    }

    public void execute(String sql, Object... args) {
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
//            PreparedStatement statement = statementWithArgs(sql, args);
            //            statement.close();
//            return createStatement().executeQuery(sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getString(String sql) {
//            PreparedStatement statement = statementWithArgs(sql, args);
            //            statement.close();
//            return createStatement().executeQuery(sql);

        try {
            ResultSet set = connection.prepareStatement(sql).executeQuery();
            return set.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return "";
    }


    public int executeUpdate(String sql, Object... args) {
        try {
            PreparedStatement statement = statementWithArgs(sql, args);
            int id = -1;
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next())
                id = rs.getInt(1);
            statement.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Statement createStatement() throws SQLException {
        return connection.createStatement(
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

    }

    private PreparedStatement statementWithArgs(String sql, Object... args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        if (args.length != 0) {
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

    public void start(String url) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    public void clearDatabase() {
        clearTable("userInGame");
        clearTable("game");
        clearTable("users");
        clearTable("lostMessages");
//        clearTable("questions");
    }

    private void clearTable(String str) {
        execute("DELETE FROM " + str);
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
