/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett (?) (?)
  Daniel (?) (?)
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Frame{
    private ArrayList<Tile> letters;
    private Pool pool;
    public static final int FRAME_CAPACITY = 7;

    /**
     * The default constructor, initialises the 'letters' instance variable
     * @see Frame#letters
     */
    public Frame(){
        this.letters = new ArrayList<Tile>();
        this.refill();
    }

    /**
     * A method which allows the Frame to be refilled via the Pool class's API.
     */
    public void refill(){
        // TODO: Implement Pool API interaction
    }

    /**
     * A basic console rendering method. Outputs the current state of the Frame to the console as a Character[] array.
     */
    public void consoleRender(){
        System.out.println(Arrays.toString(this.getLetters().toArray()));
    }

    public void render(){
        // TODO: Assignment 3, JavaFX / GUI implementation
        /*
             Some javafx code to render the Frame on screen
         */
    }

    /**
     * An overloaded method which removes all elements of the argument from the 'letters' instance variable.
     * @see Frame#letters
     * @param tiles List<Tile> containing a list of Objects to be removed from the Frame
     */
    public final void removeAll(List<Tile> tiles){
        this.getLetters().removeAll(tiles);
    }

    /**
     * @param letters_to_be_removed char[] array containing all letters to be removed from the Frame.
     */
    public final void removeAll(char[] letters_to_be_removed){
        this.removeAll(Arrays.asList(Tile.tileArrayFromCharArray(letters_to_be_removed)));
    }

    /**
     * @param letters_to_be_removed String containing all letters to be removed from the Frame
     */
    public final void removeAll(String letters_to_be_removed){
        this.removeAll(Arrays.asList(Tile.tileArrayFromCharArray(letters_to_be_removed.toCharArray())));
    }

    /**
     * A very simple method which allows for a check to be made if a given letter is in the Frame.
     * @param letter Tile to be searched for in the Frame
     * @return boolean representing whether or not letter is in the Frame.
     */
    public final boolean hasLetter(Tile letter){
        return this.getLetters().contains(letter);
    }

    /**
     * @param letter char to be searched for in the Frame
     * @return boolean representing whether or not letter is in the Frame
     */
    public final boolean hasLetter(char letter) {
        return this.hasLetter(new Tile(letter));
    }

    /**
     * @param letter Character to be searched for in the Frame
     * @return boolean representing whether or not letter is in the Frame.
     */
    public final boolean hasLetter(Character letter){
        return this.hasLetter(new Tile(letter));
    }

    /**
     * A method to check if a given set of letters is present in the Frame.
     * @param letter_array Tile[] array containing letters to be searched for in the Frame
     * @return boolean representing whether or not ALL letters are in the Frame.
     */
    public final boolean hasLetters(Tile[] letter_array){
        for(Tile letter : letter_array){
            if(!this.getLetters().contains(letter))
                return false;
        }
        return true;
    }

    /**
     * @param tiles the Tile's to be removed from the Frame
     * @return
     */
    public final boolean hasLetters(List<Tile> tiles){
        return this.getLetters().containsAll(tiles);
    }

    public final boolean hasLetters(char[] letter_array){
        return this.hasLetters(Tile.tileArrayFromCharArray(letter_array));
    }


    /**
     * @param letter_string String containing all letters to be searched for in the Frame
     */
    public final boolean hasLetters(String letter_string){
        return this.hasLetters(letter_string.replaceAll("\\s+", "").toUpperCase().toCharArray());
    }

    /**
     * A simple method to check whether or not the Frame is empty
     * @return boolean representing whether the Frame is empty.
     */
    public final boolean isEmpty(){
        return this.getLetters() == null || this.getLetters().isEmpty();
    }

    /**
     * A method to get all letters in the Frame as an ArrayList of Character's
     * Note: Changes to the returned ArrayList WILL cause changed to the Frame. It is NOT a copy.
     * @return a reference to the Frame's 'letters' instance variable.
     */
    public final ArrayList<Tile> getLetters(){
        return this.letters;
    }

    /**
     * A method to get all the letters in the Frame as a char[] array
     * Note: Changes to the char[] array returned WILL NOT cause changes to the Frame.
     * @return the contents of the Frame, as a char[] array.
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
     * A method to get all the letters in the Frame as a Character[] array
     * Note: Changes to the Character[] array returned WILL cause changes to the Frame. The Character objects therein
     * are NOT COPIES.
     * @return the contents of the Frame, as a Character[] array.
     */
    public final Character[] getLettersAsCharacterArray(){
        Character[] characters = new Character[this.getLetters().size()];
        for(int i = 0; i < this.getLetters().size(); i++){
            characters[i] = this.getLetters().get(i).getValue();
        }
        return characters;
    }

    /**
     * A method to get all the letters in the Frame as a String (all tokens space-separated)
     * Note: Changes to the String returned WILL NOT cause changes to the Frame.
     * @return the contents of the Frame, as a String.
     */
    public final String getLettersAsString(){
        char[] lettersToCharacterArray = this.getLettersAsCharArray();
        StringBuilder lettersStringBuilder = new StringBuilder();
        for(char c : lettersToCharacterArray){
            lettersStringBuilder.append(c).append(" ");
        }
        lettersStringBuilder.deleteCharAt(lettersStringBuilder.length() - 1); // remove final space character ' ' in StringBuilder
        return lettersStringBuilder.toString();
    }

    /**
     * A method to allow a single Tile to be added to the Frame, if capacity has not been reached
     * @param tile the Tile to be added to the Frame
     * @return whether the Tile was successfully added to the Frame
     */
    public final boolean add(Tile tile) {
        if (!(this.getLetters().size() < Frame.FRAME_CAPACITY)){
            return false;
        }
        this.letters.add(tile);
        return true;
    }

    /**
     * A method to allow a single char to be added to the Frame, if capacity has not been reached
     * @param letter char to be added to the Frame
     * @return whether the letter was successfully added to the Frame
     */
    public final boolean add(char letter){
        return this.add(new Tile(letter));
    }

    /**
     * A method to allow an array of Tile's to be added to the Frame, if the capacity has not been reached, and will
     * not be exceeded if all Tile's in the Array are added.
     * @param tiles Tile[] array containging all Tile's to be added to the Frame
     * @return boolean value representing whether or not all Tiles were successfully added to the Frame
     */
    public final boolean addAll(Tile[] tiles) {
        if (!(this.letters.size() + tiles.length <= Frame.FRAME_CAPACITY)) {
            return false;
        }
        for (Tile tile : tiles) {
            this.add(tile);
        }
        return true;
    }
    
}
