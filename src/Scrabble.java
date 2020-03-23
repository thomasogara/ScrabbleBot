/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Scrabble {
    /*
        CLASS VARIABLES
     */
    /**The default number of players for a game of Scrabble*/
    public static final int PLAYER_COUNT = 2;
    /**The board to be used for the duration of the game*/
    public static final Board BOARD = new Board();
    /**The pool to be used for the duration of the game*/
    public static final Pool POOL = new Pool();
    /**The players participating in this game*/
    public static final Player[] PLAYERS = new Player[Scrabble.PLAYER_COUNT];
    /**The Scanner to be used throughout the program to read from stdin for the duration of the game*/
    public static final Scanner STDIN = new Scanner(System.in);
    /**The dictionary to be used for the game (not yet a requirement)*/
    public static final HashSet<String> DICTIONARY = null;
    /**The banner presented on program startup*/
    public static final String WELCOME_BANNER = createWelcomeBanner();
    /**The number of consecutive scoreless turns that have been played thus far*/
    public static int NUMBER_OF_SCORELESS_TURNS = 0;

    /**
     * Scrabble.main() is the entry-point of the game logic.
     * It controls the game progression for the lifetime of the game.
     * @param args command-line arguments passed by the caller (ignored)
     */
    public static void main(String[] args){
        System.out.print(Scrabble.WELCOME_BANNER);
        initPlayers(Scrabble.PLAYERS);
        PLAYERS[0].getFrame().setPool(Scrabble.POOL);
        PLAYERS[1].getFrame().setPool(Scrabble.POOL);
        for(int i = 0; i < Scrabble.PLAYERS.length; i++){
            System.out.println("Welcome to scrabble, player " + (i + 1) + ", who has decided upon the username: " + Scrabble.PLAYERS[i].getUsername());
        }
        System.out.println();
        while(Scrabble.NUMBER_OF_SCORELESS_TURNS < 6 && !Scrabble.POOL.isEmpty()){
            PLAYERS[0].getFrame().refill();
            PLAYERS[1].getFrame().refill();
            for(int i = 0; i < Scrabble.PLAYER_COUNT; i++){
                System.out.println(Scrabble.PLAYERS[i].getUsername() + "'s turn!");
                System.out.println(Scrabble.BOARD);
                System.out.println("Your frame: " + PLAYERS[i].getFrame());
                String input = Scrabble.STDIN.nextLine();

                // TODO implement proper interaction with the UI interface once complete
                // should approximately have a pattern as per below template
                // UI.parseInput(input);;
            }
        }
        System.out.println("The game has almost ended. Now, each player will be invited to challenge the plays");
        System.out.println("of their opponent. ");



        Scrabble.STDIN.close();
    }

    /**
     * Invite a player to challenge the words placed by their opponent. Since checks are manual at the moment,
     * the program will assume that all challenged words which have been played are invalid.
     * @param player the index of the player who is to be invited to challenge the plays of their opponent.
     */
    public void invitePLayerToChallenge(int player){
        int opponent = (player == 0 ? 1 : 0);
        /* MANUAL WORD CHALLENGE SECTION OF THE GAME */
        System.out.println("Congratulations! The game has officially ended. Now, you will be invited to challenge the plays of your opponent.");
        System.out.println(Scrabble.PLAYERS[player].getUsername() + " please enter all the words that your opponent played which you would like to challenge");
        System.out.println("Input the words EITHER:");
        System.out.println("On a single line, with spaces between each word");
        System.out.println("OR");
        System.out.println("Place each word on its own line");
        System.out.println("In both cases, press the enter key twice once you have finished in order to continue!");

        /* Initialise the arraylist to be used to store the words which the players would like to challenge */
        ArrayList<String> challenged_words = new ArrayList<>();

        String line = null;
        while((line = Scrabble.STDIN.nextLine()) != null){
            challenged_words.addAll(Arrays.asList(line.replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ").split(" ")));
        }

        System.out.println("Outcome of " + Scrabble.PLAYERS[0].getUsername() + "'s challenges:");
        for(int i = 0; i < challenged_words.size(); i++){
            String s = challenged_words.get(i);
            int index = -1;
            if(!Scrabble.PLAYERS[opponent].played_words.contains(s)){
                System.out.println(Scrabble.PLAYERS[1].getUsername() + " did not play the word " + s);
            }else{
                while((index = PLAYERS[opponent].played_words.indexOf(s)) != -1) {
                    int score = PLAYERS[opponent].scores_from_play.get(index);
                    System.out.println(s + " was succesffully challenged, removing " + score + " points from " + Scrabble.PLAYERS[1].getUsername());
                    Scrabble.PLAYERS[opponent].increaseScore((-1) * score);
                    Scrabble.PLAYERS[opponent].played_words.remove(index);
                    Scrabble.PLAYERS[opponent].scores_from_play.remove(index);
                }
            }
        }
        /* END OF CHALLENGE SECTION */
    }

    /**
     * calculateScore calculates the score to be awarded to a player for a given move
     * @param s the word played
     * @param p the point at which the first letter of the word was placed
     * @param d the direction in which the word was placed
     * @return the score to be awarded to the player for this move
     */
    private static int calculateScore(String s, Point p, char d){
        Point[] required = Scrabble.BOARD.getRequiredTilesAsPointArray(s, p, d);
        int sum = 0;
        int wordMultiplier = 1;
        for(Point point : required){
            int score = point.getScore();
            switch (point.getBonusType()){
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
    private static void initPlayers(Player[] players){
        for(int i = 0; i < Scrabble.PLAYER_COUNT; i++) {
            players[i] = new Player();
            players[i].setUsername(Scrabble.readUsername(i));
        }
    }

    /**
     * A simple method which will read input from stdin until a valid username is consumed, at which point it
     * returns this valid username.
     * @return String representing the valid username entered on stdin.
     */
    private static String readUsername(int playerNumber){
        String username = "";
        do{
            System.out.println("Please enter the username you would like to set for player " + (playerNumber + 1) + " and then press the enter key");
            username = Scrabble.STDIN.nextLine().replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ");
        }while(username.equals(""));
        return username;
    }

    /**
     * Method to create a HashSet containing all valid words as per the contents of a text file
     * (text file should be located in /assets/words.txt)
     * @return HashSet<String> representing all valid words
     */
    public static HashSet<String> createDictionary(){
        HashSet<String> legalWords = new HashSet<>();
        try {
            File file = new File("assets/dictionary.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                legalWords.add(line);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return legalWords;
    }

    /**
     * Create the static class variable WELCOME_MESSAGE which is contained in the file banner.txt
     * @return String representing the message to be printed to stdout on start-up
     */
    public static String createWelcomeBanner(){
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File("assets/banner.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * A simple method to allow checks to be made to ensure a word is a valid scrabble word,
     * as per the Collins 2019 Scrabble Dictionary
     * @param s the String to be verified
     * @return the validity of the word (true => valid, false => invalid)
     */
    public static boolean isValidWord(String s){
        return Scrabble.DICTIONARY.contains(s.toUpperCase());
    }


}
