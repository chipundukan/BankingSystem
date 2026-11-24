package banking.service;

import banking.model.Account;
import banking.model.Withdrawable;
import banking.model.InterestBearing;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankingService {

    // Stores accounts managed by the banking system.
    private List<Account> accounts;

    // Creates an empty account collection.
    public BankingService() {
        accounts = new ArrayList<>();
    }

    // Registers a new account with the banking system.
    public void registerAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null."
            );
        }

        for (Account existing : accounts) {
            if (existing.getAccountNumber()
                    .equals(account.getAccountNumber())) {

                throw new IllegalArgumentException(
                    "Account number already exists."
                );
            }
        }

        accounts.add(account);
    }

    // Finds an account using its account number.
    public Account findAccount(String accountNumber) {

        for (Account account : accounts) {

            if (account.getAccountNumber()
                    .equals(accountNumber)) {
                return account;
            }
        }

        throw new IllegalArgumentException(
            "Account not found."
        );
    }

    // Processes a deposit.
    public void deposit(String accountNumber, BigDecimal amount) {

        Account account = findAccount(accountNumber);

        account.deposit(amount);
    }

    // Processes withdrawals only for eligible accounts.
    public void withdraw(String accountNumber, BigDecimal amount) {

        Account account = findAccount(accountNumber);

        if (!(account instanceof Withdrawable)) {
            throw new IllegalArgumentException(
                "This account does not allow withdrawals."
            );
        }

        Withdrawable withdrawable = (Withdrawable) account;

        withdrawable.withdraw(amount);
    }

   // Processes monthly interest for eligible accounts.
public void processMonthlyInterest() {

    for (Account account : accounts) {

        if (account instanceof InterestBearing) {

            if (account instanceof banking.model.SavingsAccount) {

                banking.model.SavingsAccount savings =
                    (banking.model.SavingsAccount) account;

                savings.applyMonthlyInterest();

            } else if (account instanceof banking.model.InvestmentAccount) {

                banking.model.InvestmentAccount investment =
                    (banking.model.InvestmentAccount) account;

                investment.applyMonthlyInterest();
            }
        }
    }
}

    // Returns all registered accounts.
    public List<Account> getAccounts() {
        return List.copyOf(accounts);
    }
}