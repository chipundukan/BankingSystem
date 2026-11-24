package banking.controller;

import banking.dao.AccountDAO;
import banking.dao.CustomerDAO;
import banking.dao.TransactionDAO;

import banking.model.Account;
import banking.model.Customer;
import banking.model.SavingsAccount;
import banking.model.InvestmentAccount;
import banking.model.ChequeAccount;
import banking.model.Withdrawable;
import banking.model.Transaction;

import java.math.BigDecimal;

public class AccountController {

    private final AccountDAO accountDAO;
    private final CustomerDAO customerDAO;
    private final TransactionDAO transactionDAO;

    public AccountController() {

        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
        transactionDAO = new TransactionDAO();
    }

    // Creates a new bank account.
    public String createAccount(String accountNumber,
                                String customerId,
                                String accountType,
                                BigDecimal openingBalance) {

        try {

            Customer customer = customerDAO.findById(customerId);

            if (customer == null) {
                return "Customer not found.";
            }

            if (accountDAO.findById(accountNumber) != null) {
                return "Account number already exists.";
            }

            Account account;

            switch (accountType) {

                case "Savings":
                    account = new SavingsAccount(
                            accountNumber, customer, openingBalance);
                    break;

                case "Investment":
                    account = new InvestmentAccount(
                            accountNumber, customer, openingBalance);
                    break;

                case "Cheque":
                    account = new ChequeAccount(
                            accountNumber, customer, openingBalance);
                    break;

                default:
                    return "Invalid account type.";
            }

            accountDAO.save(account);

            if (accountDAO.findById(accountNumber) != null) {
                return "Account created successfully.";
            }

            return "Account creation failed.";

        } catch (IllegalArgumentException e) {

            return e.getMessage();
        }
    }

    // Searches for an account.
    public Account searchAccount(String accountNumber) {

        return accountDAO.findById(accountNumber);
    }

    // Deposits money into an account.
    public String deposit(String accountNumber, BigDecimal amount) {

        try {

            Account account = accountDAO.findById(accountNumber);

            if (account == null) {
                return "Account not found.";
            }

            account.deposit(amount);

            accountDAO.update(account);

            Transaction transaction = account.getTransactions()
                    .get(account.getTransactions().size() - 1);

            transactionDAO.save(transaction, account);

            return "Deposit completed successfully. New balance: BWP "
                    + account.getBalance();

        } catch (IllegalArgumentException e) {

            return e.getMessage();
        }
    }

    // Withdraws money from an eligible account.
    public String withdraw(String accountNumber, BigDecimal amount) {

        try {

            Account account = accountDAO.findById(accountNumber);

            if (account == null) {
                return "Account not found.";
            }

            if (!(account instanceof Withdrawable)) {
                return "Withdrawals are not allowed on Savings Accounts.";
            }

            ((Withdrawable) account).withdraw(amount);

            accountDAO.update(account);

            Transaction transaction = account.getTransactions()
                    .get(account.getTransactions().size() - 1);

            transactionDAO.save(transaction, account);

            return "Withdrawal completed successfully. New balance: BWP "
                    + account.getBalance();

        } catch (IllegalArgumentException e) {

            return e.getMessage();
        }
    }
}