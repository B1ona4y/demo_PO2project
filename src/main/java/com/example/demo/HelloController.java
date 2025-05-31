package com.example.demo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class HelloController {
    @FXML
    private ListView<Food> myListView;

    @FXML
    private Label myLabel;

    @FXML
    private TextField foodNameTextField;

    @FXML
    private TextField foodNumberTextField;

    @FXML
    private DatePicker foodDatePicker;

    @FXML
    private Button addFoodAddButton;

    @FXML
    private Button addFoodEditButton;

    @FXML
    private Button addFoodDeleteButton;

    String[] food  = {"Pizza", "Burger", "Fries", "Chicken", "Heroin", "Coca", "Krokodil", "Bebra", "Knife", "M4A4", "Male Reproductive System"};

    private final Food[] initialFoods = new Food[] {
            new Food("Pizza", 1, "2025-05-01"),
            new Food("Burger", 2, "2025-05-02"),
            new Food("Fries", 3, "2025-05-03"),
            new Food("Chicken", 4, "2025-05-04"),
            // … you can add more initial items if you like
    };

    Food currentFood;

    public void initialize() {

        myListView.getItems().addAll(initialFoods);

        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Food>() {

            @Override
            public void changed(ObservableValue<? extends Food> observable, Food oldFood, Food newFood) {
                currentFood = newFood;
                foodNameTextField.setText(currentFood.getName());
                foodNumberTextField.setText(Integer.toString(currentFood.getNumber()));
                foodDatePicker.setValue(LocalDate.parse(currentFood.getDate()));
            }

        });
    }

    public void addFood() {
        // 4.1 Read the name that the user typed
        String newName = foodNameTextField.getText().trim();

        if (newName.isEmpty()) {
            // If the name field is blank, do nothing (or show an alert)
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Add");
            alert.setHeaderText("Missing Food Name");
            alert.setContentText("Please enter a food name before clicking Add.");
            alert.showAndWait();
            return;
        }

        // 4.2 Optionally, read number & date as well
        String numberValue = foodNumberTextField.getText().trim();
        if (!numberValue.isEmpty()) {
            if (Integer.parseInt(numberValue) <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Add");
                alert.setHeaderText("Invalid Number");
                alert.setContentText("Please enter a positive number for the number of items.");
                alert.showAndWait();
                return;
            }
        }

        LocalDate dateValue = foodDatePicker.getValue();

        Food newFood = new Food(newName, Integer.parseInt(numberValue), dateValue.toString());

        // 4.3 Insert the new item into the ListView
        myListView.getItems().add(newFood);

        // 4.4 Clear the TextField(s) so it’s ready for a new entry
        foodNameTextField.clear();
        foodNumberTextField.clear();
        foodDatePicker.setValue(null);

        // 4.5 Optionally, scroll the ListView to the newly added item
        myListView.scrollTo(newFood);
    }

    public void editFood() {
    }

    public void deleteFood() {
    }
}