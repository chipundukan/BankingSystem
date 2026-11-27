package banking.ui;

import banking.controller.TransactionController;
import banking.model.Transaction;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class TransactionView extends VBox {

    private final TransactionController controller;

    private final TextField accountNumberField = new TextField();

    private final Label messageLabel = new Label();
    private final Label countLabel = new Label("0 transactions");

    private final TableView<Transaction> transactionTable =
            new TableView<>();

    public TransactionView() {

        controller = new TransactionController();

        setSpacing(22);
        setPadding(new Insets(30));
        getStyleClass().add("content-area");

        // Page heading.
        Label title = new Label("Transaction History");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Search and review transactions made on bank accounts."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox heading = new VBox(6, title, subtitle);

        // Search section.
        Label searchTitle = new Label("Find Transactions");
        searchTitle.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        Label accountLabel = new Label("Account Number");

        accountNumberField.setPromptText("e.g. S001");
        accountNumberField.setPrefWidth(300);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchTransactions());

        Button clearButton = new Button("Clear");
        clearButton.setStyle(
                "-fx-background-color: #64748b;"
        );
        clearButton.setOnAction(e -> clearSearch());

        HBox searchControls = new HBox(
                12,
                accountNumberField,
                searchButton,
                clearButton
        );

        searchControls.setAlignment(Pos.CENTER_LEFT);

        accountNumberField.setOnAction(e -> searchTransactions());

        VBox searchCard = new VBox(
                12,
                searchTitle,
                accountLabel,
                searchControls,
                messageLabel
        );

        searchCard.getStyleClass().add("card");

        messageLabel.getStyleClass().add("message-label");

        // Transaction table heading.
        Label tableTitle = new Label("Transaction Records");

        tableTitle.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold;"
        );

        countLabel.setStyle(
                "-fx-text-fill: #64748b; -fx-font-size: 12px;"
        );

        HBox tableHeading = new HBox(
                tableTitle,
                countLabel
        );

        tableHeading.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(tableTitle, Priority.ALWAYS);

        // Transaction ID column.
        TableColumn<Transaction, String> idColumn =
                new TableColumn<>("Transaction ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionId")
        );

        // Transaction type column.
        TableColumn<Transaction, String> typeColumn =
                new TableColumn<>("Transaction Type");

        typeColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionType")
        );

        // Transaction amount column.
        TableColumn<Transaction, String> amountColumn =
                new TableColumn<>("Amount (BWP)");

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );

        // Transaction date column.
        TableColumn<Transaction, String> dateColumn =
                new TableColumn<>("Transaction Date");

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("transactionDate")
        );

        transactionTable.getColumns().addAll(
                idColumn,
                typeColumn,
                amountColumn,
                dateColumn
        );

        transactionTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        transactionTable.setPlaceholder(
                new Label("Search using an account number to view transactions.")
        );

        transactionTable.setPrefHeight(400);
        transactionTable.setMinHeight(250);

        // Table section.
        VBox tableCard = new VBox(
                15,
                tableHeading,
                transactionTable
        );

        tableCard.getStyleClass().add("card");

        VBox.setVgrow(tableCard, Priority.ALWAYS);
        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        getChildren().addAll(
                heading,
                searchCard,
                tableCard
        );
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

            countLabel.setText(
                    transactions.size() + " transaction(s)"
            );

            messageLabel.setText(
                    transactions.size() + " transaction(s) found."
            );

        } catch (IllegalArgumentException e) {

            transactionTable.getItems().clear();

            countLabel.setText("0 transactions");

            messageLabel.setText(e.getMessage());
        }
    }

    // Clears the search field and transaction records.
    private void clearSearch() {

        accountNumberField.clear();

        transactionTable.getItems().clear();

        countLabel.setText("0 transactions");

        messageLabel.setText("");

    }
}