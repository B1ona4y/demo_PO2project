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
        String newName = foodNameTextField.getText().trim();
        if (newName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Add");
            alert.setHeaderText("Missing Food Name");
            alert.setContentText("Please enter a food name before clicking Add.");
            alert.showAndWait();
            return;
        }

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

        myListView.getItems().add(newFood);

        foodNameTextField.clear();
        foodNumberTextField.clear();
        foodDatePicker.setValue(null);

        myListView.scrollTo(newFood);
    }

    public void editFood() {
        int index = myListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Edit");
            alert.setHeaderText("No selection");
            alert.setContentText("Please select a food before clicking Edit.");
            alert.showAndWait();
            return;
        }

        String newName = foodNameTextField.getText().trim();
        if (newName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Edit");
            alert.setHeaderText("Missing Food Name");
            alert.setContentText("Please enter a food name before clicking Edit.");
            alert.showAndWait();
            return;
        }
        int newNumber = Integer.parseInt(foodNumberTextField.getText().trim());
        if (textfildTests.testNum(newNumber)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Edit");
            alert.setHeaderText("Invalid Number");
            alert.setContentText("Please enter a food name before clicking Edit.");
            alert.showAndWait();
            return;
        }
        LocalDate newDate = foodDatePicker.getValue();

        myListView.getItems().set(index, new Food(newName, newNumber, newDate.toString()));
    }

    public void deleteFood() {
        int index = myListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            myListView.getItems().remove(index);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Delete");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select an item from the list before clicking Delete.");
            alert.showAndWait();
        }
    }
}