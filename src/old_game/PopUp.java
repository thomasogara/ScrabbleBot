package old_game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class PopUp {
    public static boolean choice;

    /**
     * Displays a pop up alert box with a message
     * @param title   - title to be displayed in pop up
     * @param message - message to be displayed in pop up
     */
    public static void messageDisplay(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #6b6b6b; -fx-text-fill: white;");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label, closeButton);
        layout.setStyle("-fx-background-color: #6b6b6b; -fx-text-fill: white;");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Custom confirmation message allowing for custom options to be specified (instead of standard Yes/No
     * @param title - title of pop up box
     * @param message - message for pop up box
     * @param option1 - first option for user to select
     * @param option2 - second option for user to select
     * @return boolean confirming whether option 1 or option 2 was taken
     */
    public static boolean confirmDisplay(String title, String message, String option1, String option2){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-text-fill: white;");

        Button opt1Button = new Button(option1);
        opt1Button.setOnAction(e -> {
            choice = true;
            window.close();
        });

        Button opt2Button = new Button(option2);
        opt2Button.setOnAction(e -> {
            choice = false;
            window.close();
        } );

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(opt1Button, opt2Button);
        buttons.setAlignment(Pos.CENTER);
        buttons.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");

        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");
        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return choice;
    }
}