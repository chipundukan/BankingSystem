package banking.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BankingApplication extends Application {

    private BorderPane mainLayout;
    private VBox sidebar;

    @Override
    public void start(Stage stage) {

        mainLayout = new BorderPane();

        // Create the navigation sidebar.
        createSidebar();

        // Display the dashboard when the application opens.
        showDashboard();

        Scene scene = new Scene(mainLayout, 1200, 750);

        // Load the external stylesheet.
        String css = getClass().getResource("/banking.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Banking Management System");
        stage.setScene(scene);
        stage.setMinWidth(950);
        stage.setMinHeight(650);
        stage.show();
    }

    private void createSidebar() {

        sidebar = new VBox(15);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        Label brand = new Label("BANKING SYSTEM");
        brand.getStyleClass().add("brand");

        Label menuTitle = new Label("MAIN MENU");
        menuTitle.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;"
        );

        Button dashboardButton = createNavButton("Dashboard");
        Button customerButton = createNavButton("Customers");
        Button accountButton = createNavButton("Accounts");
        Button transactionButton = createNavButton("Transactions");

        dashboardButton.setOnAction(event -> showDashboard());

        customerButton.setOnAction(event ->
            showSection("Customer Management"));

        accountButton.setOnAction(event ->
            showSection("Account Management"));

        transactionButton.setOnAction(event ->
            showSection("Transaction History"));

        RegionSpacer spacer = new RegionSpacer();

        Label footer = new Label("Banking System\nVersion 1.0");
        footer.setStyle(
            "-fx-text-fill: #94a3b8;" +
            "-fx-font-size: 11px;"
        );

        sidebar.getChildren().addAll(
            brand,
            new Label(" "),
            menuTitle,
            dashboardButton,
            customerButton,
            accountButton,
            transactionButton,
            spacer,
            footer
        );

        mainLayout.setLeft(sidebar);
    }

    private Button createNavButton(String text) {

        Button button = new Button(text);

        button.getStyleClass().add("nav-button");
        button.setMaxWidth(Double.MAX_VALUE);

        return button;
    }

    private void showDashboard() {

        VBox content = new VBox(25);
        content.getStyleClass().add("content-area");

        Label heading = new Label("Dashboard");
        heading.getStyleClass().add("page-title");

        Label subtitle = new Label(
            "Welcome to your Banking Management System."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox headingSection = new VBox(7);
        headingSection.getChildren().addAll(heading, subtitle);

        // Dashboard information cards.
        HBox cards = new HBox(18);

        VBox customerCard = createCard(
            "CUSTOMERS",
            "Customer Management",
            "Register and manage customer information."
        );

        VBox accountCard = createCard(
            "ACCOUNTS",
            "Account Management",
            "Create accounts and manage balances."
        );

        VBox transactionCard = createCard(
            "TRANSACTIONS",
            "Transaction History",
            "View recorded banking transactions."
        );

        HBox.setHgrow(customerCard, Priority.ALWAYS);
        HBox.setHgrow(accountCard, Priority.ALWAYS);
        HBox.setHgrow(transactionCard, Priority.ALWAYS);

        cards.getChildren().addAll(
            customerCard,
            accountCard,
            transactionCard
        );

        // Quick action section.
        Label quickActionsTitle = new Label("Quick Actions");
        quickActionsTitle.setStyle(
            "-fx-font-size: 19px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #172554;"
        );

        Button manageCustomers = new Button("Manage Customers");
        Button manageAccounts = new Button("Manage Accounts");
        Button viewTransactions = new Button("View Transactions");

        manageCustomers.setOnAction(event ->
            showSection("Customer Management"));

        manageAccounts.setOnAction(event ->
            showSection("Account Management"));

        viewTransactions.setOnAction(event ->
            showSection("Transaction History"));

        VBox actions = new VBox(15);
        actions.getChildren().addAll(
            manageCustomers,
            manageAccounts,
            viewTransactions
        );

        content.getChildren().addAll(
            headingSection,
            cards,
            quickActionsTitle,
            actions
        );

        mainLayout.setCenter(content);
    }

    private VBox createCard(
        String title,
        String subtitle,
        String description
    ) {

        VBox card = new VBox(12);

        card.getStyleClass().add("card");

        Label cardTitle = new Label(title);
        cardTitle.getStyleClass().add("card-title");

        Label cardSubtitle = new Label(subtitle);
        cardSubtitle.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #172554;"
        );

        Label cardDescription = new Label(description);
        cardDescription.setWrapText(true);
        cardDescription.getStyleClass().add("page-subtitle");

        card.getChildren().addAll(
            cardTitle,
            cardSubtitle,
            cardDescription
        );

        return card;
    }

    private void showSection(String sectionName) {

        switch (sectionName) {

            case "Customer Management":
                mainLayout.setCenter(new CustomerView());
                break;

            case "Account Management":
                mainLayout.setCenter(new AccountView());
                break;

            case "Transaction History":
                mainLayout.setCenter(new TransactionView());
                break;

            default:
                showDashboard();
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Pushes the footer towards the bottom of the sidebar.
    private static class RegionSpacer extends javafx.scene.layout.Region {
        RegionSpacer() {
            VBox.setVgrow(this, Priority.ALWAYS);
        }
    }
}