package banking.ui;

import banking.controller.CustomerController;
import banking.model.Customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CustomerView extends VBox {

    private final CustomerController controller;

    private final TextField customerIdField = new TextField();
    private final TextField firstNameField = new TextField();
    private final TextField surnameField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField phoneField = new TextField();

    private final CheckBox employedCheckBox =
            new CheckBox("Customer is employed");

    private final TextField employerNameField = new TextField();
    private final TextField employerAddressField = new TextField();

    private final Label messageLabel = new Label();

    public CustomerView() {

        controller = new CustomerController();

        setSpacing(20);
        setPadding(new Insets(30));
        getStyleClass().add("content-area");

        // Page heading.
        Label title = new Label("Customer Management");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label(
                "Register, search and manage customer information."
        );
        subtitle.getStyleClass().add("page-subtitle");

        VBox heading = new VBox(6);
        heading.getChildren().addAll(title, subtitle);

        // Main customer information card.
        VBox formCard = new VBox(22);
        formCard.getStyleClass().add("card");

        Label formTitle = new Label("Customer Information");
        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        Label formDescription = new Label(
                "Enter the customer's personal and contact details."
        );
        formDescription.getStyleClass().add("page-subtitle");

        // Prepare input fields.
        customerIdField.setPromptText("e.g. CUST001");
        firstNameField.setPromptText("Enter first name");
        surnameField.setPromptText("Enter surname");
        addressField.setPromptText("Enter residential address");
        phoneField.setPromptText("Enter phone number");

        employerNameField.setPromptText("Enter employer name");
        employerAddressField.setPromptText("Enter employer address");

        // Make fields expand to available width.
        customerIdField.setMaxWidth(Double.MAX_VALUE);
        firstNameField.setMaxWidth(Double.MAX_VALUE);
        surnameField.setMaxWidth(Double.MAX_VALUE);
        addressField.setMaxWidth(Double.MAX_VALUE);
        phoneField.setMaxWidth(Double.MAX_VALUE);
        employerNameField.setMaxWidth(Double.MAX_VALUE);
        employerAddressField.setMaxWidth(Double.MAX_VALUE);

        // Two-column form layout.
        GridPane grid = new GridPane();

        grid.setHgap(25);
        grid.setVgap(18);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50);

        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50);

        grid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn
        );

        addField(grid, "Customer ID", customerIdField, 0, 0);
        addField(grid, "First Name", firstNameField, 1, 0);

        addField(grid, "Surname", surnameField, 0, 1);
        addField(grid, "Phone Number", phoneField, 1, 1);

        addField(grid, "Residential Address", addressField, 0, 2);

        employedCheckBox.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #334155;"
        );

        grid.add(employedCheckBox, 1, 2);

        addField(grid, "Employer Name", employerNameField, 0, 3);
        addField(grid, "Employer Address", employerAddressField, 1, 3);

        employedCheckBox.setOnAction(e -> updateEmploymentFields());

        updateEmploymentFields();

        // Action buttons.
        Button registerButton = new Button("Register Customer");
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear Form");

        registerButton.setOnAction(e -> registerCustomer());
        searchButton.setOnAction(e -> searchCustomer());
        updateButton.setOnAction(e -> updateCustomer());
        deleteButton.setOnAction(e -> deleteCustomer());
        clearButton.setOnAction(e -> clearFields());

        HBox buttonBar = new HBox(12);

        buttonBar.setAlignment(Pos.CENTER_LEFT);

        buttonBar.getChildren().addAll(
                registerButton,
                searchButton,
                updateButton,
                deleteButton,
                clearButton
        );

        // Status message.
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setWrapText(true);

        formCard.getChildren().addAll(
                formTitle,
                formDescription,
                new Separator(),
                grid,
                new Separator(),
                buttonBar,
                messageLabel
        );

        // Scrollable page.
        VBox page = new VBox(22);
        page.getChildren().addAll(heading, formCard);

        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        getChildren().add(scrollPane);
    }

    // Adds a labelled input field to the form.
    private void addField(
            GridPane grid,
            String labelText,
            TextField field,
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

        grid.add(fieldBox, column, row);
    }

    // Registers a customer in the database.
    private void registerCustomer() {

        try {

            Customer customer = createCustomer();

            String result = controller.registerCustomer(customer);

            messageLabel.setText(result);

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());
        }
    }

    // Searches for a customer using the customer ID.
    private void searchCustomer() {

        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            messageLabel.setText("Enter a customer ID.");
            return;
        }

        Customer customer = controller.searchCustomer(customerId);

        if (customer == null) {
            messageLabel.setText("Customer not found.");
            return;
        }

        firstNameField.setText(customer.getFirstName());
        surnameField.setText(customer.getSurname());
        addressField.setText(customer.getAddress());
        phoneField.setText(customer.getPhoneNumber());

        employedCheckBox.setSelected(customer.isEmployed());

        employerNameField.setText(customer.getEmployerName());
        employerAddressField.setText(customer.getEmployerAddress());

        updateEmploymentFields();

        messageLabel.setText("Customer found successfully.");
    }

    // Updates an existing customer.
    private void updateCustomer() {

        try {

            Customer customer = createCustomer();

            String result = controller.updateCustomer(customer);

            messageLabel.setText(result);

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());
        }
    }

    // Deletes a customer using the customer ID.
    private void deleteCustomer() {

        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            messageLabel.setText("Enter a customer ID.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Delete Customer");
        confirmation.setHeaderText("Confirm customer deletion");
        confirmation.setContentText(
                "Are you sure you want to delete customer " +
                customerId + "?"
        );

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        String result = controller.deleteCustomer(customerId);

        messageLabel.setText(result);
    }

    // Creates a customer object from the form information.
    private Customer createCustomer() {

        String customerId = customerIdField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String surname = surnameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (customerId.isEmpty()
                || firstName.isEmpty()
                || surname.isEmpty()
                || address.isEmpty()
                || phone.isEmpty()) {

            throw new IllegalArgumentException(
                    "Please complete all required customer fields."
            );
        }

        boolean employed = employedCheckBox.isSelected();

        String employerName = employed
                ? employerNameField.getText().trim() : null;

        String employerAddress = employed
                ? employerAddressField.getText().trim() : null;

        if (employed && (employerName.isEmpty()
                || employerAddress.isEmpty())) {

            throw new IllegalArgumentException(
                    "Enter the employer name and address."
            );
        }

        return new Customer(
                customerId,
                firstName,
                surname,
                address,
                phone,
                employed,
                employerName,
                employerAddress
        );
    }

    // Enables employer fields only when the customer is employed.
    private void updateEmploymentFields() {

        boolean employed = employedCheckBox.isSelected();

        employerNameField.setDisable(!employed);
        employerAddressField.setDisable(!employed);
    }

    // Clears the form.
    private void clearFields() {

        customerIdField.clear();
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
        phoneField.clear();

        employedCheckBox.setSelected(false);

        employerNameField.clear();
        employerAddressField.clear();

        updateEmploymentFields();

        messageLabel.setText("");
    }
}