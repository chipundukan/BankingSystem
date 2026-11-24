package banking.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SavingsAccount extends Account implements InterestBearing {

    // Monthly savings interest rate: 0.05%.
    private static final BigDecimal INTEREST_RATE =
            new BigDecimal("0.0005");

    // Creates a new savings account.
    public SavingsAccount(String accountNumber, Customer owner,
                          BigDecimal openingBalance) {

        super(accountNumber, owner, openingBalance);
    }

    // Calculates the monthly interest based on the current balance.
    @Override
    public BigDecimal calculateMonthlyInterest() {

        return getBalance()
                .multiply(INTEREST_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Adds the calculated interest to the savings account.
    public void applyMonthlyInterest() {

        BigDecimal interest = calculateMonthlyInterest();

        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            updateBalance(getBalance().add(interest));

            recordTransaction(TransactionType.INTEREST, interest);
        }
    }
}