import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSInput;
import sun.plugin.javascript.navig.Anchor;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.Scanner;

public class BoardGUI extends Application implements EventHandler<ActionEvent> {
    Button button;
    Button EndGameButton;
    Stage window;
    Scene scene;

    public static String receiveInput(){
        String input = "";
        boolean invalidInput = true;
        while(invalidInput) {
            System.out.println("Enter your move");
            Scanner in = new Scanner(System.in);

        }
        return input;
    }

    public static void help(){
        System.out.println("");
        System.out.println("QUIT:       (quit game)");
        System.out.println("PASS:       (pass current move)");
        System.out.println("EXCHANGE <letters>:     (swaps these letters for new letters)");
        System.out.println("HELP:       (display this guide)");
        System.out.println("");
        System.out.println("How to place a word on the board:");
        System.out.println("- Starting tile position");
        System.out.println("- A or D for direction");
        System.out.println("- WORD");
        System.out.println("e.g. A3 D HELLO");
    }

    /**
     * Execute command to execute a command from user input
     * @param c - Command to execute
     * @return false if command not executed , or true if executed successfully
     */
    public static boolean execute(String c) {


        return true;
    }

    public static void main(String[] args) {
       // launch(args);
        help();
    }

    /**
     * Sets up primary window for game
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // main code for GUI goes here
        window = primaryStage;
        primaryStage.setTitle("Scrabble Game - Team Squash");

        window.setOnCloseRequest(e -> {
            e.consume();
            endProgram();
        });

        GridPane layout = new GridPane();
        layout.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");

        EndGameButton = new Button();
        EndGameButton.setText("Close Game");
        EndGameButton.setOnAction(e -> endProgram());
        EndGameButton.setStyle("-fx-background-color: #6b6b6b; -fx-text-fill: white;");

        layout.getChildren().addAll(EndGameButton);

        final Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    @Override
    public void handle(ActionEvent event) {

    }

    /**
     * gets user confirmation to end game and exit application
     */
    private void endProgram(){
            boolean result = PopUp.confirmDisplay("Are you sure?", "Do you want to end the game and exit the application?", "Yes", "No");
            if(result)
                window.close();
    }
}

//           Colour Scheme for Board:
//                #ebca67 yellow 2L
//                #6699e0 blue 2W
//                #4a9c3b green 3L
//                #e8486b pink 3W
//                ##e8e6e4 white background tiles
//                #303030 grey background window
//                #6b6b6b grey button accent