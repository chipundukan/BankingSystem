package banking.ui;

import banking.controller.CustomerController;
import banking.model.Customer;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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

        setSpacing(12);
        setPadding(new Insets(30));

        Label title = new Label("Customer Management");

        title.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        customerIdField.setPromptText("Customer ID");
        firstNameField.setPromptText("First name");
        surnameField.setPromptText("Surname");
        addressField.setPromptText("Address");
        phoneField.setPromptText("Phone number");
        employerNameField.setPromptText("Employer name");
        employerAddressField.setPromptText("Employer address");

        Button registerButton = new Button("Register");
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        Button clearButton = new Button("Clear");

        registerButton.setOnAction(e -> registerCustomer());
        searchButton.setOnAction(e -> searchCustomer());
        updateButton.setOnAction(e -> updateCustomer());
        deleteButton.setOnAction(e -> deleteCustomer());
        clearButton.setOnAction(e -> clearFields());

        employedCheckBox.setOnAction(e -> updateEmploymentFields());

        updateEmploymentFields();

        getChildren().addAll(
                title,
                new Label("Customer ID"),
                customerIdField,
                new Label("First Name"),
                firstNameField,
                new Label("Surname"),
                surnameField,
                new Label("Address"),
                addressField,
                new Label("Phone Number"),
                phoneField,
                employedCheckBox,
                employerNameField,
                employerAddressField,
                registerButton,
                searchButton,
                updateButton,
                deleteButton,
                clearButton,
                messageLabel
        );
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