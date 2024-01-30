package ch.hslu.msed.messagesimulator;

import java.util.List;

public class Order {
    private int number;
    private Customer customer;
    private List<Position> positions;
    private PaymentMethod paymentMethod;
    private boolean partialDeliveryEnabled = false;

    public Order(int number, Customer customer, PaymentMethod paymentMethod, List<Position> positions) {
        this.number = number;
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.positions = positions;
    }

    // Getters and setters for all properties

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isPartialDeliveryEnabled() {
        return partialDeliveryEnabled;
    }

    public void setPartialDeliveryEnabled(boolean partialDeliveryEnabled) {
        this.partialDeliveryEnabled = partialDeliveryEnabled;
    }

    // Method to add an order position
    public void addPosition(Position position) {
        positions.add(position);
    }

    public double getOrderAmount() {
        double totalCost = 0.0;
        for (Position position : positions) {
            totalCost += position.getAmount() * position.getPrice();
        }
        return totalCost;
    }
}
