package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static Connection getJDBCConnection() {
        final String url = "jdbc:mysql://localhost:3306/post1";
        final String user = "root";
        final String password = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("thanh cong");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("loi");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("loi");
            e.printStackTrace();
        }
        return null;
    }
}
