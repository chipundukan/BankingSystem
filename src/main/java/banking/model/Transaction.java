
package banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    // Information describing a banking transaction.
    private String transactionId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    // Creates a new transaction record.
    public Transaction(String transactionId, TransactionType transactionType,
                       BigDecimal amount) {

        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Transaction ID is required.");
        }

        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type is required.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;

        // Records the date and time when the transaction is created.
        this.transactionDate = LocalDateTime.now();
    }

    // Reconstructs a transaction using its original database date.
    public Transaction(String transactionId, TransactionType transactionType,
                       BigDecimal amount, LocalDateTime transactionDate) {

        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Transaction ID is required.");
        }

        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type is required.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date is required.");
        }

        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    // Returns the transaction ID.
    public String getTransactionId() {
        return transactionId;
    }

    // Returns the transaction type.
    public TransactionType getTransactionType() {
        return transactionType;
    }

    // Returns the transaction amount.
    public BigDecimal getAmount() {
        return amount;
    }

    // Returns the transaction date and time.
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
}
