package com.example.demo;

public class Food {
    private final String name;
    private final double quantity; // изменили с int на double
    private final String unit;

    public Food(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        // при выводе в ListView отображаем "Яблоки (1.5 кг)" или просто "Яблоки", если целое
        if (quantity == Math.floor(quantity)) {
            // целое число
            return String.format("%s (%d %s)", name, (int)quantity, unit);
        } else {
            // дробное
            return String.format("%s (%.2f %s)", name, quantity, unit);
        }
    }
}
