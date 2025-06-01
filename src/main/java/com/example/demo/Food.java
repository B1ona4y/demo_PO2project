package com.example.demo;

public class Food {
    private final String name;
    private final int number;
    private final String date;

    public Food(String name, int number, String date) {
        this.name = name;
        this.number = number;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return name;
    }
}
