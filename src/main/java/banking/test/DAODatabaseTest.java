package banking.test;

import banking.database.DatabaseInitializer;
import banking.dao.CustomerDAO;
import banking.dao.AccountDAO;
import banking.dao.TransactionDAO;

import banking.model.Customer;
import banking.model.SavingsAccount;
import banking.model.Transaction;
import banking.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DAODatabaseTest {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        System.out.println("\n===== CRUD INTEGRATION TEST =====");

        // CREATE CUSTOMER
        Customer customer = new Customer(
                "CRUD001",
                "Mpho",
                "Molefe",
                "Gaborone",
                "71234567",
                true,
                "ABC Company",
                "Gaborone"
        );

        customerDAO.save(customer);

        // READ CUSTOMER
        Customer retrievedCustomer =
                customerDAO.findById("CRUD001");

        if (retrievedCustomer != null) {
            System.out.println("Customer retrieved: "
                    + retrievedCustomer.getFullName());
        }

        // UPDATE CUSTOMER
        Customer updatedCustomer = new Customer(
                "CRUD001",
                "Mpho",
                "Molefe",
                "Francistown",
                "71234567",
                true,
                "ABC Company",
                "Francistown"
        );

        customerDAO.update(updatedCustomer);

        // CREATE ACCOUNT
        SavingsAccount account = new SavingsAccount(
                "CRUD-S001",
                customer,
                new BigDecimal("1000.00")
        );

        accountDAO.save(account);

        // READ ACCOUNT
        SavingsAccount retrievedAccount =
                (SavingsAccount) accountDAO.findById("CRUD-S001");

        if (retrievedAccount != null) {
            System.out.println("Account retrieved: "
                    + retrievedAccount.getAccountNumber());
        }

        // UPDATE ACCOUNT
        SavingsAccount updatedAccount = new SavingsAccount(
                "CRUD-S001",
                customer,
                new BigDecimal("1250.00")
        );

        accountDAO.update(updatedAccount);

        // CREATE TRANSACTION
        Transaction transaction = new Transaction(
                "CRUD-T001",
                TransactionType.DEPOSIT,
                new BigDecimal("100.00")
        );

        transactionDAO.save(transaction, account);

        // READ TRANSACTION
        List<Transaction> transactions =
                transactionDAO.findByAccountNumber("CRUD-S001");

        System.out.println("Transactions retrieved: "
                + transactions.size());

        // UPDATE TRANSACTION
        Transaction updatedTransaction = new Transaction(
                "CRUD-T001",
                TransactionType.DEPOSIT,
                new BigDecimal("150.00"),
                LocalDateTime.now()
        );

        transactionDAO.update(updatedTransaction);

        // DELETE TRANSACTION
        transactionDAO.delete("CRUD-T001");

        // DELETE ACCOUNT
        accountDAO.delete("CRUD-S001");

        // DELETE CUSTOMER
        customerDAO.delete("CRUD001");

        System.out.println("\n===== CRUD TEST COMPLETED =====");
    }
}