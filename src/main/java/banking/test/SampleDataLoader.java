package banking.test;

import banking.database.DatabaseInitializer;
import banking.dao.CustomerDAO;
import banking.dao.AccountDAO;
import banking.dao.TransactionDAO;

import banking.model.Customer;
import banking.model.Account;
import banking.model.SavingsAccount;
import banking.model.InvestmentAccount;
import banking.model.ChequeAccount;
import banking.model.Transaction;

import java.math.BigDecimal;

public class SampleDataLoader {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();

        System.out.println("\n===== LOADING SAMPLE DATA =====");

        for (int i = 1; i <= 10; i++) {

            String customerId = String.format("SAMPLE-C%03d", i);
            String accountNumber = String.format("SAMPLE-A%03d", i);

            // Create a customer.
            Customer customer = new Customer(
                    customerId,
                    "Customer" + i,
                    "Sample",
                    "Gaborone",
                    "7100000" + i,
                    true,
                    "Company" + i,
                    "Gaborone"
            );

            customerDAO.save(customer);

            // Create different account types.
            Account account;

            BigDecimal openingBalance;

            if (i % 3 == 1) {

                openingBalance = new BigDecimal("1000.00");

                account = new SavingsAccount(
                        accountNumber,
                        customer,
                        openingBalance
                );

            } else if (i % 3 == 2) {

                openingBalance = new BigDecimal("1500.00");

                account = new InvestmentAccount(
                        accountNumber,
                        customer,
                        openingBalance
                );

            } else {

                openingBalance = new BigDecimal("2000.00");

                account = new ChequeAccount(
                        accountNumber,
                        customer,
                        openingBalance
                );
            }

            // Make a deposit and record the transaction.
            account.deposit(new BigDecimal("100.00"));

            // Save the account and its transaction.
            accountDAO.save(account);

            for (Transaction transaction : account.getTransactions()) {
                transactionDAO.save(transaction, account);
            }

            System.out.println(
                    "Sample record " + i + " created successfully."
            );
        }

        System.out.println("\n===== SAMPLE DATA LOADING COMPLETED =====");
    }
}