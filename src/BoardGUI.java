import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.util.HashMap;

public class BoardGUI extends Application implements EventHandler<ActionEvent> {

    /** GUI wrappers **/
    public static Stage window;
    public static Scene scrabbleScene;

    /** GUI layouts **/
    public static BorderPane rootLayout;
    public static BorderPane boardContainer;
    public static Pane boardGrid;
    public static HBox topContainer;
    public static HBox bottomContainer;
    public static VBox sideContainer;

    public static Pane LetterContainerTop;
    public static Pane LetterContainerBottom;
    public static Pane NumberContainerRight;
    public static Pane NumberContainerLeft;

    /** GUI components **/
    public static Button endGameBtn;
    public static TextField gameInput;

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
    public static Scrabble.CommandReturnWrapper execute(String c, Player p) {
        // split the input string into individual tokens, using any whitespace character as a valid word separator
        // (the entire string is capitalised)
        // (all whitespace at the beginning or end of a line is removed entirely)
        // (all whitespace between words is replaced with a single <space> character to help ease the of splitting tokens)
        String[] tokens = c.toUpperCase().replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ").split(" ");
        String commandName = tokens[0];
        // if the first token of the command is a grid reference, set commandName to "PLACE"
        if(commandName.matches("[A-O]\\d{1,2}")) commandName = "PLACE";
        // query the hashmap containing all known commands, if this command is not recognised then immediately quit
        if(COMMAND_MAP.containsKey(commandName))
            //if the command is recognised, attempt to run it
            return COMMAND_MAP.get(commandName).run(tokens, p);

        return new Scrabble.CommandReturnWrapper();
    }

    public static void main(String[] args) throws Exception {
        Thread mainThread = new Thread(new ScrabbleMainThread());
        mainThread.start();
        launch(args);
        mainThread.join();
    }

    /**
     * Sets up primary window for game
     * [ PS: This MAY be split into multiple classes in the END to reduce clutter of everything being in a single method ]
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialize the stage & title
        window = primaryStage;
        window.setTitle("Scrabble Game - Team Squash");
        window.setOnCloseRequest(e -> {
            e.consume();
            endProgram();
        });

        // Initialize the GUI layouts
        rootLayout = new BorderPane();
        boardGrid = new Pane();
        topContainer = new HBox();
        bottomContainer = new HBox();
        sideContainer = new VBox();
        boardContainer = new BorderPane();
        rootLayout.setCenter(boardContainer);
        rootLayout.setTop(topContainer);
        rootLayout.setBottom(bottomContainer);
        rootLayout.setRight(sideContainer);

        LetterContainerTop = new Pane();
        LetterContainerBottom = new Pane();
        NumberContainerRight = new Pane();
        NumberContainerLeft = new Pane();
        boardContainer.setTop(LetterContainerTop);
        boardContainer.setBottom(LetterContainerBottom);
        boardContainer.setRight(NumberContainerRight);
        boardContainer.setLeft(NumberContainerLeft);
        boardContainer.setCenter(boardGrid);

        NumberContainerRight.setStyle("-fx-background-color: #685eeb; -fx-text-fill: white;");
        NumberContainerLeft.setStyle("-fx-background-color: #685eeb; -fx-text-fill: white;");

        // Set layout constraints & stylings
        boardGrid.setPrefSize(Scrabble.BOARD_WIDTH, Scrabble.BOARD_HEIGHT);
        boardGrid.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");
        topContainer.setPadding(new Insets(15, 12, 15, 12));
        sideContainer.setStyle("-fx-background-color: #ebebeb; -fx-text-fill: white;");
        sideContainer.setPrefWidth(300);
        LetterContainerTop.setPrefHeight(Scrabble.POINT_HEIGHT);
        LetterContainerBottom.setPrefHeight(Scrabble.POINT_HEIGHT);
        NumberContainerRight.setPrefHeight(3000);
        NumberContainerLeft.setPrefWidth(Scrabble.POINT_WIDTH);

        // Initialize respective components, their EventListeners & add to layouts
        endGameBtn = new Button("End Game");
        endGameBtn.setStyle("-fx-background-color: linear-gradient(to top, #0f4db8, #10439c);-fx-text-fill:white;-fx-font-weight: bold");
        endGameBtn.setOnAction(e -> endProgram());
        topContainer.getChildren().addAll(endGameBtn);
        gameInput = new TextField();
        gameInput.setPromptText("Enter your command here");
        sideContainer.getChildren().add(gameInput);
        sideContainer.setAlignment(Pos.BOTTOM_CENTER);

        // Initialize the boardContainer Letters & Numbers
        for(int x = 1; x < 16; x++) {
            LetterContainerTop.getChildren().add(Point.renderGridHeader("" + String.valueOf((char) (x + 64)), x, -1));
        }
        for(int x = 1; x < 16; x++) {
            LetterContainerBottom.getChildren().add(Point.renderGridHeader("" + String.valueOf((char) (x + 64)), x, -1));
        }
        for(int y = 0; y < 15; y++) {
            NumberContainerRight.getChildren().add(Point.renderGridHeader("" + (y + 1), -1, y));
        }
        for(int y = 0; y < 15; y++) {
            NumberContainerLeft.getChildren().add(Point.renderGridHeader("" + (y + 1) , -1, y));
        }

        // Initialize the boardGrid Squares
        for(int x = 0; x < 15; x++) {
            for(int y = 0; y < 15; y++) {
                boardGrid.getChildren().add(Scrabble.BOARD.points[x][y]);
            }
        }

        // Initialize the scene with root (main) layout
        scrabbleScene = new Scene(rootLayout, Scrabble.WINDOW_WIDTH, Scrabble.WINDOW_HEIGHT);

        // Set the scene to the scrabble scene
        window.setScene(scrabbleScene);
        //window.setMaximized(true);
        window.show();
    }


    @Override
    public void handle(ActionEvent event) {

    }

    /**
     * gets user confirmation to end game and exit application
     */
    private void endProgram(){
           // boolean result = PopUp.confirmDisplay("Are you sure?", "Do you want to end the game and exit the application?", "Yes", "No");
             boolean result = true;
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