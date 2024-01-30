package ch.hslu.msed.messagesimulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Position {
    private int number;
    private int amount;
    private double price;

    private static List<Position> positionList = new ArrayList<>();

    public Position(int number, int amount, double price) {
        this.number = number;
        this.amount = amount;
        this.price = price;

        positionList.add(this);
    }

    // Getters and setters for all properties

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static Position getRandomPosition() {
        Random random = new Random();
        int randomIndex = random.nextInt(positionList.size());
        return positionList.get(randomIndex);
    }
}
