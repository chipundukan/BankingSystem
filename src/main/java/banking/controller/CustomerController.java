package banking.controller;

import banking.dao.CustomerDAO;
import banking.model.Customer;

public class CustomerController {

    private final CustomerDAO customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAO();
    }

    // Registers a new customer.
    public String registerCustomer(Customer customer) {

        if (customer == null) {
            return "Invalid customer information.";
        }

        customerDAO.save(customer);

        if (customerDAO.findById(customer.getCustomerId()) != null) {
            return "Customer registered successfully.";
        }

        return "Customer registration failed. Check the database output.";
    }

    // Searches for a customer.
    public Customer searchCustomer(String customerId) {

        return customerDAO.findById(customerId);
    }

    // Updates customer information.
    public String updateCustomer(Customer customer) {

        if (customer == null) {
            return "Invalid customer information.";
        }

        if (customerDAO.findById(customer.getCustomerId()) == null) {
            return "Customer not found.";
        }

        customerDAO.update(customer);

        return "Customer update request completed.";
    }

    // Deletes a customer.
    public String deleteCustomer(String customerId) {

        if (customerDAO.findById(customerId) == null) {
            return "Customer not found.";
        }

        customerDAO.delete(customerId);

        return "Customer deletion request completed.";
    }
}