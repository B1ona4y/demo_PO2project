package com.example.demo;

public class Food {
    private final String name;
    private final int number;
    private final String unit;

    public Food(String name, int number, String unit) {
        this.name = name;
        this.number = number;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name;
    }
}
