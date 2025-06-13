package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;
import javafx.stage.Window;

import java.io.*;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HelloController {
    public static Scanner myReader;
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

    private static List<Food> collectionOfFoods = new LinkedList<>();

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
        configSetup();
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

    private List<Food> searchList(String searchWords, List<Food> listOfFoods) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return listOfFoods.stream()
                .filter(food -> searchWordsArray.stream()
                        .allMatch(word -> food.getName().toLowerCase().contains(word.toLowerCase())))
                .distinct()
                .collect(Collectors.toList());
    }

    public void addFood() {
        String newName = foodNameTextField.getText().trim();
        if (newName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a food name before clicking Add.")
                    .showAndWait();
            return;
        }

        String rawNumberValue = foodNumberTextField.getText().trim();
        if (rawNumberValue.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a food number before clicking Add.")
                    .showAndWait();
            return;
        }

        double numberValue;
        try {
            numberValue = Double.parseDouble(rawNumberValue);
            if (numberValue < 0) {
                new Alert(Alert.AlertType.WARNING,
                        "Please number of a food must be >= 0.")
                        .showAndWait();
                return;
            }
        }
        catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a positive number for the quantity.")
                    .showAndWait();
            return;
        }


        String unit = unitComboBox.getValue();
        if (unit == null || unit.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a unit type for the items.")
                    .showAndWait();
            return;
        }

        Food newFood = new Food(newName, numberValue, unit);

        boolean existsInCollection = false;
        for (Food food : collectionOfFoods) {
            if (food.getName().equalsIgnoreCase(newName)) {
                existsInCollection = true;
                break;
            }
        }

        if (existsInCollection) {
            ObservableList<Food> items = myListView.getItems();
            boolean merged = false;
            for (int i = 0; i < items.size(); i++) {
                Food existing = items.get(i);
                if (existing.getName().equalsIgnoreCase(newName)) {
                    double updatedQty = existing.getQuantity() + numberValue;
                    items.set(i, new Food(newName, updatedQty, unit));
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                items.add(newFood);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Food cant be added!");
            alert.setHeaderText("The food item is not in the allowed collection.");
            alert.setContentText("Please enter a food item from the master list or add it to the collection.");
            alert.showAndWait();
        }
    }

    public void editFood() {
        int index = myListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select a food before clicking Edit.")
                    .showAndWait();
            return;
        }
        String newName = foodNameTextField.getText().trim();
        if (newName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a food name before clicking Edit.")
                    .showAndWait();
            return;
        }
        double newNumber;
        try {
            newNumber = Double.parseDouble(foodNumberTextField.getText().trim());
            if (newNumber <= 0) {
                new Alert(Alert.AlertType.WARNING,
                        "Please enter a positive number for the quantity.")
                        .showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a positive number for the quantity.")
                    .showAndWait();
            return;
        }

        String unit = unitComboBox.getValue();
        if (unit == null || unit.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a unit type for the items.")
                    .showAndWait();
            return;
        }

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
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));
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
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));
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

    public void configSetup() {
        Path path = Path.of("src/main/resources/confList.txt");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return;
            }

            List<String> lines = Files.readAllLines(path);

            collectionOfFoods.clear();

            for (String data : lines) {
                if (data.isBlank()) continue;          // skip empty
                String[] parts = data.split(";");
                if (parts.length != 2) continue;      // skip bad lines

                String name = parts[0];
                String unit = parts[1];
                double quantity = 0.0;                // default

                collectionOfFoods.add(new Food(name, quantity, unit));
            }

        } catch (IOException e) {

            e.printStackTrace();
            new Alert(AlertType.ERROR,
                    "Error loading configuration file:\n" + e.getMessage()
            ).showAndWait();
        }
    }


    public void addNewFood() {
        String name = foodNameTextField.getText().trim();
        String unit = unitComboBox.getValue();

        if (name.isEmpty()) {
            new Alert(AlertType.WARNING, "Please enter a food name.").showAndWait();
            return;
        }

        for (Food f : collectionOfFoods) {
            if (f.getName().equalsIgnoreCase(name)) {
                new Alert(AlertType.INFORMATION, "This food already exists.").showAndWait();
                return;
            }
        }

        String lineToAdd = String.format("%s;%s", name, unit);
        Path path = Path.of("src/main/resources/confList.txt");

        System.out.println("Writing to: " + path.toAbsolutePath());

        try {
            Files.write(
                    path,
                    Collections.singletonList(lineToAdd),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            collectionOfFoods.add(new Food(name, 0, unit));
            foodNameTextField.clear();
            unitComboBox.getSelectionModel().clearSelection();

            new Alert(AlertType.INFORMATION, "New food added and saved to file.").showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(AlertType.ERROR, "Could not save to file:\n" + e.getMessage())
                    .showAndWait();
        }
    }

}