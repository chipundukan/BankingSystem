package banking.dao;

import banking.database.DatabaseConnection;
import banking.model.Transaction;
import banking.model.Account;
import banking.model.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Saves a transaction linked to its bank account.
    public void save(Transaction transaction, Account account) {

        String sql = """
            INSERT INTO transactions (
                transaction_id, account_number, transaction_type,
                amount, transaction_date
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getTransactionId());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, transaction.getTransactionType().name());
            statement.setString(4, transaction.getAmount().toPlainString());
            statement.setString(5, transaction.getTransactionDate().toString());

            statement.executeUpdate();

            System.out.println("Transaction saved successfully.");

        } catch (SQLException e) {
            System.out.println("Could not save transaction: "
                    + e.getMessage());
        }
    }

    // Retrieves all transactions for a particular account.
    public List<Transaction> findByAccountNumber(String accountNumber) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
            SELECT * FROM transactions
            WHERE account_number = ?
            ORDER BY transaction_date
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Transaction transaction = new Transaction(
                        result.getString("transaction_id"),

                        TransactionType.valueOf(
                            result.getString("transaction_type")),

                        new BigDecimal(
                            result.getString("amount")),

                        LocalDateTime.parse(
                            result.getString("transaction_date"))
                    );

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve transactions: "
                    + e.getMessage());
        }

        return transactions;
    }

    // Updates an existing transaction.
    public void update(Transaction transaction) {

        String sql = """
            UPDATE transactions SET
                transaction_type = ?,
                amount = ?,
                transaction_date = ?
            WHERE transaction_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getTransactionType().name());
            statement.setString(2, transaction.getAmount().toPlainString());
            statement.setString(3, transaction.getTransactionDate().toString());
            statement.setString(4, transaction.getTransactionId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Transaction updated successfully.");
            } else {
                System.out.println("Transaction not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not update transaction: "
                    + e.getMessage());
        }
    }

    // Deletes a transaction using its transaction ID.
    public void delete(String transactionId) {

        String sql = """
            DELETE FROM transactions
            WHERE transaction_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transactionId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("Transaction not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not delete transaction: "
                    + e.getMessage());
        }
    }
}