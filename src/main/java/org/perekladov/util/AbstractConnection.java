package org.perekladov.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractConnection {
    private static String url = "jdbc:h2:file:./data";
    private static String username = "sa";
    private static String password = "";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            if (connection == null) {
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException(e);
        }
        return connection;
    }

    public static void close() throws SQLException {
        if(connection != null){
            connection.close();
        }
        connection = null;
    }
}