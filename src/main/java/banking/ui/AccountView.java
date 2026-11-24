package banking.ui;

import banking.controller.AccountController;
import banking.model.Account;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

public class AccountView extends VBox {

    private final AccountController controller;

    private final TextField accountNumberField = new TextField();
    private final TextField customerIdField = new TextField();
    private final TextField amountField = new TextField();

    private final ComboBox<String> accountTypeBox =
            new ComboBox<>();

    private final Label messageLabel = new Label();

    public AccountView() {

        controller = new AccountController();

        setSpacing(12);
        setPadding(new Insets(30));

        Label title = new Label("Account Management");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        accountNumberField.setPromptText("Account number");
        customerIdField.setPromptText("Customer ID");
        amountField.setPromptText("Amount (BWP)");

        accountTypeBox.getItems().addAll(
                "Savings",
                "Investment",
                "Cheque"
        );

        accountTypeBox.setPromptText("Select account type");

        Button createButton = new Button("Create Account");
        Button searchButton = new Button("Search Account");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button clearButton = new Button("Clear");

        createButton.setOnAction(e -> createAccount());
        searchButton.setOnAction(e -> searchAccount());
        depositButton.setOnAction(e -> deposit());
        withdrawButton.setOnAction(e -> withdraw());
        clearButton.setOnAction(e -> clearFields());

        getChildren().addAll(
                title,

                new Label("Account Number"),
                accountNumberField,

                new Label("Customer ID"),
                customerIdField,

                new Label("Account Type"),
                accountTypeBox,

                new Label("Amount"),
                amountField,

                createButton,
                searchButton,
                depositButton,
                withdrawButton,
                clearButton,

                messageLabel
        );
    }

    // Creates a new account.
    private void createAccount() {

        try {

            String accountNumber =
                    accountNumberField.getText().trim();

            String customerId =
                    customerIdField.getText().trim();

            String accountType =
                    accountTypeBox.getValue();

            if (accountNumber.isEmpty()
                    || customerId.isEmpty()
                    || accountType == null
                    || amountField.getText().trim().isEmpty()) {

                messageLabel.setText(
                        "Complete all account information.");
                return;
            }

            BigDecimal openingBalance =
                    new BigDecimal(amountField.getText().trim());

            String result = controller.createAccount(
                    accountNumber,
                    customerId,
                    accountType,
                    openingBalance
            );

            messageLabel.setText(result);

        } catch (NumberFormatException e) {

            messageLabel.setText("Enter a valid monetary amount.");

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());
        }
    }

    // Searches for an account.
    private void searchAccount() {

        String accountNumber =
                accountNumberField.getText().trim();

        if (accountNumber.isEmpty()) {
            messageLabel.setText("Enter an account number.");
            return;
        }

        Account account = controller.searchAccount(accountNumber);

        if (account == null) {
            messageLabel.setText("Account not found.");
            return;
        }

        accountNumberField.setText(account.getAccountNumber());
        customerIdField.setText(
                account.getOwner().getCustomerId());

        amountField.setText(account.getBalance().toString());

        if (account instanceof banking.model.SavingsAccount) {
            accountTypeBox.setValue("Savings");

        } else if (account instanceof banking.model.InvestmentAccount) {
            accountTypeBox.setValue("Investment");

        } else if (account instanceof banking.model.ChequeAccount) {
            accountTypeBox.setValue("Cheque");
        }

        messageLabel.setText(
                "Account found. Current balance: BWP "
                        + account.getBalance());
    }

    // Deposits money into an account.
    private void deposit() {

        processAmount(true);
    }

    // Withdraws money from an account.
    private void withdraw() {

        processAmount(false);
    }

    // Processes deposit and withdrawal requests.
    private void processAmount(boolean isDeposit) {

        try {

            String accountNumber =
                    accountNumberField.getText().trim();

            if (accountNumber.isEmpty()) {
                messageLabel.setText("Enter an account number.");
                return;
            }

            BigDecimal amount =
                    new BigDecimal(amountField.getText().trim());

            String result;

            if (isDeposit) {

                result = controller.deposit(accountNumber, amount);

            } else {

                result = controller.withdraw(accountNumber, amount);
            }

            messageLabel.setText(result);

        } catch (NumberFormatException e) {

            messageLabel.setText("Enter a valid monetary amount.");

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());
        }
    }

    // Clears all input fields.
    private void clearFields() {

        accountNumberField.clear();
        customerIdField.clear();
        amountField.clear();

        accountTypeBox.setValue(null);

        messageLabel.setText("");
    }
}