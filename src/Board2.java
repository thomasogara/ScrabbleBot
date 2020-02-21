import java.util.ArrayList;

public class Board {
    private int occupiedTileCount;
    private boolean inProgress;
    private final ArrayList<Point> buffer = new ArrayList<>();

    private final void endTurn(){
        inProgress = !inProgress;
    }

    public Point[][] points;

    public Board() {
        this.initPoints();
    }

    /**
     * Initiate the points grid of the board, 15x15 size.
     */
    public void initPoints() {
        this.points = new Point[15][15];
        for(int x = 0; x < 15; x++) {
            for(int y = 0; y < 15; y++) {
                points[y][x] = new Point(x, y, this);
            }
        }
    }

    /**
     * Allows the board to be reset
     */
    public void resetBoard(){
        for(int x=0; x < 15; x++){
            for(int y=0; y < 15; y++){
                points[y][x].setTile(null);
            }
        }
    }

    /**
     * Allows board to be displayed using ASCII characters via console
     */
    public void displayBoard(){

    }

    /**
     * An alternative to placePoint() which instead accepts a Tile as a parameter, as well as two integers x and y
     * representing the chosen square on the board. It attempts to place a given Tile onto this board at the given
     * position and returns a boolean indicating success/failure.
     * @param t The Tile to be placed on this board
     * @param x The x co-ordinate of the chosen square of the board
     * @param y The y co-ordinate of the chosen square of the board
     * @return
     */
    public final boolean add(Tile t, int x, int y) {
        Point p = new Point(x, y);
        // check if tile is empty, if yes - check if player's rack contains tile - if yes place tile, else false
        //                         if not - check if tiles match, if yes - check if player's rack has tile - if yes - place tile else false
        if (this.points[y][x].getTile() == null) {

            //           for (int i = 0; i < PLAYER FRAME LENGTH; i++)
            //         {
            //           if (player frame contains tile){
            p.setBoard(this);
            p.setTile(t);
            return true;
            //         }
            //       else{ return false; }
            // }
        }
        else if(this.points[y][x].getTile().getValue() == t.getValue()) {
            //  for (int i = 0; i < PLAYER FRAME LENGTH; i++){
            //    if (player frame contains tile){
            p.setBoard(this);
            p.setTile(t);
            return true;
            //  }
            //  }
            //     else{ return false; }
        }
        else{ return false; }


    }


    /**
     * Implementation of the add method - this takes in values for the word, starintg co-ords, word direction
     * @param p Point encapsulating the x/y position where the first character of the String should be placed
     * @param d Character indicating the direction the word should be placed in ('D' => Down, 'R' => Right)
     */
    public final boolean add(String s, Point p, char d){
        char[] chars = s.toUpperCase().toCharArray(); // converts string to char array
        Point[] wordPoints = new Point[s.length()]; //  creates point array for each point of word

        switch (d){
            case 'R':
                for(int z=0; z < s.length(); z++){
                    int x = p.getX() + z;
                    int y = p.getY();
                    wordPoints[z] = new Point(x, y);
                }
                if(this.isValidWordPoints(wordPoints)) { // true if word is valid
                    for(int i = 0; i < s.length(); i++) {
                        char c = chars[i];
                        int x = p.getX() + i;
                        int y = p.getY();
                        this.add(new Tile(c), x, y);
                    }
                    return true;
                }
                else{ return false; }
                break;
            case 'D':
                for(int z=0; z < s.length(); z++){
                    int x = p.getX();
                    int y = p.getY() + z;
                    wordPoints[z] = new Point(x, y);
                }
                if(this.isValidWordPoints(wordPoints)) { // true if word is valid
                    for(int i = 0; i < s.length(); i++) {
                        char c = chars[i];
                        int x = p.getX() + i;
                        int y = p.getY();
                        this.add(new Tile(c), x, y);
                    }
                    return true;
                }
                else{ return false; }
                break;
            default:
                return false;
        }
        return true;
    }


    /**
     * Ensures that a word placement is valid by checking that it connects to another tile and that i
     * @param wordPoints array to hold all points of board that a word covers
     * @return boolean representing whether or not the placement of the word is valid
     */
    public final boolean isValidWordPoints(Point[] wordPoints) {
        if (this.getOccupiedTileCount() == 0) { // if board is empty
            return isCentered(wordPoints) && inRange(wordPoints);
        }
        else {
            return inRange( && isTouching(wordPoints);
        }

        for(int c=0; c < wordPoints.length; c++){
            if(this.poinxts[wordPoints[c].getY()][wordPoints[c].getX()].isFilled()){ return true; } // at least one co-ord is already filled
        }

        return false;

    }

    /**
     * Ensures word goes through centre if it is the first word on the board
     * @param wordPoints array to hold all points of board that a word covers
     * @return boolean representing whether or not word goes through centre
     */
    public final boolean isCentered(Point[] wordPoints){
        for (int i = 0; i < wordPoints.length; i++) {
            if ((wordPoints[i].getX() == 6) && (wordPoints[i].getY() == 6)) { return true; }
        }
        return false;
    }

    /**
     * Ensures the point is on the board (within range)
     * @param wordPoints array to hold all points of board that a word covers
     * @return boolean representing whether or not word is on boat
     */
    public final boolean inRange(Point[] wordPoints){
        for (int i = 0; i < wordPoints.length; i++) {
            if(wordPoints[i].getX() < 0 || wordPoints[i].getX() >= 15){
                return false;
            }
            if(wordPoints[i].getY() < 0 || wordPoints[i].getY() >= 15) {
                return false;
            }
        }
        return true;
    }

    /**
     * temporary toString() method until Board.render() is complete <3
     * @return String representation of the Board
     */
    @Override
    public String toString() {
        String[] strings = new String[15];
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++){
            strings[i] = "";
            for(int j = 0; j < 15; j++){
                if(this.points[i][j].getTile() != null) {
                    strings[i] += this.points[i][j].getTile().getValue() + " ";
                }
                else{
                    strings[i] += "  ";
                }
            }
        }
        for(String s : strings){
            sb.append(s).append('\n');
        }
        return sb.toString();
    }

    /**
     * return the number of Points on the Board which are occupied by a Tile
     * @return int representing the count of occupied Point's on this Board
     */
    public final int getOccupiedTileCount(){
        return this.occupiedTileCount;
    }

    /**
     * increment the count of occupied Point's on this board, in the event of a successful placement
     */
    private void incrementOccupiedTileCount(){
        this.occupiedTileCount++;
    }
}
