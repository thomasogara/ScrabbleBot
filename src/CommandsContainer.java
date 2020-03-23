/**
 * CommandsContainer is a wrapper class for all the methods associated with the recognised commands in the game
 * All members have a signature matching that of the run() method in the Command interface
 */

public class CommandsContainer {
    static boolean exchange(String[] tokens, Player p){
        // 2 unique pieces of information are required to exchange tiles
        // the command itself (EXCHANGE)
        // the tiles to be exchanged
        // if either of these are missing, the command fails
        if(tokens.length < 2) return false;

        // the second token of the command is the word which is to be discarded
        String toBeDiscared = tokens[1];

        // if the player does not have all the tokens which they would like to
        // remove from their frame, the command fails.
        // no partial removals are completed
        if(!p.getFrame().hasLetters(toBeDiscared)) return false;

        // if the command parameters are valid, execute the command
        // remove all the desired tiles from the player's frame
        p.getFrame().removeAll(toBeDiscared);
        // refill the player's frame with random tiles from the Pool
        p.getFrame().refill();
        // at this point, the command has succeeded; return true
        return true;
    }

    static boolean place(String[] tokens, Player p){
        // 3 unique pieces of information are required to place a tile:
        // a grid references
        // a direction
        // a word
        // if any of these are missing, the command fails
        if(tokens.length < 3) return false;

        // the grid reference is always the first token of the command
        String gridref = tokens[0];
        // the direction is the fist character of the second token of the command
        String dir = tokens[1];
        // the word is the third token of the command
        String word = tokens[2];

        // coOrdinates stores the string representation of the x/y co-ordinates
        String[] coOrdinates;
        int x = 0, y = 0;

        // a grid reference must have two unique members:
        // a letter, indicating the x position (A-O inclusive)
        // a number, representing the y position (1-15 inclusive)
        if((coOrdinates = gridref.toUpperCase().split("[A-O]")).length > 2) return false;

        // the x co-ordinate must be a single letter in the range (A-O inclusive)
        if(!coOrdinates[0].matches("[A-O]")) return false;

        // x has now been thoroughly checked to ensure compliance
        // the Board has integer indices, so the x value must be mapped
        // from its character representation to an integer representation
        x = (int)coOrdinates[0].charAt(0) - 'A';
        // attempt conversion of the y co-ordinate, if it fails, the command fails
        try{
            y = Integer.parseInt(coOrdinates[1]);
        } catch (NumberFormatException e){
            return false;
        }
        // y co-ordinate must be between 1 and 15, else command fails
        if(y < 1 || y > 15) return false;
        // count from zero, not one
        y--;

        // both 'A' and 'R' will be recognised as representing 'ACROSS' to ensure legacy support
        if(!dir.matches("[ARD]")) return false;

        // dir has now been amply checked and so the relevant information can be extracted
        char d = dir.charAt(0);
        // 'A' and 'D' are interpreted as both being synonymous and meaning 'ACROSS'
        if(d == 'A') d = 'R';

        // if the command parameters are valid, execute the command
        // return the result of attempting to add the word to the board
        return Scrabble.BOARD.add(word, new Point(x, y), d, p);
    }

    static boolean pass(String[] tokens, Player p){
        // a pass always succeeds, even if it is parameterised
        return true;
    }

    static boolean help(String[] tokens, Player p){
        // help always succeeds
        return true;
    }

    static boolean quit(String[] tokens, Player p){
        // quit cannot fail, though there is no way to communicate
        // that back to the caller. the 'return true;' is really extraneous.
        return true;
    }
}
