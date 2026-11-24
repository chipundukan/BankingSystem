package banking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    // Personal information.
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phoneNumber;

    // Employment information.
    private boolean employed;
    private String employerName;
    private String employerAddress;

    // Stores accounts belonging to the customer.
    private List<Account> accounts;

    // Creates a customer who is not registered as employed.
    public Customer(String customerId, String firstName,
                    String surname, String address, String phoneNumber) {

        this(customerId, firstName, surname, address,
             phoneNumber, false, null, null);
    }

    // Creates a customer with employment information.
    public Customer(String customerId, String firstName,
                    String surname, String address, String phoneNumber,
                    boolean employed, String employerName,
                    String employerAddress) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.employed = employed;
        this.employerName = employerName;
        this.employerAddress = employerAddress;

        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isEmployed() {
        return employed;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    // Adds an account to the customer's account list.
    public void addAccount(Account account) {

        if (account == null) {
            throw new IllegalArgumentException(
                "Account cannot be null."
            );
        }

        accounts.add(account);
    }

    // Returns a protected view of the customer's accounts.
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}