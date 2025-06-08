package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.stage.Window;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.FileReader;

import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class HelloController {
    @FXML
    private ListView<Food> myListView;

    @FXML
    private ListView<Food> searchListView;

    @FXML
    private Label myLabel;

    @FXML
    private TextField foodNameTextField;

    @FXML
    private TextField foodNumberTextField;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private Button clearButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button warningOKbutton;

    @FXML
    private Button addtoAFCButton;



    @FXML
    private Button addFoodAddButton;

    @FXML
    private Button addFoodEditButton;

    @FXML
    private Button addFoodDeleteButton;

    private final Food[] collectionOfFoods = new Food[]{
            new Food("Pizza", 0, "piece"),
            new Food("Burger", 0, "piece"),
            new Food("Fries", 0, "piece"),
            new Food("Chicken", 0, "piece"),
            new Food("Sauce", 0, "piece"),
            new Food("Cheese", 0, "piece"),
            new Food("Pepperoni", 0, "piece"),
            new Food("Soda", 0, "piece"),
            new Food("Tomato", 0, "piece"),
            new Food("Veggie", 0, "piece"),
            new Food("Tomato", 0, "piece"),
            //...
    };

    private final Food[] initialFoods = new Food[]{};

    private final String[] units = new String[]{
            "kg",
            "l",
            "gr",
            "mg",
            "piece"
    };

    Food currentFood;

    public void initialize() {
        myListView.getItems().addAll(initialFoods);
        unitComboBox.getItems().addAll(units);
        searchListView.getItems().addAll(initialFoods);

        ChangeListener<Food> foodSelectionListener = (observable, oldFood, newFood) -> {
            if (newFood != null) {
                currentFood = newFood;
                foodNameTextField.setText(currentFood.getName());
                foodNumberTextField.setText(Double.toString(currentFood.getQuantity()));
                unitComboBox.setValue(currentFood.getUnit());
            }
        };

        myListView.getSelectionModel().selectedItemProperty().addListener(foodSelectionListener);
        searchListView.getSelectionModel().selectedItemProperty().addListener(foodSelectionListener);
    }

    public void search() {
        searchListView.getItems().clear();
        List<Food> searchResult = searchList(foodNameTextField.getText(), collectionOfFoods);
        searchListView.getItems().addAll(searchResult);
    }

    private List<Food> searchList(String searchWords, Food[] listOfFoods) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return Arrays.stream(listOfFoods)
                .filter(food -> searchWordsArray.stream()
                        .allMatch(word -> food.getName().toLowerCase().contains(word.toLowerCase())))
                        .distinct()
                        .collect(Collectors.toList());
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

        String unit = unitComboBox.getValue();
        if (unit == null || unit.isEmpty()) {
            if (Integer.parseInt(numberValue) <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cannot Add");
                alert.setHeaderText("Missing Unit");
                alert.setContentText("Please enter a unit type for the items.");
                alert.showAndWait();
                return;
            }
        }

        Food newFood = new Food(newName, Integer.parseInt(numberValue), unit);

        boolean existsInCollection = false;
        for (Food food : collectionOfFoods) {
            if (food.getName().equalsIgnoreCase(newName)) {
                existsInCollection = true;
                break;
            }
        }

        if (existsInCollection) {
            myListView.getItems().add(newFood);
            foodNameTextField.clear();
            foodNumberTextField.clear();
            unitComboBox.setValue(null);
            myListView.scrollTo(newFood);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Food Not Allowed"); // Corrected typo
            alert.setHeaderText("The food item is not in the allowed collection.");
            alert.setContentText("Please enter a food item from the master list.");
            alert.showAndWait();
        }
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
        int newNumber = 0;
        try {
            newNumber = Integer.parseInt(foodNumberTextField.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Edit");
            alert.setHeaderText("Invalid Number");
            alert.setContentText("Please enter a positive number for the quantity.");
            alert.showAndWait();
            return;
        }

        if (newNumber <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cannot Edit");
            alert.setHeaderText("Invalid Number");
            alert.setContentText("Please enter a positive number for the quantity.");
            alert.showAndWait();
            return;
        }

        String unit = unitComboBox.getValue();

        myListView.getItems().set(index, new Food(newName, newNumber, unit));
    }

    public void clearFoodFields() {
        foodNameTextField.clear();
        foodNumberTextField.clear();
        unitComboBox.setValue(null);
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

    public void saveListToTxt() {
        Window window = myListView.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save buy list");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );
        File file = fileChooser.showSaveDialog(window);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Food food : myListView.getItems()) {
                    String line = String.format("%s;%f;%s",
                            food.getName(),
                            food.getQuantity(),
                            food.getUnit()
                    );
                    writer.write(line);
                    writer.newLine();
                }
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setContentText("File saved to:\n" + file.getAbsolutePath());
                successAlert.showAndWait();
            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setContentText("Error while saving file:\n" + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    public void loadListFromTxt() {
        Window window = myListView.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open buy list");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                myListView.getItems().clear();

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        String name = parts[0];
                        String raw = parts[1];
                        double quantity;
                        String cleaned = raw.trim().replace(',', '.');
                        try {
                            quantity = Double.parseDouble(cleaned);
                        } catch (NumberFormatException nfe) {
                            System.err.println("Не удалось распарсить число из строки: '" + cleaned + "'");
                            continue;
                        }

                        String unit = parts[2];

                        Food food = new Food(name, quantity, unit);
                        myListView.getItems().add(food);
                    }
                }

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Файл успешно загружен из:\n" + file.getAbsolutePath());
                successAlert.showAndWait();

            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Ошибка при загрузке файла");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }
}