package banking.dao;

import banking.database.DatabaseConnection;
import banking.model.Account;
import banking.model.Customer;
import banking.model.SavingsAccount;
import banking.model.InvestmentAccount;
import banking.model.ChequeAccount;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    private final CustomerDAO customerDAO = new CustomerDAO();

    // Identifies the account type before saving it.
    private String getAccountType(Account account) {

        if (account instanceof SavingsAccount) {
            return "Savings";
        }

        if (account instanceof InvestmentAccount) {
            return "Investment";
        }

        if (account instanceof ChequeAccount) {
            return "Cheque";
        }

        throw new IllegalArgumentException("Unknown account type.");
    }

    // CREATE: Saves a new account.
    public void save(Account account) {

        String sql = """
                INSERT INTO accounts
                (account_number, customer_id, account_type, balance)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountNumber());
            statement.setString(2, account.getOwner().getCustomerId());
            statement.setString(3, getAccountType(account));
            statement.setString(4, account.getBalance().toString());

            statement.executeUpdate();

            System.out.println("Account saved successfully.");

        } catch (SQLException e) {
            System.out.println("Could not save account: " + e.getMessage());
        }
    }

    // READ: Retrieves an account using its account number.
    public Account findById(String accountNumber) {

        String sql = """
                SELECT account_number, customer_id, account_type, balance
                FROM accounts
                WHERE account_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    String number = result.getString("account_number");
                    String customerId = result.getString("customer_id");
                    String type = result.getString("account_type");

                    BigDecimal balance = new BigDecimal(
                            result.getString("balance"));

                    // Retrieves the account owner.
                    Customer customer = customerDAO.findById(customerId);

                    if (customer == null) {
                        System.out.println("Account owner not found.");
                        return null;
                    }

                    // Reconstructs the correct account type.
                    switch (type) {

                        case "Savings":
                            return new SavingsAccount(
                                    number, customer, balance);

                        case "Investment":
                            return InvestmentAccount.restore(
                                    number, customer, balance);

                        case "Cheque":
                            return new ChequeAccount(
                                    number, customer, balance);

                        default:
                            System.out.println("Unknown account type.");
                            return null;
                    }
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Could not retrieve account: "
                    + e.getMessage());
        }

        return null;
    }

    // UPDATE: Changes an existing account's information.
    public void update(Account account) {

        String sql = """
                UPDATE accounts
                SET customer_id = ?, account_type = ?, balance = ?
                WHERE account_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, account.getOwner().getCustomerId());
            statement.setString(2, getAccountType(account));
            statement.setString(3, account.getBalance().toString());
            statement.setString(4, account.getAccountNumber());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Account updated successfully.");
            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Could not update account: "
                    + e.getMessage());
        }
    }

    // DELETE: Removes an account using its account number.
    public void delete(String accountNumber) {

        String sql = """
                DELETE FROM accounts
                WHERE account_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not delete account: "
                    + e.getMessage());
        }
    }
}