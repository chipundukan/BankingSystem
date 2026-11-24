package banking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // Location of our SQLite database file.
    private static final String URL = "jdbc:sqlite:banking.db";

    // Opens a connection and enables foreign-key protection.
    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }
}