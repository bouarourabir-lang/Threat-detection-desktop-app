
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

public class InputPane extends Application {

    @Override
    public void start(Stage stage) {

        // 1. Create root
        VBox root = new VBox(15);
      //15 المساحة بين عناصر الحاوية vbox
        

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
          
            urlField.setPromptText("Enter URL here...");//تجي داخل الفايلد توضح للمستخد وين يكتب


            urlFieldsBox.getChildren().add(urlField);

            analyze.setVisible(true);
            analyze.setManaged(true);
        });

        // 15. When Add Message is clicked
        addMes.setOnAction(e -> {

            TextArea MesArea = new TextArea();

            MesArea.setPromptText("Enter Message here...");
            MesArea.setPrefRowCount(4);

            MesFieldsBox.getChildren().add(MesArea);

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

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                fileLabel.setText(selectedFile.getName());

                analyze.setVisible(true);
                analyze.setManaged(true);
            }
        });

        // 17. Create Group
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
            new Label("Threat Detection"),
            inputGroup,
            scrollPane,
            analyze
        );

        // 20. Create and show window
        Scene scene = new Scene(root, 500, 400);

        stage.setScene(scene);
        stage.setTitle("Threat Detection");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
 Root (VBox)
  |
  |--- Threat Detection (Label)
  |
  |--- Input Group (Group)
  |       |
  |       |--- RadioBox (HBox)
  |               |
  |               |--- URL RadioButton
  |               |
  |               |--- Message RadioButton
  |               |
  |               |--- CSV File RadioButton
  |
  |--- ScrollPane
  |       |
  |       |--- InputsBox (VBox)
  |               |
  |               |--- URL Box (VBox)
  |               |       |
  |               |       |--- Add URL Button
  |               |       |
  |               |       |--- URL Fields Box (VBox)
  |               |               |
  |               |               |--- TextField 1
  |               |               |
  |               |               |--- TextField 2
  |               |               |
  |               |               |--- TextField 3
  |               |
  |               |--- Message Box (VBox)
  |               |       |
  |               |       |--- Add Message Button
  |               |       |
  |               |       |--- Message Fields Box (VBox)
  |               |               |
  |               |               |--- TextArea 1
  |               |               |
  |               |               |--- TextArea 2
  |               |
  |               |--- CSV Box (VBox)
  |                       |
  |                       |--- Browse Files Button
  |                       |
  |                       |--- File Label
  |
  |--- Analyze Button
  */
 