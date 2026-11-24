package banking.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvestmentAccount extends Account
        implements Withdrawable, InterestBearing {

    private static final BigDecimal MINIMUM_OPENING_DEPOSIT =
            new BigDecimal("500.00");

    private static final BigDecimal INTEREST_RATE =
            new BigDecimal("0.05");

    // Normal account creation must meet the minimum deposit.
    public InvestmentAccount(String accountNumber, Customer owner,
                             BigDecimal openingBalance) {

        this(accountNumber, owner, openingBalance, true);
    }

    // Internal constructor allows database restoration.
    private InvestmentAccount(String accountNumber, Customer owner,
                              BigDecimal balance, boolean newAccount) {

        super(accountNumber, owner,
                newAccount ? validateOpeningBalance(balance)
                           : validateStoredBalance(balance));
    }

    // Creates an account object using its existing database balance.
    public static InvestmentAccount restore(String accountNumber,
                                             Customer owner,
                                             BigDecimal balance) {

        return new InvestmentAccount(
                accountNumber, owner, balance, false);
    }

    // Checks the minimum deposit when opening a new account.
    private static BigDecimal validateOpeningBalance(BigDecimal balance) {

        if (balance == null || balance.compareTo(
                MINIMUM_OPENING_DEPOSIT) < 0) {

            throw new IllegalArgumentException(
                    "Investment account requires a minimum opening deposit of BWP 500.");
        }

        return balance;
    }

    // Checks that a stored balance is valid.
    private static BigDecimal validateStoredBalance(BigDecimal balance) {

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid account balance.");
        }

        return balance;
    }

    // Allows withdrawals from investment accounts.
    @Override
    public void withdraw(BigDecimal amount) {

        validateAmount(amount);

        if (amount.compareTo(getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        updateBalance(getBalance().subtract(amount));
        recordTransaction(TransactionType.WITHDRAWAL, amount);
    }

    // Calculates monthly interest at 5%.
    @Override
    public BigDecimal calculateMonthlyInterest() {

        return getBalance()
                .multiply(INTEREST_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Adds monthly interest to the account.
    public void applyMonthlyInterest() {

        BigDecimal interest = calculateMonthlyInterest();

        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            updateBalance(getBalance().add(interest));

            recordTransaction(TransactionType.INTEREST, interest);
        }
    }
}