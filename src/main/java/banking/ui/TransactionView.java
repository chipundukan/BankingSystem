package banking.ui;

import banking.controller.TransactionController;
import banking.model.Transaction;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

public class TransactionView extends VBox {

    private final TransactionController controller;

    private final TextField accountNumberField = new TextField();

    private final Label messageLabel = new Label();

    private final TableView<Transaction> transactionTable =
            new TableView<>();

    public TransactionView() {

        controller = new TransactionController();

        setSpacing(15);
        setPadding(new Insets(30));

        Label title = new Label("Transaction History");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        accountNumberField.setPromptText("Enter account number");

        Button searchButton = new Button("Search Transactions");

        searchButton.setOnAction(e -> searchTransactions());

        // Transaction ID column.
        TableColumn<Transaction, String> idColumn =
                new TableColumn<>("Transaction ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionId"));

        // Transaction type column.
        TableColumn<Transaction, String> typeColumn =
                new TableColumn<>("Type");

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionType"));

        // Transaction amount column.
        TableColumn<Transaction, String> amountColumn =
                new TableColumn<>("Amount (BWP)");

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount"));

        // Transaction date column.
        TableColumn<Transaction, String> dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionDate"));

        transactionTable.getColumns().addAll(
                idColumn,
                typeColumn,
                amountColumn,
                dateColumn
        );

        transactionTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        getChildren().addAll(
                title,
                accountNumberField,
                searchButton,
                messageLabel,
                transactionTable
        );

        VBox.setVgrow(transactionTable,
                javafx.scene.layout.Priority.ALWAYS);
    }

    // Searches for transactions using the account number.
    private void searchTransactions() {

        try {

            String accountNumber =
                    accountNumberField.getText().trim();

            List<Transaction> transactions =
                    controller.getTransactions(accountNumber);

            transactionTable.setItems(
                    FXCollections.observableArrayList(transactions)
            );

            messageLabel.setText(
                    transactions.size() +
                    " transaction(s) found."
            );

        } catch (IllegalArgumentException e) {

            transactionTable.getItems().clear();

            messageLabel.setText(e.getMessage());
        }
    }
}
