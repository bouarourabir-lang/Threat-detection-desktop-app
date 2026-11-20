package UI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import Data.InputItem;
import Data.InputType;
import Data.ThreatResult;
import Csv.CsvParserService;
import java.io.IOException;
import javafx.scene.control.Alert;


public class InputPane extends Application {

    @Override
    public void start(Stage stage) {

        // 1. Create root
        VBox root = new VBox(15);
        //15 المساحة بين عناصر الحاوية vbox
        List<TextField> urlFields = new ArrayList<>();

        List<TextArea> messageFields = new ArrayList<>();

        File[] selectedCSV = new File[1];

        ResultsPane resultsPane = new ResultsPane();

        VBox resultsLayout = resultsPane.getResultsLayout();

        resultsLayout.setVisible(false);
        resultsLayout.setManaged(false);

        // 2. Create RadioButton
        RadioButton urlRadio = new RadioButton("URL");
        RadioButton MesRadio = new RadioButton("Message");
        RadioButton csvRadio = new RadioButton("CSV File");

        // 3. Create ToggleGroup
        ToggleGroup group = new ToggleGroup();

        urlRadio.setToggleGroup(group);
        MesRadio.setToggleGroup(group);
        csvRadio.setToggleGroup(group);

        // 4. Create Buttons
        Button addUrl = new Button("Add URL");
        Button addMes = new Button("Add Message");
        Button browseCSV = new Button("Browse Files");

        // 5. Create a VBox to contain URL fields and Message areas
        VBox urlFieldsBox = new VBox(10);
        VBox MesFieldsBox = new VBox(10);

        // 6. Create a Label to display selected CSV file
        Label fileLabel = new Label("No file selected");
        //تعرض النص لي داخلها للمستخدم
        // 7. Create HBox to contain RadioButtons
        HBox radioBox = new HBox(15);

        radioBox.getChildren().addAll(
                urlRadio, MesRadio, csvRadio
        );

        // 8. Create a VBox to contain input controls
        VBox urlBox = new VBox(10);
        urlBox.getChildren().addAll(addUrl, urlFieldsBox);

        VBox MesBox = new VBox(10);
        MesBox.getChildren().addAll(addMes, MesFieldsBox);

        VBox csvBox = new VBox(10);
        csvBox.getChildren().addAll(browseCSV, fileLabel);

        // 9. Hide all input sections initially
        urlBox.setVisible(false);
        //تخفي العنصر
        urlBox.setManaged(false);
        //تخفي مسلاحة العنصر
        MesBox.setVisible(false);
        MesBox.setManaged(false);

        csvBox.setVisible(false);
        csvBox.setManaged(false);

        // 10. Create Analyze Button
        Button analyze = new Button("Analyze");

        analyze.setVisible(false);
        analyze.setManaged(false);

        // 11. When URL is selected
        //eمعنتها الاوامر لي رح تتنفد كي يضغط على الزر

        urlRadio.setOnAction(e -> {

            urlBox.setVisible(true);
            urlBox.setManaged(true);

            MesBox.setVisible(false);
            MesBox.setManaged(false);

            csvBox.setVisible(false);
            csvBox.setManaged(false);
        });

        // 12. When Message is selected
        MesRadio.setOnAction(e -> {

            MesBox.setVisible(true);
            MesBox.setManaged(true);

            urlBox.setVisible(false);
            urlBox.setManaged(false);

            csvBox.setVisible(false);
            csvBox.setManaged(false);
        });

        // 13. When CSV is selected
        csvRadio.setOnAction(e -> {

            csvBox.setVisible(true);
            csvBox.setManaged(true);

            urlBox.setVisible(false);
            urlBox.setManaged(false);

            MesBox.setVisible(false);
            MesBox.setManaged(false);
        });

        // 14. When Add URL is clicked
        addUrl.setOnAction(e -> {

            TextField urlField = new TextField();

            urlField.setPromptText("Enter URL here...");

            urlFieldsBox.getChildren().add(urlField);

            urlFields.add(urlField);

            analyze.setVisible(true);
            analyze.setManaged(true);
        });

        // 15. When Add Message is clicked
        addMes.setOnAction(e -> {

            TextArea MesArea = new TextArea();

            MesArea.setPromptText("Enter Message here...");
            MesArea.setPrefRowCount(4);

            MesFieldsBox.getChildren().add(MesArea);

            messageFields.add(MesArea);

            analyze.setVisible(true);
            analyze.setManaged(true);
        });

        // 16. When Browse CSV is clicked
        browseCSV.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Select CSV File");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            selectedCSV[0] = fileChooser.showOpenDialog(stage);
            if (selectedCSV[0] != null) {
                fileLabel.setText(selectedCSV[0].getName());

                analyze.setVisible(true);
                analyze.setManaged(true);
            }
        });

        // 17. Create Group
        analyze.setOnAction(e -> {

            List<InputItem> items = new ArrayList<>();

            // Collect URLs
            for (TextField field : urlFields) {

                String url = field.getText();

                if (url != null && !url.trim().isEmpty()) {
                    items.add(new InputItem(InputType.URL, url));
                }
            }

            // Collect Messages
            for (TextArea area : messageFields) {

                String message = area.getText();

                if (message != null && !message.trim().isEmpty()) {
                    items.add(new InputItem(InputType.MESSAGE, message));
                }
            }
            // Collect CSV data
                    // Collect CSV data
                    if (selectedCSV[0] != null) {

                        try {
                            CsvParserService csvParser = new CsvParserService();

                            List<InputItem> csvItems =
                                    csvParser.parseCsv(selectedCSV[0].getAbsolutePath());

                            items.addAll(csvItems);

                            List<String> csvErrors = csvParser.getErrors();

                            if (!csvErrors.isEmpty()) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("CSV Warnings");
                                alert.setHeaderText("Some rows could not be read");
                                alert.setContentText(String.join("\n", csvErrors));
                                alert.showAndWait();
                            }

                        } catch (IOException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("CSV Error");
                            alert.setHeaderText("Cannot read CSV file");
                            alert.setContentText(ex.getMessage());
                            alert.showAndWait();
                            return;
                        }
                    }

            if (items.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Input");
                alert.setHeaderText("No data to analyze");
                alert.setContentText("Please enter data or select a CSV file.");
                alert.showAndWait();
                return;
            }


            // Analyze inputs

// Show Results
            resultsLayout.setVisible(true);
            resultsLayout.setManaged(true);

// Start batch
            resultsPane.startBatch(items.size());
            analyze.setDisable(true); //ta3til zir ta3 analyzer
// Create a background thread
            Thread analysisThread = new Thread(() -> {

                Concurrency.AnalysisManager manager =
                        new Concurrency.AnalysisManager();

                // Analyze inputs one by one
                for (int i = 0; i < items.size(); i++) {

                    InputItem item = items.get(i);
                    System.out.println("Analyzing item " + (i + 1) + " of " + items.size());

                    if (i > 0) {
                        resultsPane.startNextItem();
                    }

                    try {

                        ThreatResult result = manager.analyzeInput(item);

                        resultsPane.addResult(
                                item.getType().toString(),
                                result.getInput(),
                                result.getLevel(),
                                result.getDescription(),
                                "Completed"
                        );

                    } catch (Exception ex) {

                        resultsPane.addFailedResult(
                                item.getType().toString(),
                                item.getContent(),
                                ex.getMessage()
                        );
                    }

                    // Update overall progress
                    resultsPane.updateProgress(
                            (double) (i + 1) / items.size()
                    );
                }

                // Finish all analyses
                resultsPane.finishBatch();
                javafx.application.Platform.runLater(() -> {
                    analyze.setDisable(false);
                });

            });

// Start the thread
            analysisThread.setDaemon(true);
            analysisThread.start();


        });
        Group inputGroup = new Group(radioBox);

        // 18. Create ScrollPane
        VBox inputsBox = new VBox(15);

        inputsBox.getChildren().addAll(
                urlBox,
                MesBox,
                csvBox
        );

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(inputsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);

        // 19. Add elements to root
        root.getChildren().addAll(
                new Label("Threat Detection"), inputGroup, scrollPane, analyze, resultsLayout);

        // 20. Create and show window
        Scene scene = new Scene(root, 900, 700);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Threat Detection");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

