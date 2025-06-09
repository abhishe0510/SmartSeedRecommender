package gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Optional;

public class SeedFormDialog extends Dialog<SmartSeedRecommender.Seed> {

    private final TextField nameField = new TextField();
    private final TextField soilField = new TextField();
    private final TextField categoryField = new TextField();
    private final TextField minTempField = new TextField();
    private final TextField maxTempField = new TextField();
    private final TextField minHumidityField = new TextField();
    private final TextField maxHumidityField = new TextField();
    private final TextField minRainfallField = new TextField();
    private final TextField maxRainfallField = new TextField();
    private final TextField minPHField = new TextField();
    private final TextField maxPHField = new TextField();
    private final TextField costField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField yieldField = new TextField();

    public SeedFormDialog(Stage owner, SmartSeedRecommender.Seed seed) {
        setTitle(seed == null ? "Add Seed" : "Edit Seed");
        initOwner(owner);

        // Dialog buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;

        grid.add(new Label("Name:"), 0, row);
        grid.add(nameField, 1, row++);
        grid.add(new Label("Soil Type:"), 0, row);
        grid.add(soilField, 1, row++);
        grid.add(new Label("Category (Season):"), 0, row);
        grid.add(categoryField, 1, row++);
        grid.add(new Label("Min Temperature (°C):"), 0, row);
        grid.add(minTempField, 1, row++);
        grid.add(new Label("Max Temperature (°C):"), 0, row);
        grid.add(maxTempField, 1, row++);
        grid.add(new Label("Min Humidity (%):"), 0, row);
        grid.add(minHumidityField, 1, row++);
        grid.add(new Label("Max Humidity (%):"), 0, row);
        grid.add(maxHumidityField, 1, row++);
        grid.add(new Label("Min Rainfall (mm):"), 0, row);
        grid.add(minRainfallField, 1, row++);
        grid.add(new Label("Max Rainfall (mm):"), 0, row);
        grid.add(maxRainfallField, 1, row++);
        grid.add(new Label("Min Soil pH:"), 0, row);
        grid.add(minPHField, 1, row++);
        grid.add(new Label("Max Soil pH:"), 0, row);
        grid.add(maxPHField, 1, row++);
        grid.add(new Label("Production Cost (₹/acre):"), 0, row);
        grid.add(costField, 1, row++);
        grid.add(new Label("Market Price (₹/quintal):"), 0, row);
        grid.add(priceField, 1, row++);
        grid.add(new Label("Yield per Acre (quintals):"), 0, row);
        grid.add(yieldField, 1, row++);

        getDialogPane().setContent(grid);

        // Populate fields if editing
        if (seed != null) {
            nameField.setText(seed.getName());
            soilField.setText(seed.suitableSoil);
            categoryField.setText(seed.getCategory());
            minTempField.setText(String.valueOf(seed.minTemp));
            maxTempField.setText(String.valueOf(seed.maxTemp));
            minHumidityField.setText(String.valueOf(seed.minHumidity));
            maxHumidityField.setText(String.valueOf(seed.maxHumidity));
            minRainfallField.setText(String.valueOf(seed.minRainfall));
            maxRainfallField.setText(String.valueOf(seed.maxRainfall));
            minPHField.setText(String.valueOf(seed.minPH));
            maxPHField.setText(String.valueOf(seed.maxPH));
            costField.setText(String.valueOf(seed.productionCost));
            priceField.setText(String.valueOf(seed.marketPrice));
            yieldField.setText(String.valueOf(seed.yieldPerAcre));
        }

        // Enable/disable OK button based on validation
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        soilField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        categoryField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        minTempField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        maxTempField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        minHumidityField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        maxHumidityField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        minRainfallField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        maxRainfallField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        minPHField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        maxPHField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        costField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        priceField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));
        yieldField.textProperty().addListener((obs, oldVal, newVal) -> validateForm(okButton));

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText().trim();
                    String soil = soilField.getText().trim();
                    String category = categoryField.getText().trim();
                    double minTemp = Double.parseDouble(minTempField.getText().trim());
                    double maxTemp = Double.parseDouble(maxTempField.getText().trim());
                    double minHumidity = Double.parseDouble(minHumidityField.getText().trim());
                    double maxHumidity = Double.parseDouble(maxHumidityField.getText().trim());
                    double minRainfall = Double.parseDouble(minRainfallField.getText().trim());
                    double maxRainfall = Double.parseDouble(maxRainfallField.getText().trim());
                    double minPH = Double.parseDouble(minPHField.getText().trim());
                    double maxPH = Double.parseDouble(maxPHField.getText().trim());
                    double cost = Double.parseDouble(costField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());
                    double yield = Double.parseDouble(yieldField.getText().trim());

                    // Validate ranges
                    if (minTemp > maxTemp || minHumidity > maxHumidity || minRainfall > maxRainfall || minPH > maxPH) {
                        showAlert("Validation Error", "Minimum values cannot be greater than maximum values.");
                        return null;
                    }

                    return new SmartSeedRecommender.Seed(
                            // 1) name
                            name,
                            // 2) temperature range
                            minTemp, maxTemp,
                            // 3) humidity range
                            minHumidity, maxHumidity,
                            // 4) rainfall range
                            minRainfall, maxRainfall,
                            // 5) pH range
                            minPH, maxPH,
                            // 6) soil type & season (strings)
                            soil, category,
                            // 7) economic values
                            cost, price, yield
                    );

                } catch (NumberFormatException ex) {
                    showAlert("Input Error", "Please enter valid numbers in all numeric fields.");
                    return null;
                }
            }
            return null;
        });

        // Initial validation call
        validateForm(okButton);
    }

    private void validateForm(Button okButton) {
        boolean valid = !nameField.getText().trim().isEmpty() &&
                !soilField.getText().trim().isEmpty() &&
                !categoryField.getText().trim().isEmpty() &&
                isDouble(minTempField.getText()) &&
                isDouble(maxTempField.getText()) &&
                isDouble(minHumidityField.getText()) &&
                isDouble(maxHumidityField.getText()) &&
                isDouble(minRainfallField.getText()) &&
                isDouble(maxRainfallField.getText()) &&
                isDouble(minPHField.getText()) &&
                isDouble(maxPHField.getText()) &&
                isDouble(costField.getText()) &&
                isDouble(priceField.getText()) &&
                isDouble(yieldField.getText());

        okButton.setDisable(!valid);
    }

    private boolean isDouble(String str) {
        if (str == null || str.trim().isEmpty()) return false;
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<SmartSeedRecommender.Seed> showAddDialog() {
        // We don’t have a Stage owner handy here—pass null or grab from your application
        SeedFormDialog dialog = new SeedFormDialog(null, null);
        return dialog.showAndWait();
    }

    /**
     * Show the “Edit Seed” dialog, pre-populated with the given seed’s values.
     * Returns the updated seed (or empty if the user cancelled).
     */
    public static Optional<SmartSeedRecommender.Seed> showEditDialog(SmartSeedRecommender.Seed existing) {
        SeedFormDialog dialog = new SeedFormDialog(null, existing);
        return dialog.showAndWait();
    }

}
