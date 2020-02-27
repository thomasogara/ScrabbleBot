/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.util.Scanner;

public class Scrabble {
    /*
        CLASS VARIABLES
     */
    /* The default number of players for a game of Scrabble */
    public static final int PLAYER_COUNT = 2;
    public static final Scanner STDIN = new Scanner(System.in);

    public static void main(String[] args){
        Board board = new Board();
        Player[] players = new Player[Scrabble.PLAYER_COUNT];
        welcomeMessage();
        initPlayers(players);
        System.out.println(players[0].getUsername());
        System.out.println(players[1].getUsername());
    }

    /**
     * A simple method allowing for an array of Players to have its members initialised, and for all
     * players in that array to have valid usernames set, these usernames being read from stdin.
     * @param players the array of players whose members are to be initialised.
     */
    private static void initPlayers(Player[] players){
        for(int i = 0; i < PLAYER_COUNT; i++) {
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
            System.out.println("Please enter the username you would like to set for player " + playerNumber + " and then press the enter key");
            username = Scrabble.STDIN.nextLine().replaceAll("(^\\s+)|(\\s+$)", "").replaceAll("\\s+", " ");
        }while(username.equals(""));
        return username;
    }

    /**
     * Basic method to print the welcome message to the screen.
     */
    private static void welcomeMessage(){
        String greeting = "============================== WELCOME TO SCRABBLE ==============================";
        System.out.println(greeting);
    }
}
