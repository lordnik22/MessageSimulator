package ch.hslu.msed.messagesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {
    private int number;
    private int zip;
    private String gender;
    private String customerStatus = "NEW";

    private static List<Customer> customerList = new ArrayList<>();

    public Customer(int number, int zip, String gender) {
        this.number = number;
        this.zip = zip;
        this.gender = gender;

        customerList.add(this);
    }

    // Getters and setters for all properties

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public static Customer getRandomCustomer() {
        Random random = new Random();
        int randomIndex = random.nextInt(customerList.size());
        Customer randomCustomer = customerList.get(randomIndex);
        randomCustomer.setCustomerStatus("EXISTING");
        return randomCustomer;
    }
}
