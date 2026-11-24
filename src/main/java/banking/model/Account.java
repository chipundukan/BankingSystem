package banking.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class Account {

    // Common details shared by all account types.
    private String accountNumber;
    private BigDecimal balance;
    private Customer owner;

    // Stores the account's transaction history.
    private List<Transaction> transactions;

    // Creates a new account.
    public Account(String accountNumber, Customer owner,
                   BigDecimal openingBalance) {

        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required.");
        }

        if (owner == null) {
            throw new IllegalArgumentException("Account owner is required.");
        }

        if (openingBalance == null ||
            openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid opening balance.");
        }

        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = openingBalance;
        this.transactions = new ArrayList<>();

        // Connects the new account to its customer.
        owner.addAccount(this);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    // Adds money and records the transaction.
    public void deposit(BigDecimal amount) {

        validateAmount(amount);

        balance = balance.add(amount);

        recordTransaction(TransactionType.DEPOSIT, amount);
    }

    // Updates the balance when a withdrawal or interest payment occurs.
    protected void updateBalance(BigDecimal newBalance) {

        if (newBalance == null ||
            newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid account balance.");
        }

        balance = newBalance;
    }

    // Checks that a transaction amount is valid.
    protected void validateAmount(BigDecimal amount) {

        if (amount == null ||
            amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero."
            );
        }
    }

    // Creates and stores a transaction record.
    protected void recordTransaction(
            TransactionType type, BigDecimal amount) {

        String transactionId = UUID.randomUUID().toString();

        Transaction transaction =
            new Transaction(transactionId, type, amount);

        transactions.add(transaction);
    }

    // Returns the transaction history without exposing the original list.
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }
}