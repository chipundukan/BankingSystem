package banking.test;

import banking.database.DatabaseInitializer;
import banking.dao.CustomerDAO;
import banking.dao.AccountDAO;

import banking.model.Customer;
import banking.model.InvestmentAccount;

import java.math.BigDecimal;

public class InvestmentRestoreTest {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();

        // Create a customer.
        Customer customer = new Customer(
                "INV001",
                "Mpho",
                "Molefe",
                "Gaborone",
                "71234567",
                true,
                "ABC Company",
                "Gaborone"
        );

        customerDAO.save(customer);

        // Open an investment account with BWP 600.
        InvestmentAccount account = new InvestmentAccount(
                "INV-A001",
                customer,
                new BigDecimal("600.00")
        );

        // Withdraw BWP 200, leaving BWP 400.
        account.withdraw(new BigDecimal("200.00"));

        // Save the customer and account.
        accountDAO.save(account);

        // Retrieve the account from the database.
        InvestmentAccount retrievedAccount =
                (InvestmentAccount) accountDAO.findById("INV-A001");

        if (retrievedAccount != null) {

            System.out.println("Investment account retrieved.");
            System.out.println("Account number: "
                    + retrievedAccount.getAccountNumber());
            System.out.println("Current balance: BWP "
                    + retrievedAccount.getBalance());

        } else {
            System.out.println("Investment account retrieval failed.");
        }
    }
}