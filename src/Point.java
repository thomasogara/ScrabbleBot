/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Point extends StackPane {

    // Enum values for bonus types
    public enum BonusType {
        DL, // Double Letter - LETTER played points is worth x2 it's orig. value
        TL, // Triple Letter - LETTER played points is worth x3 it's orig. value
        DW, // Double Word - WORD played is worth x2 it's orig. value
        TW, // Triple Word - WORD played is worth x3 it's orig. value
        NB, // No Bonus - This point is not a bonus square
    }

    // Initialise the static class variable BONUS_TYPES[][] according to the contents of the config file
    public static BonusType[][] BONUS_TYPES = readBonusFile();

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

    // GUI variables
    private Rectangle graphic;
    private Text graphicText;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.formedWords = new ArrayList<>();
        this.renderGraphic();
    }

    /**
     * Alternative constructor for Point class to set the Board instance too
     */
    public Point(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.formedWords = new ArrayList<>();
        this.renderGraphic();
    }

    public void renderGraphic() {
        this.graphic = new Rectangle(Scrabble.POINT_WIDTH, Scrabble.POINT_HEIGHT);
        this.graphic.setStroke(Color.LIGHTGRAY);
        switch(this.getBonusType()) {
            case DL:
                this.graphicText = new Text("2L");
                this.graphic.setFill(Color.web("#ebca67"));
                break;
            case TL:
                this.graphicText = new Text("3L");
                this.graphic.setFill(Color.web("#4a9c3b"));
                break;
            case DW:
                this.graphicText = new Text("2W");
                this.graphic.setFill(Color.web("#6699e0"));
                break;
            case TW:
                this.graphicText = new Text("3W");
                this.graphic.setFill(Color.web("#e8486b"));
                break;
            case NB:
                this.graphicText = new Text("S");
                this.graphic.setFill(Color.web("white"));
                break;
        }

        this.graphicText.setStyle("-fx-text-fill: white;-fx-fill: white;");


        // If tile is the center tile place star
        if(this.x == 7 && this.y == 7) {
            this.graphicText = new Text("★");
            this.graphicText.setStyle("-fx-text-fill: black;-fx-fill: black;-fx-font-size: 200%;-fx-font-weight: bold");
        }

        getChildren().addAll(this.graphic, this.graphicText);
        setTranslateX(this.x * Scrabble.POINT_WIDTH);
        setTranslateY(this.y * Scrabble.POINT_HEIGHT);

        this.refreshGraphic();
    }

    /**
     * Refresh the current state of this point (i.e placed tile, removed tile etc..), and update the points' graphic accordingly
     */
    public void refreshGraphic() {

        if(BoardGUI.boardGrid == null || BoardGUI.boardGrid.getChildren() == null || BoardGUI.boardGrid.getChildren().size() <= 0)
            return;

        /**
         Get the point instance from the boardGrids children using it's index value.
         This index value is found by multiple the X coord by 1 & the Y coord by 15.
         */
        Point pointInstance = (Point) ((Node) BoardGUI.boardGrid.getChildren().get(((this.x * 1) + (this.y * 15))));

        if(pointInstance == null)
            return;

        if(this.tile != null) {
            pointInstance.graphicText.setStyle("-fx-text-fill: black;-fx-fill: black;-fx-font-size: 200%;-fx-font-weight: bold");
            pointInstance.graphicText.setText("" + this.tile.getValue()); // Set the tiles new text to the Tile's character letter
            pointInstance.graphic.setFill(Color.web("#e8e6e4"));
        } else {
            pointInstance.graphicText.setStyle("-fx-text-fill: black;-fx-fill: black;-fx-font-size: 200%;-fx-font-weight: bold");
            pointInstance.graphicText.setText("");
            pointInstance.graphic.setFill(Color.web("#e8e6e4"));
        }
    }

    public static StackPane renderGridHeader(String letter, int x, int y) {

        StackPane gridHeader = new StackPane();

        Rectangle graphic  = new Rectangle(Scrabble.POINT_WIDTH, Scrabble.POINT_HEIGHT);
        Text graphicText  = new Text(letter);
        graphic.setStroke(Color.LIGHTGRAY);
        graphic.setFill(Color.GRAY);
        graphicText.setStyle("-fx-text-fill: black;-fx-fill: white;");
        gridHeader.getChildren().addAll(graphic, graphicText);

        if(x > -1)
            gridHeader.setTranslateX(x * Scrabble.POINT_WIDTH);
        if(y > 0)
            gridHeader.setTranslateY(y * Scrabble.POINT_HEIGHT);

        return gridHeader;

    }

    /**
     * A method allowing for the score associated with a given Point to be calculated (BEFORE ANY SCORE MODIFIERS ARE
     * APPLIED).
     * @return the score associated with this Point BEFORE ANY SCORE MODIFIERS ARE APPLIED.
     */
    public int getScore(){
        return Pool.tileValues.get(this.getTile().getValue());
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
        this.refreshGraphic();
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

    public static BonusType[][] readBonusFile(){
        try{
            // initialise the array of BonusType variables which will be returned by the method
            BonusType[][] bonusArray = new BonusType[15][15];
            // open the file containing the information relating to the bonus types of each file
            File file = new File("./assets/bonus.conf");
            // initialise a BufferedReader which will read the contents of the above opened file
            BufferedReader br = new BufferedReader(new FileReader(file));

            /* begin the file reading process */

            // this arraylist will store all the lines contained within the file
            ArrayList<String> lines = new ArrayList<>();
            // this string will temporarily store the contents of each line of the file while reading
            String line = null;
            // read all lines of the file until EOF is encountered, and append the lines read into the 'lines' arraylist
            while((line = br.readLine()) != null){
                lines.add(line);
            }
            // the file reader can now be closed, and any system resources allocated to it can be freed
            br.close();
            /* the file has now been read into memory */

            // this arraylist will store all the lines contained within the file, split according to the pattern "_"
            ArrayList<String[]> split_lines = new ArrayList<>();
            //iterate through the lines of the file and split them
            for(String s : lines){
                split_lines.add(s.split("_"));
            }

            // iterate through the lines of the file, and populate the bonusArray
            // appropriately according to the contents of each line in the fle
            for(String[] s : split_lines){
                // the x co-ordinate of the square whose bonus is stored on this line of the file
                int y = Integer.parseInt(s[1].split(",")[0]);
                // the y co-ordinate of the square whose bonus is stored on this line of the file
                int x = Integer.parseInt(s[1].split(",")[1]);
                // assign the stored bonus value from the file in the appropriate indices in the bonusArray
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
            // iterate through the bonusArray and set any un-set values to NB (no bonus)
            for(int y = 0; y < bonusArray.length; y++){
                for(int x = 0; x < bonusArray[y].length; x++){
                    if(bonusArray[y][x] == null){
                        bonusArray[y][x] = BonusType.NB;
                    }
                }
            }
            // return the constructed bonusArray
            return bonusArray;
        } catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}
