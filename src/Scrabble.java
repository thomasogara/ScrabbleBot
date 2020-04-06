/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;

public class Scrabble {
    public static class CommandReturnWrapper {
        public boolean executed;
        public int score;
    }

    /*
        CLASS VARIABLES
     */
    /**
     * The default number of players for a game of Scrabble
     */
    public static final int PLAYER_COUNT = 2;
    /**
     * The board to be used for the duration of the game
     */
    public static final Board BOARD = new Board();
    /**
     * The pool to be used for the duration of the game
     */
    public static final Pool POOL = new Pool();
    /**
     * The players participating in this game
     */
    public static final Player[] PLAYERS = {new Player(), new Player()};
    /**
     * Current player who's turn it is
     */
    public static int CURRENT_PLAYER = 0;
    /**
     * The number of times players have been invited to challenge their opponents plays
     */
    public static int CHALLENGE_COUNT = 0;
    /**
     * The dictionary to be used for the game
     * NOTE: THe dictionary is immutable after creation.
     */
    public static final Set<String> DICTIONARY = Collections.unmodifiableSet(createDictionary());
    /**
     * The player number of the player who placed each word on the BOARD
     */
    public static final ArrayList<Integer> PLAYER_TURNS = new ArrayList<>();
    /**
     * The number of consecutive scoreless turns that have been played thus far
     */
    public static int NUMBER_OF_SCORELESS_TURNS = 0;
    /**
     * The size of the scrabble Window
     */
    public static final int WINDOW_WIDTH = 1066;
    public static final int WINDOW_HEIGHT = 894;
    /**
     * The size of the scrabble Board [MAY SWITCH TO RELATIVE SIZING]
     */
    public static final int BOARD_WIDTH = 900;
    public static final int BOARD_HEIGHT = 600;
    /**
     * The size of the scrabble Point
     */
    public static final int POINT_WIDTH = 45;
    public static final int POINT_HEIGHT = 45;
    /**
     * A reference to the main Application class running so that communication can be achieved
     */
    public static BoardGUI BOARD_GUI;

    /**
     * setup() is used to initialise any necessary variables for the game, it is called prior to the GUI build process
     */
    public static void setup(){
        PLAYERS[0].getFrame().setPool(Scrabble.POOL);
        PLAYERS[1].getFrame().setPool(Scrabble.POOL);
        PLAYERS[0].getFrame().refill();
        PLAYERS[1].getFrame().refill();
    }

    /**
     * endGame() is used to end the game and commence scoring.
     */
    public static void endGame(){
        BOARD_GUI.print("Congratulations! You have completed a game of Scrabble!", true);
        BOARD_GUI.print("The scoring process will now commence", true);
        int[] scores = new int[2];
        Point[][] tilesOnFrames = new Point[2][];
        tilesOnFrames[0] = new Point[PLAYERS[1].getFrame().getLetters().size()];
        tilesOnFrames[1] = new Point[PLAYERS[0].getFrame().getLetters().size()];
        for(int i = 0; i < PLAYERS[0].getFrame().getLetters().size(); i++){
            tilesOnFrames[0][i] = new Point(0, 0);
            tilesOnFrames[0][i].setTile(PLAYERS[1].getFrame().getLetters().get(i));
        }
        for(int i = 0; i < PLAYERS[1].getFrame().getLetters().size(); i++){
            tilesOnFrames[1][i] = new Point(0, 0);
            tilesOnFrames[1][i].setTile(PLAYERS[0].getFrame().getLetters().get(i));
        }

        scores[0] = PLAYERS[0].getScore() + calculateScore(tilesOnFrames[1], null);
        scores[1] = PLAYERS[1].getScore() + calculateScore(tilesOnFrames[0], null);

        BOARD_GUI.print(PLAYERS[0].getUsername() + " has received a score of: " + scores[0], true);
        BOARD_GUI.print(PLAYERS[1].getUsername() + " has received a score of: " + scores[1], true);

        if(scores[0] == scores[1])
            BOARD_GUI.print("This game was a tie", true);
        else if(scores[0] > scores[1])
            BOARD_GUI.print(PLAYERS[0].getUsername() + " has won", true);
        else
            BOARD_GUI.print(PLAYERS[1].getUsername() + " has won", true);
        BOARD_GUI.print("Feel free to close this window now :)", true);
    }

