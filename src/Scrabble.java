/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class Scrabble {
    /*
        CLASS VARIABLES
     */
    /* The default number of players for a game of Scrabble */
    public static final int PLAYER_COUNT = 2;
    public static final Board BOARD = new Board();
    public static final Pool POOL = new Pool();
    public static final Player[] PLAYERS = new Player[Scrabble.PLAYER_COUNT];
    public static final Scanner STDIN = new Scanner(System.in);
    public static final HashSet<String> DICTIONARY = createDictionary();
    public static final String WELCOME_MESSAGE = createWelcomeMessage();
    public static int NUMBER_OF_SCORELESS_TURNS = 0;

    public static void main(String[] args){
        System.out.print(Scrabble.WELCOME_MESSAGE);
        initPlayers(Scrabble.PLAYERS);
        for(int i = 0; i < Scrabble.PLAYERS.length; i++){
            System.out.println("Welcome to scrabble, player " + (i + 1) + ", who has decided upon the username: " + Scrabble.PLAYERS[i].getUsername());
        }
        System.out.println();
        while(Scrabble.NUMBER_OF_SCORELESS_TURNS < 6 && !Scrabble.POOL.isEmpty()){
            for(int i = 0; i < Scrabble.PLAYER_COUNT; i++){
                String command = Scrabble.STDIN.nextLine();
                // TODO implement proper interaction with the UI interface once complete
                // should approximately have a pattern as per below template
                // UI.parseInput(command);
            }
        }
        Scrabble.STDIN.close();
    }

    private static int calculateScore(String s, Point p, char d, Player u ){
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
        return sum;
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
     * Method to create a HashSet containing all valid word values from a text file (text file should be located
     * in /assets/words.txt)
     * @return HashSet<String> representing all valid word values
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
    public static String createWelcomeMessage(){
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
