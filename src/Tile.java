/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett (?) (?)
  Daniel (?) (?)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

public class Tile{
	private char letter;
	private static final HashSet<Character> validCharacters = Tile.createValidCharactersSet();
	public Tile(char value){
		if(!Tile.validCharacters.contains(value)){
			throw new IllegalArgumentException(String.format("char %c not valid with current character validity constraints", value));
		}
		this.letter = value;
	}
	
	public final char getValue(){
		return this.letter;
	}
	
	public final HashSet<Character> getValidCharSet(){
		return Tile.validCharacters;
	}
	
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
}
