package gui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class SeedManagerDialog extends Stage {

    public SeedManagerDialog(Stage owner) {
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
        setTitle("Manage Seeds");

        // Grab the one shared, observable seed list
        ObservableList<SmartSeedRecommender.Seed> seedData =
                SmartSeedRecommender.getSeedDatabase();

        // Create a ListView of Seed, bound to that list
        ListView<SmartSeedRecommender.Seed> seedList = new ListView<>(seedData);

        // Display only the seed name in each row
        seedList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SmartSeedRecommender.Seed seed, boolean empty) {
                super.updateItem(seed, empty);
                setText(empty || seed == null ? null : seed.getName());
            }
        });


        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");

        addBtn.setOnAction(e -> {
            Optional<SmartSeedRecommender.Seed> result = SeedFormDialog.showAddDialog();
            result.ifPresent(seed -> {
                SmartSeedRecommender.getSeedDatabase().add(seed);
                // seedList updates automatically
            });
        });

        editBtn.setOnAction(e -> {
            int selectedIndex = seedList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                // use the local seedData, not a nonexistent field:
                SmartSeedRecommender.Seed selectedSeed = seedData.get(selectedIndex);

                Optional<SmartSeedRecommender.Seed> result =
                        SeedFormDialog.showEditDialog(selectedSeed);

                result.ifPresent(newSeed -> {
                    seedData.set(selectedIndex, newSeed);
                    seedList.refresh();
                });
            }
        });


        deleteBtn.setOnAction(e -> {
            int selectedIndex = seedList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SmartSeedRecommender.getSeedDatabase().remove(selectedIndex);
                // seedList updates automatically

            }
        });

        HBox btnBox = new HBox(10, addBtn, editBtn, deleteBtn);

        VBox root = new VBox(10, new Label("Seed List"), seedList, btnBox);
        root.setPadding(new Insets(10));

        setScene(new Scene(root, 400, 300));
    }
}
