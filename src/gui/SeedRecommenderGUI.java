package gui;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import gui.SmartSeedRecommender.AreaDetails;
import gui.SmartSeedRecommender.Seed;
import java.util.List;
import java.util.Optional;

public class SeedRecommenderGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("🌱 Smart Seed Recommender");

        Label soilLabel = new Label("Soil Type:");
        ComboBox<String> soilComboBox = new ComboBox<>();
        soilComboBox.getItems().addAll("Alluvial", "Black", "Red", "Laterite", "Mountain", "Desert", "Clay", "Loamy", "Sandy");

        Label tempLabel = new Label("Temperature (°C):");
        Slider tempSlider = new Slider(10, 50, 25);
        tempSlider.setMajorTickUnit(5);
        tempSlider.setMinorTickCount(0);
        tempSlider.setSnapToTicks(true);
        Label tempValueLabel = new Label(String.valueOf((int) tempSlider.getValue()));

        Label humidityLabel = new Label("Humidity (%):");
        Slider humiditySlider = new Slider(10, 100, 50);
        humiditySlider.setMajorTickUnit(10);
        humiditySlider.setMinorTickCount(0);
        humiditySlider.setSnapToTicks(true);
        Label humidityValueLabel = new Label(String.valueOf((int) humiditySlider.getValue()));

        Label rainfallLabel = new Label("Rainfall (mm):");
        Slider rainfallSlider = new Slider(100, 2500, 800);
        rainfallSlider.setMajorTickUnit(100);
        rainfallSlider.setMinorTickCount(0);
        rainfallSlider.setSnapToTicks(true);
        Label rainfallValueLabel = new Label(String.valueOf((int) rainfallSlider.getValue()));

        Label phLabel = new Label("Soil pH:");
        Slider phSlider = new Slider(4.5, 9.0, 6.5);
        phSlider.setMajorTickUnit(0.5);
        phSlider.setMinorTickCount(0);
        phSlider.setSnapToTicks(true);
        Label phValueLabel = new Label(String.format("%.1f", phSlider.getValue()));

        Label seasonLabel = new Label("Season:");
        ComboBox<String> seasonComboBox = new ComboBox<>();
        seasonComboBox.getItems().addAll("Kharif", "Rabi", "Zaid");

        Button recommendBtn = new Button("Get Recommendations");
        Button clearBtn = new Button("Clear");
        Button manageSeedsBtn = new Button("Manage Seeds");
        CheckBox darkModeToggle = new CheckBox("🌙 Dark Mode");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        ListView<Seed> resultList = new ListView<>();
        resultList.setPlaceholder(new Label("No recommendations yet."));
        resultList.setCellFactory(lv -> new ListCell<>() {
            private final VBox vbox = new VBox(2);
            private final Label nameLabel = new Label();
            private final Label detailsLabel = new Label();
            {
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                detailsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
                detailsLabel.setWrapText(true);
                vbox.getChildren().addAll(nameLabel, new Separator(), detailsLabel);
                vbox.setPadding(new Insets(5));
            }
            @Override
            protected void updateItem(Seed seed, boolean empty) {
                super.updateItem(seed, empty);
                if (empty || seed == null) {
                    setGraphic(null);
                } else {
                    String soilTitle = seed.suitableSoil.substring(0, 1).toUpperCase() + seed.suitableSoil.substring(1);
                    String seasonTitle = seed.getCategory().substring(0, 1).toUpperCase() + seed.getCategory().substring(1);
                    nameLabel.setText(seed.getName() + "  (" + seasonTitle + ")");
                    String summary = String.format(
                            "Soil: %s    Temp: %.0f–%.0f°C    Humidity: %.0f–%.0f%%\n" +
                                    "Rainfall: %.0f–%.0f mm    pH: %.1f–%.1f\n" +
                                    "Cost: ₹%.0f/acre    Price: ₹%.0f/quintal    Yield: %.0f qtl/acre",
                            soilTitle, seed.minTemp, seed.maxTemp,
                            seed.minHumidity, seed.maxHumidity,
                            seed.minRainfall, seed.maxRainfall,
                            seed.minPH, seed.maxPH,
                            seed.productionCost, seed.marketPrice,
                            seed.yieldPerAcre);
                    detailsLabel.setText(summary);
                    setGraphic(vbox);
                }
            }
        });

        tempSlider.valueProperty().addListener((obs, oldVal, newVal) -> tempValueLabel.setText(String.valueOf(newVal.intValue() / 5 * 5)));
        humiditySlider.valueProperty().addListener((obs, oldVal, newVal) -> humidityValueLabel.setText(String.valueOf(newVal.intValue() / 10 * 10)));
        rainfallSlider.valueProperty().addListener((obs, oldVal, newVal) -> rainfallValueLabel.setText(String.valueOf(newVal.intValue() / 100 * 100)));
        phSlider.valueProperty().addListener((obs, oldVal, newVal) -> phValueLabel.setText(String.format("%.1f", Math.round(newVal.doubleValue() * 2) / 2.0)));

        recommendBtn.setOnAction(e -> {
            try {
                String soilType = soilComboBox.getValue();
                int temperature = (int) tempSlider.getValue() / 5 * 5;
                int humidity = (int) humiditySlider.getValue() / 10 * 10;
                int rainfall = (int) rainfallSlider.getValue() / 100 * 100;
                double soilPH = Math.round(phSlider.getValue() * 2) / 2.0;
                String season = seasonComboBox.getValue();

                if (soilType == null || season == null) {
                    errorLabel.setText("⚠️ Please fill all fields.");
                    resultList.getItems().clear();
                    resultList.setPlaceholder(new Label("⚠️ Please fill all fields."));
                    return;
                }
                errorLabel.setText("");
                AreaDetails area = new AreaDetails(temperature, humidity, rainfall, soilPH, soilType, season);
                List<Seed> recommendations = SmartSeedRecommender.getRecommendations(area);
                if (recommendations.isEmpty()) {
                    resultList.getItems().clear();
                    resultList.setPlaceholder(new Label("❌ No suitable seeds found for the given conditions."));
                } else {
                    resultList.getItems().setAll(recommendations);
                }
            } catch (Exception ex) {
                errorLabel.setText("❌ Unexpected error: " + ex.getMessage());
                resultList.getItems().clear();
                resultList.setPlaceholder(new Label("❌ Unexpected error occurred."));
                ex.printStackTrace();
            }
        });

        clearBtn.setOnAction(e -> {
            soilComboBox.setValue(null);
            seasonComboBox.setValue(null);
            resultList.getItems().clear();
            resultList.setPlaceholder(new Label("No recommendations yet."));
            errorLabel.setText("");
        });

        manageSeedsBtn.setOnAction(e -> new SeedManagerDialog(primaryStage).showAndWait());

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        inputGrid.add(soilLabel, 0, 0);
        inputGrid.add(soilComboBox, 1, 0);

        inputGrid.add(tempLabel, 0, 1);
        inputGrid.add(tempSlider, 1, 1);
        inputGrid.add(tempValueLabel, 2, 1);

        inputGrid.add(humidityLabel, 0, 2);
        inputGrid.add(humiditySlider, 1, 2);
        inputGrid.add(humidityValueLabel, 2, 2);

        inputGrid.add(rainfallLabel, 0, 3);
        inputGrid.add(rainfallSlider, 1, 3);
        inputGrid.add(rainfallValueLabel, 2, 3);

        inputGrid.add(phLabel, 0, 4);
        inputGrid.add(phSlider, 1, 4);
        inputGrid.add(phValueLabel, 2, 4);

        inputGrid.add(seasonLabel, 0, 5);
        inputGrid.add(seasonComboBox, 1, 5);

        HBox buttonBox = new HBox(10, recommendBtn, clearBtn, manageSeedsBtn);

        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(titleLabel, inputGrid, darkModeToggle, buttonBox, errorLabel, resultList);

        Scene scene = new Scene(layout, 620, 640);
        primaryStage.setTitle("Smart Seed Recommender");
        primaryStage.setScene(scene);
        primaryStage.show();

        darkModeToggle.setOnAction(e -> {
            if (darkModeToggle.isSelected()) {
                scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
            } else {
                scene.getStylesheets().clear();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
