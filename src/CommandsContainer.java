/**
 * CommandsContainer is a wrapper class for all the methods associated with the recognised commands in the game
 * All members have a signature matching the run() method in the Command interface
 */

public class CommandsContainer {
    /**
     * exchange() models the behaviour of the EXCHANGE command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper exchange(String[] tokens, Player p){
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();

        // 2 unique pieces of information are required to exchange tiles
        // the command itself (EXCHANGE)
        // the tiles to be exchanged
        // if either of these are missing, the command fails
        if(tokens.length < 2) return returnWrapper;

        // the second token of the command is the word which is to be discarded
        String toBeDiscared = tokens[1];

        // if the player does not have all the tokens which they would like to
        // remove from their frame, the command fails.
        // no partial removals are completed
        if(!p.getFrame().hasLetters(toBeDiscared)) return returnWrapper;

        // if the command parameters are valid, execute the command
        // remove all the desired tiles from the player's frame
        p.getFrame().removeAll(toBeDiscared);
        // refill the player's frame with random tiles from the Pool
        p.getFrame().refill();
        // at this point, the command has succeeded; return true
        returnWrapper.executed = true;
        return returnWrapper;
    }

    /**
     * place() models the behaviour of the PLACE command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper place(String[] tokens, Player p){
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        // 3 unique pieces of information are required to place a tile:
        // a grid references
        // a direction
        // a word
        // if any of these are missing, the command fails
        if(tokens.length < 3) return returnWrapper;

        // the grid reference is always the first token of the command
        String gridref = tokens[0];
        // the direction is the fist character of the second token of the command
        String dir = tokens[1];
        // the word is the third token of the command
        String word = tokens[2];

        // coOrdinates stores the string representation of the x/y co-ordinates
        String[] coOrdinates = new String[2];
        int x = 0, y = 0;

        // a grid reference must have two unique members:
        // a letter, indicating the x position (A-O inclusive)
        // a number, representing the y position (1-15 inclusive)
        if(gridref.length() < 2) return returnWrapper;
        coOrdinates[0] = gridref.substring(0,1);
        coOrdinates[1] = gridref.substring(1);

        // the x co-ordinate must be a single letter in the range (A-O inclusive)
        if(!coOrdinates[0].matches("[A-O]")) return returnWrapper;

        // x has now been thoroughly checked to ensure compliance
        // the Board has integer indices, so the x value must be mapped
        // from its character representation to an integer representation
        x = (int)coOrdinates[0].charAt(0) - 'A';
        // attempt conversion of the y co-ordinate, if it fails, the command fails
        try{
            y = Integer.parseInt(coOrdinates[1]);
        } catch (NumberFormatException e){
            return returnWrapper;
        }
        // y co-ordinate must be between 1 and 15, else command fails
        if(y < 1 || y > 15) return returnWrapper;
        // count from zero, not one
        y--;

        // both 'A' and 'R' will be recognised as representing 'ACROSS' to ensure legacy support
        if(!dir.matches("[ARD]")) return returnWrapper;

        // dir has now been amply checked and so the relevant information can be extracted
        char d = dir.charAt(0);
        // 'A' and 'D' are interpreted as both being synonymous and meaning 'ACROSS'
        if(d == 'A') d = 'R';

        // if the command parameters are valid, execute the command
        // return the result of attempting to add the word to the board
        return Scrabble.BOARD.add(word, new Point(x, y), d, p);
    }

    /**
     * pass() models the behaviour of PASS command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper pass(String[] tokens, Player p){
        // a pass always succeeds, even if it is parameterised
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        returnWrapper.executed = true;
        return returnWrapper;
    }

    /**
     * name() models the behaviour of the NAME command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper name(String[] tokens, Player p){
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        // name succeeds if and only if a name is provided, i.e. tokens.length >= 2
        if(tokens.length < 2) return returnWrapper;

        // if a name has been provided, the name command will assume that all tokens
        // in positions 1 -> tokens.length constitute the name to be set
        // e.g. NAME Onika Tanya Maraj-Petty => name = "Onika Tonya Maraj-Petty"
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < tokens.length; i++){
            // append all tokens in positions 1 -> tokens.length to the
            // String, followed by a space to separate words
            sb.append(tokens[i]).append(' ');
        }
        // Extract the name of the player from the StringBuilder
        String name = sb.toString();

        // set the player's username
        p.setUsername(name);
        // set the score to -1, this means the player does not lose their turn
        returnWrapper.score = -1;
        // set executed to true, as the turn was in fact executed
        returnWrapper.executed = true;
        return returnWrapper;
    }

    /**
     * help() models the behaviour of the HELP command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper help(String[] tokens, Player p){
        // help always succeeds
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        // help is always executed
        returnWrapper.executed = true;
        // set score to -1 so that the player does not lose their turn
        returnWrapper.score = -1;
        // print help messages to the output
        Scrabble.BOARD_GUI.print("QUIT:       (quit game)", true);
        Scrabble.BOARD_GUI.print("PASS:       (pass current move)", true);
        Scrabble.BOARD_GUI.print("EXCHANGE <letters>:     (swaps these letters for new letters)", true);
        Scrabble.BOARD_GUI.print("HELP:       (display this guide)", true);
        Scrabble.BOARD_GUI.print("", true);
        Scrabble.BOARD_GUI.print("How to place a word on the board:", true);
        Scrabble.BOARD_GUI.print("- Starting tile position", true);
        Scrabble.BOARD_GUI.print("- A or D for direction", true);
        Scrabble.BOARD_GUI.print("- WORD", true);
        Scrabble.BOARD_GUI.print("e.g. A3 D HELLO", true);
        return returnWrapper;
    }

    /**
     * quit() models the behaviour of the QUIT command
     * @param tokens the tokens received from the game input
     * @param p the player making the move
     * @return the success/failure of the command as well as the score attributed with the move
     */
    static Scrabble.CommandReturnWrapper quit(String[] tokens, Player p){
        // quit cannot fail, though there is no way to communicate
        // that back to the caller. the 'return returnWrapper;' is really extraneous.
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        returnWrapper.executed = true;
        Scrabble.BOARD_GUI.print("Ending Game... exiting now", true);
        System.exit(0);
        return returnWrapper;
    }
}
