package gui;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import gui.SmartSeedRecommender.AreaDetails;
import gui.SmartSeedRecommender.Seed;

import java.util.List;

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

        // TableView instead of ListView
        TableView<Seed> resultTable = new TableView<>();
        resultTable.setPlaceholder(new Label("No recommendations yet."));

        // Table columns
        TableColumn<Seed, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Seed, String> soilCol = new TableColumn<>("Soil");
        soilCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().suitableSoil));

        TableColumn<Seed, String> tempCol = new TableColumn<>("Temp (°C)");
        tempCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.0f–%.0f", data.getValue().minTemp, data.getValue().maxTemp)));

        TableColumn<Seed, String> humidityCol = new TableColumn<>("Humidity (%)");
        humidityCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.0f–%.0f", data.getValue().minHumidity, data.getValue().maxHumidity)));

        TableColumn<Seed, String> rainfallCol = new TableColumn<>("Rainfall (mm)");
        rainfallCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.0f–%.0f", data.getValue().minRainfall, data.getValue().maxRainfall)));

        TableColumn<Seed, String> phCol = new TableColumn<>("pH");
        phCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.1f–%.1f", data.getValue().minPH, data.getValue().maxPH)));

        TableColumn<Seed, String> costCol = new TableColumn<>("Cost (₹/acre)");
        costCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("₹%.0f", data.getValue().productionCost)));

        TableColumn<Seed, String> priceCol = new TableColumn<>("Price (₹/quintal)");
        priceCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("₹%.0f", data.getValue().marketPrice)));

        TableColumn<Seed, String> yieldCol = new TableColumn<>("Yield (qtl/acre)");
        yieldCol.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%.0f", data.getValue().yieldPerAcre)));

        resultTable.getColumns().addAll(nameCol, soilCol, tempCol, humidityCol, rainfallCol, phCol, costCol, priceCol, yieldCol);

        // Slider listeners to update labels
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
                    resultTable.getItems().clear();
                    resultTable.setPlaceholder(new Label("⚠️ Please fill all fields."));
                    return;
                }
                errorLabel.setText("");
                AreaDetails area = new AreaDetails(temperature, humidity, rainfall, soilPH, soilType, season);
                List<Seed> recommendations = SmartSeedRecommender.getRecommendations(area);
                if (recommendations.isEmpty()) {
                    resultTable.getItems().clear();
                    resultTable.setPlaceholder(new Label("❌ No suitable seeds found for the given conditions."));
                } else {
                    resultTable.getItems().setAll(recommendations);
                }
            } catch (Exception ex) {
                errorLabel.setText("❌ Unexpected error: " + ex.getMessage());
                resultTable.getItems().clear();
                resultTable.setPlaceholder(new Label("❌ Unexpected error occurred."));
                ex.printStackTrace();
            }
        });

        clearBtn.setOnAction(e -> {
            soilComboBox.setValue(null);
            seasonComboBox.setValue(null);
            resultTable.getItems().clear();
            resultTable.setPlaceholder(new Label("No recommendations yet."));
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
        layout.getChildren().addAll(titleLabel, inputGrid, darkModeToggle, buttonBox, errorLabel, resultTable);

        Scene scene = new Scene(layout, 1000, 640); // Increased width for table
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
