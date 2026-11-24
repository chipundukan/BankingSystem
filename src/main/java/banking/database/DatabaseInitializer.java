package banking.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    // Creates the database tables if they do not already exist.
    public static void initialize() {

        String customerTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id TEXT PRIMARY KEY,
                first_name TEXT NOT NULL,
                surname TEXT NOT NULL,
                address TEXT,
                phone_number TEXT,
                employed INTEGER NOT NULL,
                employer_name TEXT,
                employer_address TEXT
            )
            """;

        String accountTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number TEXT PRIMARY KEY,
                customer_id TEXT NOT NULL,
                account_type TEXT NOT NULL,
                balance TEXT NOT NULL,
                FOREIGN KEY (customer_id)
                    REFERENCES customers(customer_id)
            )
            """;

        String transactionTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id TEXT PRIMARY KEY,
                account_number TEXT NOT NULL,
                transaction_type TEXT NOT NULL,
                amount TEXT NOT NULL,
                transaction_date TEXT NOT NULL,
                FOREIGN KEY (account_number)
                    REFERENCES accounts(account_number)
            )
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(customerTable);
            statement.execute(accountTable);
            statement.execute(transactionTable);

            System.out.println("Database tables created successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization failed: "
                    + e.getMessage());
        }
    }
}