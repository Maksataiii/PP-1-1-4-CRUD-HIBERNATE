package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД
    private static Connection connection=null;
    private static Util instance = null;

    private Util() {
        try {
            if( connection == null || connection.isClosed()) {
                Properties props = getProps();
                Class.forName(props.getProperty("driver"));
                connection = DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"), props.getProperty("password"));
                System.out.println("Connection Ok");
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("Connection error");
        }
    }
    public static Util getInstance() {
        if (null == instance) {
            instance = new Util();
        }
        return instance;
    }

    public static Connection getConnection() {
        return connection;
    }

    private static Properties getProps() throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(Util.class.getResource("/database.properties").toURI()))) {
            props.load(in);
            return props;
        } catch (IOException | URISyntaxException e) {
            throw new IOException("Database config file not found", e);
        }
    }
}
