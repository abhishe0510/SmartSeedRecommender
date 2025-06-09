package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginForm extends Application {

    private static final String USERS_FILE = "users.txt";
    private Map<String, String> userDatabase = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        loadUserDatabase();

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();

        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");

        Label statusLabel = new Label();

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();

            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                statusLabel.setText("Login successful!");
                System.out.println("Login successful, opening main GUI...");

                // Close the login window
                primaryStage.close();

                // Show the main GUI
                SeedRecommenderGUI seedGUI = new SeedRecommenderGUI();
                Stage newStage = new Stage();
                
                // Launch main GUI
                try {
                    new SeedRecommenderGUI().start(new Stage());
                    primaryStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                statusLabel.setText("Invalid username or password.");
            }
        });

        signUpBtn.setOnAction(e -> {
            SignUpForm signUpForm = new SignUpForm();
            signUpForm.start(new Stage());
        });

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, signUpBtn, statusLabel);
        layout.setStyle("-fx-padding: 20;");
        primaryStage.setScene(new Scene(layout, 300, 250));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void loadUserDatabase() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return; // no users yet
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

