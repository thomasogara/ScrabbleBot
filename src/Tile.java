/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett (?) (?)
  Daniel (?) (?)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tile{
	/*
		INSTANCE VARIABLES
	 */
	/*The value of the given tile, stored as a Character object*/
	private Character value;

	/*
		CLASS VARIABLES
	 */
	/*The set of all valid Tile values, stored as a HashSet of Character objects*/
	private static final HashSet<Character> validCharacters = Tile.createValidCharactersSet();


	/**
	 * Constructor for the Tile class
	 * Note: By default java13 will allow Character/char implicit conversion. Therefore Tile(<Character>) will result
	 * in a call to this constructor.
	 *
	 * @param value a char representing the value of the tile {'A', 'B', 'C' . . .}
	 */
	public Tile(char value){
		if(!Tile.validCharacters.contains(value)){
			throw new IllegalArgumentException(String.format("char %c not valid with current character validity constraints", value));
		}
		this.value = value;
	}

	/**
	 * Default getter for 'value' instance variable
	 * @return reference to Character object representing the value of this Tile
	 */
	public final Character getValue(){
		return this.value;
	}

	/**
	 * Getter method for the class variable 'validCharacters'
	 * @return a reference to the class variable 'validCharacters', which is a HashSet<Character> representing all
	 * valid value of a Tile object
	 */
	public static HashSet<Character> getValidCharacters(){
		return Tile.validCharacters;
	}

	/**
	 * Method to create a HashSet containing all valid Tile values from a text file (text file should be located
	 * in /assets/charset.conf)
	 * @return HashSet<Character> representing all valid Tile values
	 */
	public static HashSet<Character> createValidCharactersSet(){
		HashSet<Character> legalCharacters = new HashSet<>();
		try {
			File file = new File("assets/charset.conf");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				legalCharacters.add(line.charAt(0));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
		return legalCharacters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tile tile = (Tile) o;
		return this.value.equals(tile.value);
	}

	/**
	 * A method to convert a char[] array to a Tile[] array
	 * @param letters a char[] array to be converted
	 * @return a Tile[] array containing Tile's which have the value of the corresponding char in the letters[] array
	 */
	public static Tile[] tileArrayFromCharArray(char[] letters){
		Tile[] tiles = new Tile[letters.length];
		for(int i = 0; i < letters.length; i++){
			tiles[i] = new Tile(letters[i]);
		}
		return tiles;
	}

	/**
	 * A method to convert a List<Character> to an ArrayList<Tile><
	 * @param letters the List<Character> to be converted
	 * @return the resulting ArrayList<Tile>
	 */
    public static ArrayList<Tile> tileArrayListFromCharacterList(List<Character> letters){
        ArrayList<Tile> tiles = new ArrayList<>();
        for(Character c : letters){
            tiles.add(new Tile(c));
        }
        return tiles;
    }

	@Override
	public String toString() {
		return "Tile{" +
				"value=" + value +
				'}';
	}
}