    /**
     * currentPlayer() is a utility method used to obtain a reference to the active player.
     * @return a reference to the player who is currently taking their turn.
     */
    public static Player currentPlayer() {
        return PLAYERS[CURRENT_PLAYER];
    }

    /**
     * alternatePlayer() is a utility method for alternating the current active player after a move has been made.
     */
    public static void alternatePlayer() {
        CURRENT_PLAYER = (CURRENT_PLAYER == 0 ? 1 : 0);
    }

    /**
     * CHALLENGE_HANDLER is an EventHandler which is assigned to the game input once the point has been reached where the
     * game will invite a player to challenge the words placed by their opponent. Since checks are manual at the moment,
     * the program will assume that all challenged words which have been played are invalid.
     */
    public static EventHandler<ActionEvent> CHALLENGE_HANDLER = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            alternatePlayer();
            Player opponent = currentPlayer();
            alternatePlayer();

            /* Initialise the arraylist to be used to store the words which the players would like to challenge */

            String line = BOARD_GUI.read();
            BOARD_GUI.print("> " + line, false);
            ArrayList<String> challenged_words = new ArrayList<>(Arrays.asList(line.replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ").split(" ")));

            BOARD_GUI.print("Outcome of " + currentPlayer().getUsername() + "'s challenges:", true);
            for (int i = 0; i < challenged_words.size(); i++) {
                String s = challenged_words.get(i);
                int index = -1;
                if (!opponent.played_words.contains(s) && !s.equals("")) {
                    BOARD_GUI.print(opponent.getUsername() + " did not play the word " + s, true);
                } else {
                    while ((index = opponent.played_words.indexOf(s)) != -1) {
                        int score = opponent.scores_from_play.get(index);
                        BOARD_GUI.print(s + " was successfully challenged, removing " + score + " points from " + opponent.getUsername(), true);
                        opponent.increaseScore((-1) * score);
                        opponent.played_words.remove(index);
                        opponent.scores_from_play.remove(index);
                    }
                }
            }

            if(CHALLENGE_COUNT > 0){
                BOARD_GUI.setInputHandler(null);
                Scrabble.endGame();
                return;
            }

            CHALLENGE_COUNT++;
            alternatePlayer();
            printChallengeMessage();
        }
    };

