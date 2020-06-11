package backend.web.database.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PropertiesConnection {

    public static Connection getConnection(String url , String userName, String userPassword ) throws SQLException {
        Properties properties = new Properties();
        properties.put("user", userName);
        properties.put("password", userPassword);
        properties.put("autoReconnect", "true");
        properties.put("characterEncoding", "UTF-8");
        properties.put("useUnicode", "true");
        return DriverManager.getConnection(url, properties);
    }
}