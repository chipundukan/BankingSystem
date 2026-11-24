package banking.controller;

import banking.dao.AccountDAO;
import banking.dao.TransactionDAO;
import banking.model.Account;
import banking.model.Transaction;

import java.util.List;

public class TransactionController {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransactionController() {

        accountDAO = new AccountDAO();
        transactionDAO = new TransactionDAO();
    }

    // Retrieves transactions belonging to an account.
    public List<Transaction> getTransactions(String accountNumber) {

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Enter a valid account number.");
        }

        Account account = accountDAO.findById(accountNumber);

        if (account == null) {
            throw new IllegalArgumentException(
                    "Account not found.");
        }

        return transactionDAO.findByAccountNumber(accountNumber);
    }
}
