package io.muic.ooc.webapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection init() throws ClassNotFoundException, SQLException {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3307/";
        final String DB_NAME = "user_db";
        final String USER = "admin";
        final String PASS = "password";

        Class.forName(JDBC_DRIVER);

        return DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
    }
}
