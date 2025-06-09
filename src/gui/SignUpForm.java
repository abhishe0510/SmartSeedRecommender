package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class SignUpForm extends Application {

    private static final String USERS_FILE = "users.txt";

    @Override
    public void start(Stage stage) {
        Label userLabel = new Label("Choose Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Choose Password:");
        PasswordField passField = new PasswordField();

        Button registerBtn = new Button("Register");
        Label statusLabel = new Label();

        registerBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Username and Password required!");
                return;
            }

            // Save to file
            try (FileWriter fw = new FileWriter(USERS_FILE, true)) {
                fw.write(username + "," + password + "\n");
                statusLabel.setText("Registration successful!");
                stage.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                statusLabel.setText("Error registering.");
            }
        });

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, registerBtn, statusLabel);
        layout.setStyle("-fx-padding: 20;");
        stage.setScene(new Scene(layout, 300, 250));
        stage.setTitle("Sign Up");
        stage.show();
    }
}
