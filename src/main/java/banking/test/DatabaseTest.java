package banking.test;

import banking.database.DatabaseInitializer;

public class DatabaseTest {

    public static void main(String[] args) {

        System.out.println("Testing database connection...");

        // Creates the database and its tables.
        DatabaseInitializer.initialize();

        System.out.println("Database test completed.");
    }
}