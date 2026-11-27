package banking.ui;

import banking.controller.AccountController;
import banking.model.Account;
import banking.model.SavingsAccount;
import banking.model.InvestmentAccount;
import banking.model.ChequeAccount;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.math.BigDecimal;

public class AccountView extends VBox {

    private final AccountController controller;

    private final TextField accountNumberField = new TextField();
    private final TextField customerIdField = new TextField();
    private final TextField openingBalanceField = new TextField();
    private final TextField transactionAmountField = new TextField();

    private final ComboBox<String> accountTypeBox =
            new ComboBox<>();

    private final Label balanceLabel =
            new Label("BWP 0.00");

    private final Label messageLabel = new Label();

    public AccountView() {

        controller = new AccountController();

        setPadding(new Insets(30));
        setSpacing(20);
        getStyleClass().add("content-area");

        // Page heading.
        Label title = new Label("Account Management");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Create accounts and manage customer funds."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox heading = new VBox(6);
        heading.getChildren().addAll(title, subtitle);

        // Account information card.
        VBox accountCard = new VBox(20);
        accountCard.getStyleClass().add("card");

        Label accountTitle = new Label("Account Information");
        accountTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        Label accountDescription = new Label(
                "Enter the account details to create or search for an account."
        );
        accountDescription.getStyleClass().add("page-subtitle");

        accountNumberField.setPromptText("e.g. ACC001");
        customerIdField.setPromptText("Enter customer ID");
        openingBalanceField.setPromptText("Opening balance (BWP)");

        accountTypeBox.getItems().addAll(
                "Savings",
                "Investment",
                "Cheque"
        );

        accountTypeBox.setPromptText("Select account type");
        accountTypeBox.setMaxWidth(Double.MAX_VALUE);

        GridPane accountGrid = new GridPane();
        accountGrid.setHgap(25);
        accountGrid.setVgap(18);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50);

        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50);

        accountGrid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn
        );

        addField(
                accountGrid,
                "Account Number",
                accountNumberField,
                0,
                0
        );

        addField(
                accountGrid,
                "Customer ID",
                customerIdField,
                1,
                0
        );

        addField(
                accountGrid,
                "Account Type",
                accountTypeBox,
                0,
                1
        );

        addField(
                accountGrid,
                "Opening Balance (BWP)",
                openingBalanceField,
                1,
                1
        );

        // Account action buttons.
        Button createButton = new Button("Create Account");
        Button searchButton = new Button("Search Account");

        createButton.setOnAction(e -> createAccount());
        searchButton.setOnAction(e -> searchAccount());

        HBox accountButtons = new HBox(12);
        accountButtons.setAlignment(Pos.CENTER_LEFT);

        accountButtons.getChildren().addAll(
                createButton,
                searchButton
        );

        accountCard.getChildren().addAll(
                accountTitle,
                accountDescription,
                new Separator(),
                accountGrid,
                accountButtons
        );

        // Balance display card.
        VBox balanceCard = new VBox(10);
        balanceCard.getStyleClass().add("card");

        Label balanceTitle = new Label("CURRENT ACCOUNT BALANCE");
        balanceTitle.getStyleClass().add("card-title");

        balanceLabel.setStyle(
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #2563eb;"
        );

        balanceCard.getChildren().addAll(
                balanceTitle,
                balanceLabel
        );

        // Deposit and withdrawal card.
        VBox transactionCard = new VBox(18);
        transactionCard.getStyleClass().add("card");

        Label transactionTitle = new Label("Account Transactions");
        transactionTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        Label transactionDescription = new Label(
                "Enter an amount to deposit or withdraw."
        );
        transactionDescription.getStyleClass().add("page-subtitle");

        transactionAmountField.setPromptText(
                "Transaction amount (BWP)"
        );

        transactionAmountField.setMaxWidth(350);

        Button depositButton = new Button("Deposit Funds");
        Button withdrawButton = new Button("Withdraw Funds");
        Button clearButton = new Button("Clear Form");

        depositButton.setOnAction(e -> deposit());
        withdrawButton.setOnAction(e -> withdraw());
        clearButton.setOnAction(e -> clearFields());

        HBox transactionButtons = new HBox(12);
        transactionButtons.setAlignment(Pos.CENTER_LEFT);

        transactionButtons.getChildren().addAll(
                depositButton,
                withdrawButton,
                clearButton
        );

        messageLabel.getStyleClass().add("message-label");
        messageLabel.setWrapText(true);

        transactionCard.getChildren().addAll(
                transactionTitle,
                transactionDescription,
                transactionAmountField,
                transactionButtons,
                messageLabel
        );

        // Scrollable page.
        VBox page = new VBox(22);

        page.getChildren().addAll(
                heading,
                accountCard,
                balanceCard,
                transactionCard
        );

        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().add(scrollPane);
    }

    // Adds a labelled input field to the account form.
    private void addField(
            GridPane grid,
            String labelText,
            Control field,
            int column,
            int row
    ) {

        Label label = new Label(labelText);

        label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #475569;"
        );

        VBox fieldBox = new VBox(8);
        fieldBox.getChildren().addAll(label, field);

        GridPane.setHgrow(fieldBox, Priority.ALWAYS);

        fieldBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(fieldBox, column, row);
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

            String openingBalanceText =
                    openingBalanceField.getText().trim();

            if (accountNumber.isEmpty()
                    || customerId.isEmpty()
                    || accountType == null
                    || openingBalanceText.isEmpty()) {

                messageLabel.setText(
                        "Complete all account information."
                );
                return;
            }

            BigDecimal openingBalance =
                    new BigDecimal(openingBalanceText);

            String result = controller.createAccount(
                    accountNumber,
                    customerId,
                    accountType,
                    openingBalance
            );

            messageLabel.setText(result);

            // Refresh the displayed balance after successful creation.
            if (controller.searchAccount(accountNumber) != null) {
                searchAccount();
            }

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Enter a valid opening balance."
            );

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
            balanceLabel.setText("BWP 0.00");
            return;
        }

        accountNumberField.setText(account.getAccountNumber());

        customerIdField.setText(
                account.getOwner().getCustomerId()
        );

        openingBalanceField.clear();

        if (account instanceof SavingsAccount) {

            accountTypeBox.setValue("Savings");

        } else if (account instanceof InvestmentAccount) {

            accountTypeBox.setValue("Investment");

        } else if (account instanceof ChequeAccount) {

            accountTypeBox.setValue("Cheque");
        }

        balanceLabel.setText(
                "BWP " + account.getBalance().toPlainString()
        );

        messageLabel.setText(
                "Account found successfully."
        );
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

            String amountText =
                    transactionAmountField.getText().trim();

            if (accountNumber.isEmpty()) {

                messageLabel.setText(
                        "Enter an account number first."
                );
                return;
            }

            if (amountText.isEmpty()) {

                messageLabel.setText(
                        "Enter a transaction amount."
                );
                return;
            }

            BigDecimal amount = new BigDecimal(amountText);

            String result;

            if (isDeposit) {

                result = controller.deposit(accountNumber, amount);

            } else {

                result = controller.withdraw(accountNumber, amount);
            }

            messageLabel.setText(result);

            // Refresh the balance after the transaction.
            if (controller.searchAccount(accountNumber) != null) {
                Account account =
                        controller.searchAccount(accountNumber);

                balanceLabel.setText(
                        "BWP " + account.getBalance().toPlainString()
                );
            }

            transactionAmountField.clear();

        } catch (NumberFormatException e) {

            messageLabel.setText(
                    "Enter a valid monetary amount."
            );

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());
        }
    }

    // Clears all input fields.
    private void clearFields() {

        accountNumberField.clear();
        customerIdField.clear();
        openingBalanceField.clear();
        transactionAmountField.clear();

        accountTypeBox.setValue(null);

        balanceLabel.setText("BWP 0.00");

        messageLabel.setText("");
    }
}