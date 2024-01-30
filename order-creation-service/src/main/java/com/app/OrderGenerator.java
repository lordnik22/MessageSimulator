package com.app;

import ch.hslu.msed.Customer;
import ch.hslu.msed.Order;
import ch.hslu.msed.PaymentMethod;
import ch.hslu.msed.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;

public class OrderGenerator {
    private static int orderNumber = 1000000;
    private static int customerNumber = 2000000;
    private static int positionNumber = 100;
    private static boolean customerGenerated = false;
    private static boolean positionGenerated = false;

    public String createRandomOrder() {
        Order order = new Order(
            this.getNextOrderNumber(),
            this.getRandomOrExistingCustomer(),
            getRandomPaymentMethod(),
            this.getRandomListOfPositions()
        );

        return encodeOrderToJson(order);
    }

    public static String encodeOrderToJson(Order order) {
        Gson gson = new Gson();
        return gson.toJson(order);
    }

    private int getNextOrderNumber() {
        return orderNumber++;
    }

    private static int getNextCustomerNumber() {
        return customerNumber++;
    }

    private static int getNextPositionNumber() {
        return positionNumber++;
    }

    private static int getRandomZip() {
        int min = 1000;
        int max = 9999;

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private Customer getRandomOrExistingCustomer() {
        Random random = new Random();

        return (random.nextInt(10) == 0) && customerGenerated ? Customer.getRandomCustomer() : this.createRandomCustomer();
    }

    private Customer createRandomCustomer() {
        customerGenerated = true;

        return new Customer(
            getNextCustomerNumber(),
            getRandomZip(),
            getRandomGender()
        );
    }

    public static String getRandomGender() {
        Random random = new Random();
        return (random.nextInt(2) == 0) ? "M" : "F";
    }

    private List<Position> getRandomListOfPositions() {
        List<Position> randomPositions = new ArrayList<>();
        Random random = new Random();

        // create randomly between 1 and 9 positions
        for (int i = 0; i < random.nextInt(10); i++) {
            // add existing position
            if (positionGenerated && random.nextInt(5) == 0) {
                randomPositions.add(
                    Position.getRandomPosition()
                );
            // add new random position
            } else {
                randomPositions.add(
                    new Position(
                        getNextPositionNumber(),
                        random.nextInt(10) + 1,
                        random.nextDouble() * 100
                    )
                );
                positionGenerated = true;
            }
        }

        return randomPositions;
    }

    private static PaymentMethod getRandomPaymentMethod() {
        Random random = new Random();
        PaymentMethod[] paymentMethods = PaymentMethod.values();
        return paymentMethods[random.nextInt(paymentMethods.length)];
    }
}
