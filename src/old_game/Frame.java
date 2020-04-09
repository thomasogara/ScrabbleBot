package old_game;/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Frame extends HBox {
    /*
        Instance variables
     */
    /** An ArrayList of old_game.Tile's, containing all the tiles which are currently on the frame*/
    private ArrayList<Tile> letters;
    /** A reference to the global old_game.Pool for a given game. */
    private Pool pool;

    /*
        Class variables
     */
    /** An integer constant of the old_game.Frame class, representing the maximum number of old_game.Tile's which can be placed on a old_game.Frame*/
    public static final int FRAME_CAPACITY = 7;

    // GUI variables
    private StackPane frameItems[];
    private Rectangle graphic;
    private Text graphicText;

    /**
     * The default constructor, initialises the 'letters' instance variable, and fills it from the old_game.Pool
     * @see Frame#letters
     */
    public Frame() {
        this.letters = new ArrayList<>();
        this.frameItems = new StackPane[7];
        this.renderGraphic();
    }

    /**
     * Render frame graphic
     */
    public void renderGraphic() {

        this.setSpacing(5);

        for(int i = 0; i < 7; i++) {
            this.frameItems[i] = new StackPane();
            Rectangle graphic  = new Rectangle(Scrabble.POINT_WIDTH, Scrabble.POINT_HEIGHT);
            Text graphicText  = new Text("");
            graphic.setStroke(Color.LIGHTGRAY);
            graphic.setFill(Color.WHITE);
            graphicText.setStyle("-fx-text-fill: black;-fx-fill: white;");
            this.frameItems[i].getChildren().addAll(graphic, graphicText);
            getChildren().add(this.frameItems[i]);
        }

    }

    /**
     * Refresh frame graphic & update the main games frame with the current players' letters
     */
    public void refreshGraphic() {

        Frame f = (Frame) BoardGUI.frameContainer;

        if(f == null) return;
        if(Scrabble.PLAYERS == null ||
                Scrabble.PLAYERS[Scrabble.CURRENT_PLAYER] == null ||
                Scrabble.PLAYERS[Scrabble.CURRENT_PLAYER].getFrame() == null) return;

        int i = 0;

        for(Tile t : Scrabble.PLAYERS[Scrabble.CURRENT_PLAYER].getFrame().letters) {

            StackPane frameItem = (StackPane)((Node) f.getChildren().get(i));
            Rectangle graphic = (Rectangle)((Node)frameItem.getChildren().get(0));
            Text graphicText = (Text)((Node)frameItem.getChildren().get(1));
            graphicText.setStyle("-fx-text-fill: black;-fx-fill: black;-fx-font-size: 200%;-fx-font-weight: bold");

            if(t == null)
                graphicText.setText("");
            else
                graphicText.setText("" + t.getValue());

            i++;

        }

    }

    /**
     * A method which allows the old_game.Frame to be refilled via the old_game.Pool class's API.
     */
    public void refill(){
        this.letters.addAll(this.pool.drawRandTiles(FRAME_CAPACITY - this.letters.size()));
        this.refreshGraphic();
    }

    /**
     * An overloaded method which removes all elements of the argument from the 'letters' instance variable.
     * @see Frame#letters
     * @param tiles List<old_game.Tile> containing a list of Objects to be removed from the old_game.Frame
     */
    public final void removeAll(List<Tile> tiles) {
        for(Tile tile : tiles){
            this.getLetters().remove(tile);
        }
        this.refreshGraphic();
    }

    /**
     * An overloaded method which removes all elements of the argument from the 'letters' instance variable.
     * @param letters_to_be_removed char[] array containing all letters to be removed from the old_game.Frame.
     */
    public final void removeAll(char[] letters_to_be_removed){
        this.removeAll(Arrays.asList(Tile.tileArrayFromCharArray(letters_to_be_removed)));
    }

    /**
     * An overloaded method which removes all elements of the argument from the 'letters' instance variable.
     * @param letters_to_be_removed String containing all letters to be removed from the old_game.Frame
     */
    public final void removeAll(String letters_to_be_removed){
        this.removeAll(Arrays.asList(Tile.tileArrayFromCharArray(letters_to_be_removed.toCharArray())));
    }

    /**
     * A very simple method which allows for a check to be made if a given letter is in the old_game.Frame.
     * @param letter old_game.Tile to be searched for in the old_game.Frame
     * @return boolean representing whether letter is in the old_game.Frame.
     */
    public final boolean hasLetter(Tile letter){
        return this.getLetters().contains(letter);
    }

    /**
     * A method to check if a given letter is present in the old_game.Frame, where the letter is stored as a char.
     * @param letter char to be searched for in the old_game.Frame
     * @return boolean representing whether letter is in the old_game.Frame
     */
    public final boolean hasLetter(char letter) {
        return this.hasLetter(new Tile(letter));
    }

    /**
     * A method to check if a given letter is present in the old_game.Frame, where the letter is stored as a Character.
     * @param letter Character to be searched for in the old_game.Frame
     * @return boolean representing whether letter is in the old_game.Frame.
     */
    public final boolean hasLetter(Character letter){
        return this.hasLetter(new Tile(letter));
    }

    /**
     * A method to check if a given set of letters is present in the old_game.Frame.
     * @param letter_array old_game.Tile[] array containing letters to be searched for in the old_game.Frame
     * @return boolean representing whether ALL letters are in the old_game.Frame.
     */
    public final boolean hasLetters(Tile[] letter_array){
        int blank_required = 0; // how many blank tiles would be needed to compensate for missing letters in the old_game.Frame
        ArrayList<Tile> lettersCopy = new ArrayList<>(this.getLetters());
        for(Tile letter : letter_array){
            if(!lettersCopy.contains(letter))
                blank_required++;
            else
                lettersCopy.remove(letter);
        }
        if(blank_required <= this.blank_count())
            return true;
        return false;
    }

    public final int blank_count(){
        int count = 0;
        for(Tile tile : this.getLetters()){
            if(tile.getValue() == '0')
                count++;
        }
        return count;
    }

    /**
     * A method to check if a given set of letters is present in the old_game.Frame, where the letters are stored as a List<old_game.Tile>
     * @param tiles the old_game.Tile's to be removed from the old_game.Frame
     * @return whether an equivalent old_game.Tile exists in the old_game.Frame for all elements of the input List
     */
    public final boolean hasLetters(List<Tile> tiles){
        return this.hasLetters((Tile []) tiles.toArray());
    }

    /**
     * A method to check if a given set of letters is present in the old_game.Frame, where the letters are stored as a char array
     * @param letter_array the old_game.Tile's to be removed from the Frametiles
     * @return whether an equivalent old_game.Tile exists in the old_game.Frame for all elements of the input List
     */
    public final boolean hasLetters(char[] letter_array){
        return this.hasLetters(Tile.tileArrayFromCharArray(letter_array));
    }


    /**
     * A method to check if a given set of letters is present in the old_game.Frame, where the letters are stored as a
     * String.
     * @param letter_string String containing all letters to be searched for in the old_game.Frame
     */
    public final boolean hasLetters(String letter_string){
        return this.hasLetters(letter_string.replaceAll("\\s+", "").toUpperCase().toCharArray());
    }

    /**
     * A simple method to check whether or not the old_game.Frame is empty
     * @return boolean representing whether the old_game.Frame is empty.
     */
    public final boolean isEmpty(){
        return this.getLetters() == null || this.getLetters().isEmpty();
    }

    /**
     * A method to get all letters in the old_game.Frame as an ArrayList of Character's
     * Note: Changes to the returned ArrayList WILL cause changed to the old_game.Frame. It is NOT a copy.
     * @return a reference to the old_game.Frame's 'letters' instance variable.
     */
    public final ArrayList<Tile> getLetters(){
        return this.letters;
    }

    /**
     * A method to get all the letters in the old_game.Frame as a char[] array
     * Note: Changes to the char[] array returned WILL NOT cause changes to the old_game.Frame.
     * @return the contents of the old_game.Frame, as a char[] array.
     */
    public final char[] getLettersAsCharArray(){
        Character[] lettersToCharacterArray = this.getLettersAsCharacterArray();
        char[] lettersToCharArray = new char[lettersToCharacterArray.length];
        for(int i = 0; i < lettersToCharacterArray.length; i++){
                lettersToCharArray[i] = lettersToCharacterArray[i];
        }
        return lettersToCharArray;
    }

    /**
     * A method to get all the letters in the old_game.Frame as a Character[] array
     * Note: Changes to the Character[] array returned WILL cause changes to the old_game.Frame. The Character objects therein
     * are NOT COPIES.
     * @return the contents of the old_game.Frame, as a Character[] array.
     */
    public final Character[] getLettersAsCharacterArray(){
        Character[] characters = new Character[this.getLetters().size()];
        for(int i = 0; i < this.getLetters().size(); i++){
            characters[i] = this.getLetters().get(i).getValue();
        }
        return characters;
    }

    /**
     * A method to get all the letters in the old_game.Frame as a String (all tokens space-separated)
     * Note: Changes to the String returned WILL NOT cause changes to the old_game.Frame.
     * @return the contents of the old_game.Frame, as a String.
     */
    public final String getLettersAsString(){
        char[] lettersToCharacterArray = this.getLettersAsCharArray();
        StringBuilder lettersStringBuilder = new StringBuilder();
        for(char c : lettersToCharacterArray){
            lettersStringBuilder.append(c);
        }
        return lettersStringBuilder.toString();
    }

    /**
     * A method to allow a single old_game.Tile to be added to the old_game.Frame, if capacity has not been reached
     * @param tile the old_game.Tile to be added to the old_game.Frame
     */
    public final void add(Tile tile) {
        if (!(this.getLetters().size() < Frame.FRAME_CAPACITY)){
            throw new IllegalArgumentException("old_game.Frame is at capacity. Additional old_game.Tile cannot be placed");
        }
        this.getLetters().add(tile);
        this.refreshGraphic();
    }

    /**
     * A method to allow a single char to be added to the old_game.Frame, if capacity has not been reached
     * @param letter char to be added to the old_game.Frame
     */
    public final void add(char letter){
        this.add(new Tile(letter));
    }

    /**
     * A method to allow an array of old_game.Tile's to be added to the old_game.Frame, if the capacity has not been reached, and will
     * not be exceeded if all old_game.Tile's in the Array are added.
     * @param tiles old_game.Tile[] array containging all old_game.Tile's to be added to the old_game.Frame
     */
    public final void addAll(Tile[] tiles) {
        for (Tile tile : tiles) {
            this.add(tile);
        }
    }

    /**
     * A method to allow a String to be added to the old_game.Frame's letters
     */
    public final void addAll(String s){
        this.addAll(Tile.tileArrayFromCharArray(s.toCharArray()));
    }

    /**
     * A very simple method, allowing the 'pool' instance variable of a old_game.Frame to be set.
     * @param pool the old_game.Pool to be assigned as this old_game.Frame's 'pool' variable.
     */
    public final void setPool(Pool pool){
        this.pool = pool;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Tile tile : letters){
            sb.append(tile.getValue()).append(", ");
        }
        if(sb.length() > 1)
            sb.delete(sb.length() - 2, sb.length());
        sb.append(']');
        return sb.toString();
    }
}
