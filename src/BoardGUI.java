import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.HashMap;

public class BoardGUI extends Application implements EventHandler<ActionEvent> {
    Button button;
    Button EndGameButton;
    Stage window;
    Scene scene;
    /**COMMAND_MAP is a collection of all the recognised commands in the game, keyed by their canonical name in UPPERCASE*/
    static HashMap<String, Command> COMMAND_MAP = new HashMap<String, Command>(){{
        put("EXCHANGE", CommandsContainer::exchange);
        put("PLACE", CommandsContainer::place);
        put("PASS", CommandsContainer::pass);
        put("HELP", CommandsContainer::help);
        put("QUIT", CommandsContainer::quit);
    }};

    /**
     * Execute command to execute a command from user input
     * @param c - Command to execute
     * @param p - Player executing the command
     * @return false if command not executed , or true if executed successfully
     */
    public static boolean execute(String c, Player p) {
        // split the input string into individual tokens, using any whitespace character as a valid word separator
        // (the entire string is capitalised)
        // (all whitespace at the beginning or end of a line is removed entirely)
        // (all whitespace between words is replaced with a single <space> character to help ease the of splitting tokens)
        String[] tokens = c.toUpperCase().replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ").split(" ");
        String commandName = tokens[0];
        // if the first token of the command is a grid reference, set commandName to "PLACE"
        if(commandName.matches("[a-oA-O]\\d{1,2}")) commandName = "PLACE";
        // query the hashmap containing all known commands, if this command is not recognised then immediately quit
        if(COMMAND_MAP.containsKey(commandName))
            //if the command is recognised, attempt to run it
            return COMMAND_MAP.get(commandName).run(tokens, p);
        return false;
    }

    public static void main(String[] args) throws Exception {
        Thread mainThread = new Thread(new ScrabbleMainThread());
        mainThread.start();
        launch(args);
        mainThread.join();
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