package banking.model;

import java.math.BigDecimal;

public class ChequeAccount extends Account implements Withdrawable {

    // Creates a cheque account for an eligible customer.
    public ChequeAccount(String accountNumber, Customer owner,
                         BigDecimal openingBalance) {

        super(accountNumber, validateCustomer(owner), openingBalance);
    }

    // Checks employment and employer information.
    private static Customer validateCustomer(Customer owner) {

        if (owner == null || !owner.isEmployed()) {
            throw new IllegalArgumentException(
                "Cheque accounts require an employed customer."
            );
        }

        if (owner.getEmployerName() == null ||
            owner.getEmployerName().isBlank() ||
            owner.getEmployerAddress() == null ||
            owner.getEmployerAddress().isBlank()) {

            throw new IllegalArgumentException(
                "Employer name and address are required."
            );
        }

        return owner;
    }

    // Processes withdrawals from the cheque account.
    @Override
    public void withdraw(BigDecimal amount) {

        validateAmount(amount);

        if (amount.compareTo(getBalance()) > 0) {
            throw new IllegalArgumentException(
                "Insufficient account balance."
            );
        }

        updateBalance(getBalance().subtract(amount));

        recordTransaction(TransactionType.WITHDRAWAL, amount);
    }
}