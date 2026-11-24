package banking.dao;

import banking.database.DatabaseConnection;
import banking.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    // Saves a new customer in the database.
    public void save(Customer customer) {

        String sql = """
            INSERT INTO customers (
                customer_id, first_name, surname, address,
                phone_number, employed, employer_name, employer_address
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getSurname());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getPhoneNumber());
            statement.setInt(6, customer.isEmployed() ? 1 : 0);
            statement.setString(7, customer.getEmployerName());
            statement.setString(8, customer.getEmployerAddress());

            statement.executeUpdate();

            System.out.println("Customer saved successfully.");

        } catch (SQLException e) {
            System.out.println("Could not save customer: " + e.getMessage());
        }
    }

    // Retrieves a customer using their customer ID.
    public Customer findById(String customerId) {

        String sql = """
            SELECT * FROM customers
            WHERE customer_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Customer(
                        result.getString("customer_id"),
                        result.getString("first_name"),
                        result.getString("surname"),
                        result.getString("address"),
                        result.getString("phone_number"),
                        result.getInt("employed") == 1,
                        result.getString("employer_name"),
                        result.getString("employer_address")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not retrieve customer: "
                    + e.getMessage());
        }

        return null;
    }

    // Updates an existing customer's information.
    public void update(Customer customer) {

        String sql = """
            UPDATE customers SET
                first_name = ?,
                surname = ?,
                address = ?,
                phone_number = ?,
                employed = ?,
                employer_name = ?,
                employer_address = ?
            WHERE customer_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getSurname());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getPhoneNumber());
            statement.setInt(5, customer.isEmployed() ? 1 : 0);
            statement.setString(6, customer.getEmployerName());
            statement.setString(7, customer.getEmployerAddress());
            statement.setString(8, customer.getCustomerId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Customer not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not update customer: "
                    + e.getMessage());
        }
    }

    // Deletes a customer using their customer ID.
    public void delete(String customerId) {

        String sql = """
            DELETE FROM customers
            WHERE customer_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer not found.");
            }

        } catch (SQLException e) {
            System.out.println("Could not delete customer: "
                    + e.getMessage());
        }
    }
}