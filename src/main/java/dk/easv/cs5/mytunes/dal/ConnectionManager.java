package dk.easv.cs5.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionManager {
    private static final String PROP_FILE = "config/config.settings";
    private static SQLServerDataSource dataSource = new SQLServerDataSource();

    static {
        try {Properties databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream(new File(PROP_FILE)));

        dataSource = new SQLServerDataSource();
        dataSource.setServerName(databaseProperties.getProperty("Server"));
        dataSource.setDatabaseName(databaseProperties.getProperty("Database"));
        dataSource.setUser(databaseProperties.getProperty("User"));
        dataSource.setPassword(databaseProperties.getProperty("Password"));
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }   catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Failed to load database settings", e);}
    }

    public static Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) throws Exception {
        ConnectionManager conn = new ConnectionManager();
        try (Connection connection = conn.getConnection()) {
            System.out.println("Is it open? " + !connection.isClosed());
        }
    }
}