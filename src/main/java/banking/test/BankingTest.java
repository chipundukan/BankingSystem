package banking.test;

import banking.model.*;
import banking.service.BankingService;

import java.math.BigDecimal;

public class BankingTest {

    public static void main(String[] args) {

        // Create a customer.
        Customer customer = new Customer(
            "C001",
            "Mpho",
            "Molefe",
            "Gaborone",
            "71234567",
            true,
            "ABC Company",
            "Gaborone"
        );

        // Create the banking service.
        BankingService bank = new BankingService();

        // Create a savings account.
        SavingsAccount savings = new SavingsAccount(
            "S001", customer, new BigDecimal("1000.00")
        );

        // Create an investment account.
        InvestmentAccount investment = new InvestmentAccount(
            "I001", customer, new BigDecimal("2000.00")
        );

        // Create a cheque account.
        ChequeAccount cheque = new ChequeAccount(
            "CH001", customer, new BigDecimal("1500.00")
        );

        // Register the accounts.
        bank.registerAccount(savings);
        bank.registerAccount(investment);
        bank.registerAccount(cheque);

        // Test deposits.
        bank.deposit("S001", new BigDecimal("500.00"));

        // Test withdrawals.
        bank.withdraw("I001", new BigDecimal("200.00"));
        bank.withdraw("CH001", new BigDecimal("100.00"));

        // Process monthly interest.
        bank.processMonthlyInterest();

        // Display account balances.
        System.out.println("BANKING SYSTEM TEST RESULTS");

        for (var account : bank.getAccounts()) {

            System.out.println(
                account.getAccountNumber() + " | Balance: BWP "
                + account.getBalance()
            );

            System.out.println(
                "Transactions: " + account.getTransactions().size()
            );
        }
    }
}