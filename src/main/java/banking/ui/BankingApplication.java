package banking.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class BankingApplication extends Application {

    private BorderPane mainLayout;

    @Override
    public void start(Stage stage) {

        mainLayout = new BorderPane();

        // Create the navigation sidebar.
        VBox sidebar = createSidebar();

        // Display the dashboard when the application starts.
        showDashboard();

        mainLayout.setLeft(sidebar);

        Scene scene = new Scene(mainLayout, 1000, 650);

        stage.setTitle("Banking Management System");
        stage.setMinWidth(800);
        stage.setMinHeight(550);
        stage.setScene(scene);
        stage.show();
    }

    // Creates the navigation sidebar.
    private VBox createSidebar() {

        Label heading = new Label("BANKING SYSTEM");

        heading.setStyle(
                "-fx-font-size: 19px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        Button dashboardButton = createButton("Dashboard");
        Button customersButton = createButton("Customers");
        Button accountsButton = createButton("Accounts");
        Button transactionsButton = createButton("Transactions");

        dashboardButton.setOnAction(e -> showDashboard());

        customersButton.setOnAction(e ->
                showSection("Customer Management"));

        accountsButton.setOnAction(e ->
                showSection("Account Management"));

        transactionsButton.setOnAction(e ->
                showSection("Transaction History"));

        VBox sidebar = new VBox(
                20,
                heading,
                dashboardButton,
                customersButton,
                accountsButton,
                transactionsButton
        );

        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(230);

        sidebar.setStyle("-fx-background-color: #172554;");

        return sidebar;
    }

    // Creates a navigation button.
    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefWidth(180);
        button.setAlignment(Pos.CENTER_LEFT);

        button.setStyle(
                "-fx-background-color: #1e40af;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12px;"
        );

        return button;
    }

    // Displays the dashboard.
    private void showDashboard() {

        Label title = new Label("Dashboard");

        title.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        Label welcome = new Label(
                "Welcome to the Banking Management System."
        );

        Label information = new Label(
                "Use the navigation menu to manage customers, " +
                "accounts and transactions."
        );

        VBox content = new VBox(
                20,
                title,
                welcome,
                information
        );

        content.setPadding(new Insets(40));

        mainLayout.setCenter(content);
    }

    // Displays the selected application section.
    private void showSection(String sectionName) {

        if (sectionName.equals("Customer Management")) {

            // Opens the customer management interface.
            mainLayout.setCenter(new CustomerView());

        } else {

            Label title = new Label(sectionName);

            title.setStyle(
                    "-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;"
            );

            Label message = new Label(
                    "The " + sectionName +
                    " interface will be implemented here."
            );

            VBox content = new VBox(20, title, message);

            content.setPadding(new Insets(40));

            mainLayout.setCenter(content);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}