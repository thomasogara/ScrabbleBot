/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett (?) (?)
  Daniel (?) (?)
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Frame{
    private HashSet<Character> letters;
    private Pool pool;
    public static final int FRAME_CAPACITY = 7;

    /**
     * The default constructor, initialises the 'letters' instance variable
     * @see Frame#letters
     */
    public Frame(){
        this.letters = new HashSet<>();
    }

    /**
     * A method which allows the Frame to be refilled via the Pool class's API.
     */
    public void refill(){
        this.letters = new HashSet<>();
        // TODO: Implement Pool API interaction
    }

    /**
     * A basic console rendering method. Outputs the current state of the Frame to the console as a Character[] array.
     */
    public void consoleRender(){
        System.out.println(Arrays.toString(this.letters.toArray()));
    }

    public void render(){
        // TODO: Assignment 3, JavaFX / GUI implementation
        /*
             Some javafx code to render the Frame on screen
         */
    }

    /**
     * An overloaded method which removes all elements of the argument from the 'letters' instance variable.
     *
     * @see Frame#letters
     * @param letters_to_be_removed char[] array containing all letters to be removed from the Frame.
     */
    public final void removeAll(char[] letters_to_be_removed){
        for(char letter : letters_to_be_removed){
            Character letter_as_object = letter;
            if(letters.contains(letter_as_object))
                letters.remove(letter);
            else
                throw new IllegalArgumentException(String.format("cannot remove character %c from Frame as not present", letter));
        }
    }

    /**
     * @param letters_to_be_removed List<Character> containing all letters to be removed from the Frame
     */
    public final void removeAll(List<Character> letters_to_be_removed){
        for(Character letter : letters_to_be_removed){
            if(letters.contains(letter))
                letters.remove(letter);
            else
                throw new IllegalArgumentException(String.format("cannot remove character %c from Frame as not present", letter));
        }
    }

    /**
     * @param letters_to_be_removed String containing all letters to be removed from the Frame
     */
    public final void removeAll(String letters_to_be_removed){
        for(char letter : letters_to_be_removed.toCharArray()){
            Character letter_as_object = letter;
            if(letters.contains(letter_as_object)){
                letters.remove(letter);
            }
            else{
                throw new IllegalArgumentException(String.format("cannot remove character %c from Frame as not present", letter));
            }
        }
    }

    /**
     * A very simple method which allows for a check to be made if a given letter is in the Frame.
     * @param letter char to be searched for in the Frame
     * @return boolean representing whether or not letter is in the Frame.
     */
    public boolean hasLetter(char letter){
        Character letter_as_object = letter;
        return letters.contains(letter_as_object);
    }

    /**
     * A method to check if a given set of letters is present in the Frame.
     * @param letter_array char[] array containing letters to be searched for in the Frame
     * @return boolean representing whether or not ALL letters are in the Frame.
     */
    public boolean hasLetters(char[] letter_array){
        for(char letter : letter_array){
            Character letter_as_object = letter;
            if(!letters.contains(letter_as_object))
                return false;
        }
        return true;
    }

    /**
     * @param letter_list List<Character> containing all letters to be searched for in the Frame
     */
    public boolean hasLetters(List<Character> letter_list){
        for(Character letter : letter_list){
            if(!letters.contains(letter))
                return false;
        }
        return true;
    }

    /**
     * @param letter_string String containing all letters to be searched for in the Frame
     */
    public boolean hasLetters(String letter_string){
        for(char letter : letter_string.toCharArray()){
            Character letter_as_object = letter;
            if(!letters.contains(letter_as_object))
                return false;
        }
        return true;
    }

    /**
     * A simple method to check whether or not the Frame is empty
     * @return boolean representing whether the Frame is empty.
     */
    public boolean isEmpty(){
        return letters == null || letters.isEmpty();
    }

    /**
     * A method to get all letters in the Frame as a HashSet of Character's
     * Note: Changes to the returned HashSet WILL cause changed to the Frame. It is NOT a copy.
     * @return a reference to the Frame's 'letters' instance variable.
     */
    public HashSet<Character> getLetters(){
        return this.letters;
    }

    /**
     * A method to get all the letters in the Frame as a char[] array
     * Note: Changes to the char[] array returned WILL NOT cause changes to the Frame.
     * @return the contents of the Frame, as a char[] array.
     */
    public char[] getLettersAsCharArray(){
        int letter_count = this.letters.size();
        Character[] lettersToCharacterArray = (Character[]) this.letters.toArray();
        char[] lettersToCharArray = new char[letter_count];
        for(int i = 0; i < letter_count; i++){
                lettersToCharArray[i] = lettersToCharacterArray[i];
        }
        return lettersToCharArray;
    }

    /**
     * A method to get all the letters in the Frame as a char[] array
     * Note: Changes to the String returned WILL NOT cause changes to the Frame.
     * @return the contents of the Frame, as a String.
     */
    public String getLettersAsString(){
        int letter_count = this.letters.size();
        Character[] lettersToCharacterArray = (Character[]) this.letters.toArray();
        StringBuilder lettersStringBuilder = new StringBuilder();
        for(char c : lettersToCharacterArray){
            lettersStringBuilder.append(c);
        }
        return lettersStringBuilder.toString();
    }

    /**
     * A method to get all the letters in the Frame[] as a List<Character>
     * Note: Changes to the List<Character> returned WILL NOT cause changes to the Frame.
     * @return the contents of the Frame, as a List<Character>
     */
    public List<Character> getLettersAsCharacterList(){
        int letter_count = this.letters.size();
        Character[] lettersToCharacterArray = (Character[]) this.letters.toArray();
        ArrayList<Character> lettersToCharacterList = new ArrayList<>();
        lettersToCharacterList.ensureCapacity(letter_count);
        lettersToCharacterList.addAll(Arrays.asList(lettersToCharacterArray));
        return lettersToCharacterList;
    }
}
