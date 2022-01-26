package LANchat;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {
    public Connection dbLink;

    public Connection getConnection () {
        String dbName = "sertsevcom_LANchat";
        String dbUser = "***********";
        String dbPassword = "**********";
        String url = "jdbc:mysql://sertsev.com:3306/" + dbName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbLink;
    }
}
