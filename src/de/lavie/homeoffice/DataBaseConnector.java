package de.lavie.homeoffice;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;

public class DataBaseConnector {
    public Connection connectDB()
    {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setServerName("127.0.0.1");
        dataSource.setDatabaseName("lib_management");
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Database cannot be reached.");
        }
        finally
        {
            return conn;
        }
    }
}
