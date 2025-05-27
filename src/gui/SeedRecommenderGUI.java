package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SeedRecommenderGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create UI elements
        Label welcomeLabel = new Label("Smart Seed Recommender");

        Label soilLabel = new Label("Soil Type:");
        ComboBox<String> soilComboBox = new ComboBox<>();
        soilComboBox.getItems().addAll("Alluvial", "Black", "Red", "Laterite", "Mountain", "Desert");

        Label tempLabel = new Label("Temperature (°C):");
        Slider tempSlider = new Slider(10, 50, 25);
        tempSlider.setShowTickLabels(true);
        tempSlider.setShowTickMarks(true);

        Label humidityLabel = new Label("Humidity (%):");
        Slider humiditySlider = new Slider(10, 100, 50);
        humiditySlider.setShowTickLabels(true);
        humiditySlider.setShowTickMarks(true);

        // Declare resultArea before using it
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);

        Button recommendBtn = new Button("Get Recommendations");

        // Button action handler
        recommendBtn.setOnAction(e -> {
            String soilType = soilComboBox.getValue();
            double temperature = tempSlider.getValue();
            double humidity = humiditySlider.getValue();

            if (soilType == null) {
                resultArea.setText("Please select a soil type.");
                return;
            }

            java.util.List<gui.SmartSeedRecommender.Seed> recommendations =
                    gui.SmartSeedRecommender.getRecommendations(soilType, temperature, humidity);

            if (recommendations.isEmpty()) {
                resultArea.setText("No suitable seeds found for the given conditions.");
            } else {
                StringBuilder resultText = new StringBuilder();
                for (gui.SmartSeedRecommender.Seed seed : recommendations) {
                    resultText.append(seed.toString()).append("\n");
                }
                resultArea.setText(resultText.toString());
            }

        });

        // Layout
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(
                welcomeLabel,
                soilLabel, soilComboBox,
                tempLabel, tempSlider,
                humidityLabel, humiditySlider,
                recommendBtn,
                resultArea
        );

        // Set Scene and Stage
        Scene scene = new Scene(layout, 400, 450);
        primaryStage.setTitle("Smart Seed Recommender GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
