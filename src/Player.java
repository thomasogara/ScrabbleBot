/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */


import java.util.ArrayList;

public class Player {
    // instance variables for username, score and player's frame (tiles)
    private String username = "";
    private int score = 0; // holds score for user
    private Frame frame;
    public ArrayList<String> played_words = new ArrayList<>(); // holds the words played successfully by this player
    public ArrayList<Point[]> played_points = new ArrayList<>(); // holds the points played successfully by this player
    public ArrayList<Integer> scores_from_play = new ArrayList<>(); // holds the scores attained for each play by this player

    // default constructor for Player object
    public Player() {
        this.frame = new Frame();
    }

    // constructor with just name argtiles
    public Player(String name) {
        this.username = name;
        this.frame = new Frame();
    }

    // reset player data method
    public void resetUser(){
       this.username = "";
       this.score = 0;
    }

    // set player's name method
    public void setUsername(String name){
        this.username = name;
    }

    // get player's name method
    public String getUsername(){
        return this.username;
    }

    // set user score
    public void setScore(int value){
       this.score = value;
    }

    // increase score method - takes an int as argument as adds it to the player's score
    public void increaseScore(int a) {
        this.score = this.score + a;
    }

    // access player score
    public int getScore() {
        return this.score;
    }

    // access players tiles
    public Frame getFrame() {
        return this.frame;
    }

    // displays username when called
    public String displayUsername() {
        return this.username;
    }
}