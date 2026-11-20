
package UI;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ProgressPane {

    private ProgressBar progressBar;
    private Label progressLabel;
    private Label analysisLabel;
    private VBox progressLayout;

    public ProgressPane() {

        // Create ProgressBar
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.getStyleClass().add("analysis-progress");

        // Create Labels
        progressLabel = new Label("Progress: 0%");
        analysisLabel = new Label("Waiting for analysis...");

        // Create VBox
        progressLayout = new VBox(15);
        progressLayout.setAlignment(Pos.CENTER);

        progressLayout.getChildren().addAll(
                analysisLabel,
                progressBar,
                progressLabel
        );
    }

    // Start analyzing an item
    public void startAnalysis(int current, int total) {
        analysisLabel.setText("Analyzing " + current + " of " + total);
    }
    public void reset() {
        progressBar.setProgress(0);
        progressLabel.setText("Progress: 0%");
    }

    // Update Progress
    public void updateProgress(double progress) {

        progress = Math.max(0, Math.min(1, progress));

        progressBar.setProgress(progress);

        int percentage = (int) (progress * 100);

        progressLabel.setText(
                "Progress: " + percentage + "%"
        );
    }

    // Finish all analyses
    public void finishAll() {

        progressBar.setProgress(1);

        progressLabel.setText("Progress: 100%");

        analysisLabel.setText("All Analysis Completed");
    }

    // Get Layout
    public VBox getProgressLayout() {

        return progressLayout;
    }
}
