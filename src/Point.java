/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Point {

    // Enum values for bonus types
    public enum BonusType {
        DL, // Double Letter - LETTER played points is worth x2 it's orig. value
        TL, // Triple Letter - LETTER played points is worth x3 it's orig. value
        DW, // Double Word - WORD played is worth x2 it's orig. value
        TW, // Triple Word - WORD played is worth x3 it's orig. value
        NB, // No Bonus - This point is not a bonus square
    }

    public static BonusType[][] BONUS_TYPES;

    // The tile placed on this Point
    private Tile tile;

    // Point coordinates on the Board
    private int x, y;

    // List of words formed by this Point/Tile on the Board
    private ArrayList<String> formedWords;

    // The original/first word formed by this Point/Tile on the Board
    private String originWord;

    // Stores an object reference of the games board
    private Board board;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.formedWords = new ArrayList<>();
    }

    /**
     * Alternative constructor for Point class to set the Board instance too
     */
    public Point(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.formedWords = new ArrayList<>();
    }

    /**
     * A method allowing for the score associated with a given Point to be calculated.
     * @return the score associated with this Point.
     */
    public int getScore(){
        return 0;
    }

    /**
     * Get the tile belong to this point
     * @return The tile placed on this point
     */
    public Tile getTile() {
        return this.tile;
    }

    /**
     * Set the tile of this point
     * @param tile - The tile instance to set the class instance of tile as
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Gets the bonus type of this point
     * @return The bonus type of this point i.e Double Letter, Triple Letter, etc..
     */
    public BonusType getBonusType() {
        return Point.BONUS_TYPES[this.y][this.x];
    }

    /**
     * Get the X coordinate of this Point
     * @return The integer value X coordinate of this point
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the Y coordinate of this point
     * @return The integer value Y coordinate of this point
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the list of formed words by this point's tile
     * @return ArrayList of words formed by this point's tile
     */
    public ArrayList<String> getFormedWords() {
        this.refreshFormedWords();
        return this.formedWords;
    }

    /**
     * Run traversing algorithm to get a list of formed Words by this Point's Tiles' placement
     */
    public void refreshFormedWords() {

        // Clear the formed words ArrayList and regenerate it again
        if(!this.formedWords.isEmpty())
            this.formedWords.clear();

        // Traverse right
        Point right = this.getRight(); // Get the point beside this current point
        StringBuilder rightWord = new StringBuilder();
        while(right != null && right.isFilled()) { // Make sure that point isn't null & actually has a Tile/Letter on it
            rightWord.append(right.getTile().getValue()); // Append this points' tiles' letter onto the word formed @ the right hand side
            right = right.getRight(); // Go to the next point beside it
        }
        if(rightWord.length() >= 1) { // If there are 1 or more letters adjacent to this point then a new word exists
            rightWord.insert(0, this.getTile().getValue());
            this.formedWords.add(rightWord.toString());
        }

        // ** Same explanation as above for traversal follows in the other traversing directions; left, up & down

        // Traverse left
        Point left = this.getLeft();
        StringBuilder leftWord = new StringBuilder();
        while(left != null && left.isFilled()) {
            leftWord.append(left.getTile().getValue());
            left = left.getLeft();
        }
        if(leftWord.length() >= 1) {
            leftWord.insert(0, this.getTile().getValue());
            this.formedWords.add(leftWord.reverse().toString());
        }

        // Traverse UP
        Point up = this.getUp();
        StringBuilder upWord = new StringBuilder();
        while(up != null && up.isFilled()) {
            upWord.append(up.getTile().getValue());
            up = up.getUp();
        }
        if(upWord.length() >= 1) {
            upWord.insert(0, this.getTile().getValue());
            this.formedWords.add(upWord.reverse().toString());
        }

        // Traverse Down
        Point down = this.getDown();
        StringBuilder downWord = new StringBuilder();
        while(down != null && down.isFilled()) {
            downWord.append(down.getTile().getValue());
            down = down.getDown();
        }
        if(downWord.length() >= 1) {
            downWord.insert(0, this.getTile().getValue());
            this.formedWords.add(downWord.toString());
        }

    }

    /**
     * Get the original/first word formed by this point's tile
     * @return The first word formed by this point's tile when it was placed
     */
    public String getOriginWord() {
        return this.originWord;
    }

    /**
     * Get the board that this point is on
     * @return the board belonging to this point
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Sets the board of this point so it can be referenced
     * @param board - The board instance to set the class instance of board as
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isFilled() {
        return (this.getTile() != null && this.getTile().getValue() != null && this.getTile().getValue() != '\0');
    }

    /**
     * Get the point to the right of this point
     * @return The point instance belong to the point to the right of this point
     */
    public Point getRight() {
        if( x + 1 < 15 )
            return this.board.points[y][x + 1];

        return null;
    }

    /**
     * Get the point to the left of this point
     * @return The point instance belong to the point to the left of this point
     */
    public Point getLeft() {
        if( x - 1 > 0 )
            return this.board.points[y][x - 1];

        return null;
    }

    /**
     * Get the point above this point
     * @return The point instance belong to the point above of this point
     */
    public Point getUp() {
        if( y > 0 )
            return this.board.points[y - 1][x];

        return null;
    }

    /**
     * Get the point below this point
     * @return The point instance belong to the point to below this point
     */
    public Point getDown() {
        if( y + 1 < 15 )
         return this.board.points[y + 1][x];

        return null;
    }

    public static void readBonusFile(){
        try{
            BonusType[][] bonusArray = new BonusType[15][15];
            File file = new File("./assets/bonus.conf");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> lines = new ArrayList<>();
            ArrayList<String[]> split_lines = new ArrayList<>();
            String line = null;
            while((line = br.readLine()) != null){
                lines.add(line);
            }
            for(String s : lines){
                split_lines.add(s.split("_"));
            }
            for(String[] s : split_lines){
                int y = Integer.parseInt(s[1].split(",")[0]);
                int x = Integer.parseInt(s[1].split(",")[1]);
                switch(s[0]){
                    case "DL":
                        bonusArray[y][x] = BonusType.DL;
                        break;
                    case "TL":
                        bonusArray[y][x] = BonusType.TL;
                        break;
                    case "DW":
                        bonusArray[y][x] = BonusType.DW;
                        break;
                    case "TW":
                        bonusArray[y][x] = BonusType.TW;
                        break;
                    default:
                        bonusArray[y][x] = BonusType.NB;
                        break;
                }
            }
            for(int y = 0; y < bonusArray.length; y++){
                for(int x = 0; x < bonusArray[y].length; x++){
                    if(bonusArray[y][x] == null){
                        bonusArray[y][x] = BonusType.NB;
                    }
                }
            }
            Point.BONUS_TYPES = bonusArray;
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
