
package UI;

import javafx.application.Platform;



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

        table.getColumns().addAll(
                typeColumn,
                contentColumn,
                riskColumn,
                descriptionColumn,
                statusColumn
        );


        // Create ProgressPane

        progressPane = new ProgressPane();
        progressPane.getProgressLayout().setVisible(true);
        progressPane.getProgressLayout().setManaged(true);


        // Add Components to Root

        root.getChildren().addAll(
                progressPane.getProgressLayout(),
                table
        );


        // Create Scene



    }


    // =====================================
    // METHODS USED BY ANALYSIS CLASSES
    // =====================================


    // Start a new batch of analyses

    public void startBatch(int total) {

        Platform.runLater(() -> {

            totalItems = total;

            currentItem = 1;

            table.getItems().clear();

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

                progressPane.startAnalysis(
                        currentItem,
                        totalItems
                );

            }

        });
    }


    // Update current item's progress

    public void updateProgress(double progress) {

        Platform.runLater(() -> {

            progressPane.updateProgress(progress);

        });
    }


    // Add successful analysis result

    public void addResult(
            String type,
            String content,
            String risk,
            String description,
            String status) {

        Platform.runLater(() -> {

            ObservableList<String> row =
                    FXCollections.observableArrayList(
                            type,
                            content,
                            risk,
                            description,
                            status
                    );

            table.getItems().add(row);

        });
    }


    // Add failed analysis result

    public void addFailedResult(
            String type,
            String content,
            String errorMessage) {

        addResult(
                type,
                content,
                "Unverified",
                errorMessage,
                "Failed"
        );

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

/*package UI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class ResultsPane extends Application {

    private TableView<ObservableList<String>> table;
    private ProgressPane progressPane;

    private Timeline timeline;

    private int currentIndex = 0;
    private int currentStep = 0;

    private final int totalSteps = 20;

    private final String[][] inputs = {

            {"URL", "https://google.com", "Low",
                    "No threat detected", "Completed"},

            {"URL", "https://suspicious-site.com", "High",
                    "Suspicious URL detected", "Completed"},

            {"Message", "Hello, how are you?", "Low",
                    "No threat detected", "Completed"},

            {"Message", "Your account is blocked!", "High",
                    "Possible phishing message", "Completed"},

            {"CSV", "data.csv", "Not Found",
                    "Analysis could not be completed", "Not Found"},

            {"URL", "https://example.com", "Medium",
                    "Potential threat detected", "Completed"}
    };


    @Override
    public void start(Stage stage) {

        VBox root = new VBox(20);

        table = new TableView<>();

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


        typeColumn.setPrefWidth(120);
        contentColumn.setPrefWidth(220);
        riskColumn.setPrefWidth(120);
        descriptionColumn.setPrefWidth(250);
        statusColumn.setPrefWidth(150);


        table.getColumns().addAll(
                typeColumn,
                contentColumn,
                riskColumn,
                descriptionColumn,
                statusColumn
        );


        progressPane = new ProgressPane();


        root.getChildren().addAll(
                progressPane.getProgressLayout(),
                table
        );


        Scene scene = new Scene(root, 950, 600);

        stage.setTitle("Threat Detection App");

        stage.setScene(scene);

        stage.show();


        // Start simulation automatically

        startBatch();

    }


    private void startBatch() {

        currentIndex = 0;

        table.getItems().clear();

        analyzeNext();

    }


    private void analyzeNext() {

        if (currentIndex >= inputs.length) {

            progressPane.finishAll();

            return;
        }


        currentStep = 0;

        progressPane.startAnalysis(
                currentIndex + 1,
                inputs.length
        );


        timeline = new Timeline(

                new KeyFrame(Duration.millis(100), event -> {

                    currentStep++;

                    double progress =
                            (double) currentStep / totalSteps;

                    progressPane.updateProgress(progress);


                    if (currentStep >= totalSteps) {

                        timeline.stop();


                        // Add current result

                        String[] result = inputs[currentIndex];

                        ObservableList<String> row =
                                FXCollections.observableArrayList(
                                        result[0],
                                        result[1],
                                        result[2],
                                        result[3],
                                        result[4]
                                );

                        table.getItems().add(row);


                        // Move to next item

                        currentIndex++;

                        analyzeNext();

                    }

                })

        );


        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();

    }


    public static void main(String[] args) {

        launch(args);

    }

}*/