    /**
     * GAME_HANDLER is an EventHandler which is assigned to the game input during the normal course of the game.
     * It will monitor the game input, and once a player has issued a command, it will attempt to run it.
     * Should the command fail, the user is invited to retry.
     */
    public static final EventHandler<ActionEvent> GAME_HANDLER = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            String text = BOARD_GUI.read();
            BOARD_GUI.print("> " + text, false);
            CommandReturnWrapper returnWrapper;
            if(!(returnWrapper = BoardGUI.execute(text, currentPlayer())).executed){
                BOARD_GUI.print("Unfortunately that command failed, please try again :(", true);
            }else{
                // assess whether 6 consecutive scoreless turns have occured (this means the game is over)
                if(returnWrapper.score == 0) NUMBER_OF_SCORELESS_TURNS++;
                else if(returnWrapper.score > 0) NUMBER_OF_SCORELESS_TURNS = 0;

                if(NUMBER_OF_SCORELESS_TURNS >= 6){
                    alternatePlayer();
                    BOARD_GUI.setInputHandler(null);
                    endGame();
                }else{
                    // if the help, name, or challenge command was called, offer another turn
                    if(returnWrapper.score == -1){
                        BOARD_GUI.print("It is now " + currentPlayer().getUsername() + "'s turn.", true);
                        BOARD_GUI.print(currentPlayer().getUsername() + "'s frame: " + currentPlayer().getFrame(), true);
                        return;
                    }

                    currentPlayer().increaseScore(returnWrapper.score);
                    currentPlayer().getFrame().refill();
                    PLAYER_TURNS.add(CURRENT_PLAYER);
                    alternatePlayer();

                    BOARD_GUI.print("It is now " + currentPlayer().getUsername() + "'s turn.", true);
                    BOARD_GUI.print(currentPlayer().getUsername() + "'s frame: " + currentPlayer().getFrame(), true);
                }
            }
        }
    };

    /**
     * printChallengeMessage() is a simple method used to print a pre-defined String which is used as the indication of completion of the game
     * and the commencement of the challenge and scoring processes.
     */
    public static void printChallengeMessage() {
        BOARD_GUI.print("Congratulations! The game has officially ended. Now, you will be invited to challenge the plays of your opponent.", true);
        BOARD_GUI.print(currentPlayer().getUsername() + " please enter all the words that your opponent played which you would like to challenge", true);
        BOARD_GUI.print("Place the words on a single line, with spaces between each word.\nHit enter once you have finished inputting words", true);
    }

    /**
     * calculateScore calculates the score to be awarded to a player for a given move
     * @param required the array of points placed on the board
     * @return the score to be awarded to the player for this move
     */
    public static int calculateScore(Point[] required, ArrayList<String> formed_words) {
        int sum = 0;
        int wordMultiplier = 1;
        System.out.println(formed_words);
        for (Point point : required) {
            int score = point.getScore();
            switch (point.getBonusType()) {
                case DW:
                    wordMultiplier *= 2;
                    break;
                case TW:
                    wordMultiplier *= 3;
                    break;
                case DL:
                    score *= 2;
                    break;
                case TL:
                    score *= 3;
                    break;
                default:
                    break;
            }
            sum += score;
        }
        return sum * wordMultiplier;
    }

    /**
     * A simple method allowing for an array of Players to have its members initialised, and for all
     * players in that array to have valid usernames set, these usernames being read from stdin.
     * @param players the array of players whose members are to be initialised.
     */
    private static void initPlayers(Player[] players) {
        for (int i = 0; i < Scrabble.PLAYER_COUNT; i++) {
            players[i] = new Player();
        }
        BOARD_GUI.setInputHandler(USERNAME_HANDLER);
    }

    /**
     * A simple method which will read input from stdin until a valid username is consumed, at which point it
     * returns this valid username.
     * @return String representing the valid username entered on stdin.
     */
    public static EventHandler<ActionEvent> USERNAME_HANDLER = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            String text = BOARD_GUI.read();
            BOARD_GUI.print("> " + text, false);
            if (Scrabble.offerUsername(text)) {
                BOARD_GUI.print("Player " + (CURRENT_PLAYER + 1) + " has been assigned the username: " + currentPlayer().getUsername(), true);
                alternatePlayer();
                if (CURRENT_PLAYER == 0) {
                    BOARD_GUI.print("The game will now begin. " + currentPlayer().getUsername() + ", please feel free to input a command", true);
                    BOARD_GUI.print("For assistance, type \"HELP\"", true);
                    BOARD_GUI.print("It is now " + currentPlayer().getUsername() + "'s turn.", true);
                    BOARD_GUI.print(currentPlayer().getUsername() + "'s frame: " + currentPlayer().getFrame(), true);
                    BOARD_GUI.setInputHandler(GAME_HANDLER);
                } else{
                    BOARD_GUI.print("Please enter the username you would like to set for player " + (CURRENT_PLAYER + 1) + " and then press the enter key", true);
                }
            } else {
                BOARD_GUI.print("Unfortunately that didn't work, please try again : )", true);
            }
        }
    };


    /**
     * offerUsername() will attempt to assign the provided username to the current active player. if the username is
     * invalid, it fails. if the username is valid, it succeeds, and the user is assigned with the username provided.
     * @param s the username to be assigned to the current active player
     * @return success/failure of the command.
     */
    public static boolean offerUsername(String s) {
        if (!s.replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ").equals("")) {
            currentPlayer().setUsername(s);
            return true;
        }
        return false;
    }

    /**
     * Method to create a HashSet containing all valid words as per the contents of a text file
     * (text file should be located in /assets/words.txt)
     * [NOT YET A REQUIREMENT]
     * @return HashSet<String> representing all valid words
     */
    public static HashSet<String> createDictionary() {
        HashSet<String> legalWords = new HashSet<>();
        try {
            File file = new File("assets/dictionary.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                legalWords.add(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return legalWords;
    }

    /**
     * A simple method to allow checks to be made to ensure a word is a valid scrabble word,
     * as per the Collins 2019 Scrabble Dictionary
     * @param s the String to be verified
     * @return the validity of the word (true => valid, false => invalid)
     */
    public static boolean isValidWord(String s) {
        return Scrabble.DICTIONARY.contains(s.toUpperCase());
    }


}
