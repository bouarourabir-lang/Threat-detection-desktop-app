
package UI;

import javafx.application.Platform;


import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import javafx.scene.layout.VBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.beans.property.SimpleStringProperty;


public class ResultsPane {
    private TableView<ObservableList<String>> table;

    private ProgressPane progressPane;

    private int totalItems = 0;

    private int currentItem = 0;
    private VBox resultsLayout;


    public ResultsPane() {

        // Create Root
        VBox root = new VBox(20);
        resultsLayout = root;


        // Create Table
        table = new TableView<>();
        table.setPrefHeight(250);
        table.getStyleClass().add("results-table");



        // Create Columns

        TableColumn<ObservableList<String>, String> typeColumn =
                new TableColumn<>("Input Type");

        TableColumn<ObservableList<String>, String> contentColumn =
                new TableColumn<>("Content");

        TableColumn<ObservableList<String>, String> riskColumn =
                new TableColumn<>("Risk Level");

        TableColumn<ObservableList<String>, String> descriptionColumn =
                new TableColumn<>("Description");

        TableColumn<ObservableList<String>, String> statusColumn =
                new TableColumn<>("Status");


        // Connect Columns to Data

        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(0)));

        contentColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(1)));

        riskColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(2)));

        descriptionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(3)));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get(4)));


        // Set Column Widths

        typeColumn.setPrefWidth(120);

        contentColumn.setPrefWidth(220);

        riskColumn.setPrefWidth(120);

        descriptionColumn.setPrefWidth(250);

        statusColumn.setPrefWidth(150);


        // Add Columns

        table.getColumns().addAll(typeColumn, contentColumn, riskColumn, descriptionColumn, statusColumn);


        // Create ProgressPane

        progressPane = new ProgressPane();
        progressPane.getProgressLayout().setVisible(true);
        progressPane.getProgressLayout().setManaged(true);


        // Add Components to Root

        root.getChildren().addAll(progressPane.getProgressLayout(), table);
    }

    // Start a new batch of analyses

    public void startBatch(int total) {
        Platform.runLater(() -> {
            totalItems = total;
            currentItem = 1;
            table.getItems().clear();
            progressPane.reset();

            if (totalItems > 0) {
                progressPane.startAnalysis(1, totalItems);
            }
        });
    }


    // Start analyzing the next item

    public void startNextItem() {

        Platform.runLater(() -> {

            currentItem++;

            if (currentItem <= totalItems) {

                progressPane.startAnalysis(currentItem,totalItems);

            }});
    }


    // Update current item's progress

    public void updateProgress(double progress) {

        Platform.runLater(() -> {

            progressPane.updateProgress(progress);

        });
    }


    // Add successful analysis result

    public void addResult(String type,String content, String risk, String description, String status) {

        Platform.runLater(() -> {

            ObservableList<String> row =FXCollections.observableArrayList(type, content, risk, description, status);

            table.getItems().add(row);

        });
    }


    // Add failed analysis result

    public void addFailedResult(String type, String content, String errorMessage) {

        addResult(type, content, "Unverified", errorMessage, "Failed");

    }


    // Finish all analyses

    public void finishBatch() {

        Platform.runLater(() -> {

            progressPane.finishAll();

        });
    }


    public VBox getResultsLayout() {
        return resultsLayout;
    }

}
