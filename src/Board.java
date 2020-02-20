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
     * Ensure that the selected square is empty
     * @param p the Point to be queried
     * @return boolean representing emptiness of the Point p
     */
    public final boolean isAvailable(Point p){
        return !this.points[p.getY()][p.getX()].isFilled();
    }

    /**
     * Ensure that a move is valid, according to all constraints (essentially a boolean combination of all
     *  validity-checking methods)
     * @param p the Point to be queried
     * @return boolean representing whether the placement of the given Point p would be valid
     */
    public final boolean isValid(Point p){
        if(this.isFirst()){
            return this.isCentered(p);
        }else{
            return this.isAvailable(p) && this.isConnecting(p);
        }
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
    public final boolean add(Tile t, int x, int y){
        Point p = new Point(x, y);
        p.setBoard(this);
        p.setTile(t);
        return this.add(p);
    }

    /**
     * The method which acts as the main interface for the class. It attempts to place a given point onto this board,
     * and returns a boolean indicating success/failure.
     * @param p The Point to be placed onto this board
     * @return boolean indicating the success/failure of the placement.
     */
    public final boolean add(Point p){
        if(!this.isValid(p)){
            return false;
        }
        p.setBoard(this);
        this.points[p.getY()][p.getX()] = p;
        this.incrementOccupiedTileCount();
        return true;

    }

    /**
     * The method which acts as another primary interface for the class. It attempts to place Tile representations of
     * each character of the argument String onto this board, and returns a boolean indicating success/failure.
     * @param s String to be placed on the board
     * @param p Point encapsulating the x/y position where the first character of the String should be placed
     * @param d Character indicating the direction the word should be placed in ('D' => Down, 'R' => Right)
     */
    public final boolean add(String s, Point p, char d){
        char[] chars = s.toCharArray();
        switch (d){
            case 'R':
                for(int i = 0; i < s.length(); i++) {
                    char c = chars[i];
                    this.add(new Tile(c), p.getX() + i, p.getY());
                }
                break;
            case 'D':
                for(int i = 0; i < s.length(); i++) {
                    char c = chars[i];
                    this.add(new Tile(c), p.getX(), p.getY() + i);
                }
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Ensure that placing a Tile at a given Point p will result in it being connected to at least one square already
     * on this Board
     * @param p the Point at which the Tile is to be placed
     * @return boolean representing whether this Point has any neighbours on this Board
     */
    public final boolean isConnecting(Point p){
        return p.getFormedWords().size() != 0;
    }


    /**
     * Confirm that a point is located at the center of the board
     * @param p the Point to be tested
     * @return boolean representing whether the point is at the center of the board
     */
    public final boolean isCentered(Point p){
        return p.getX() == 6 && p.getY() == 6;
    }

    /**
     * Confirm that the board contains no other points, .i.e. that this is the first placement on the Board
     * @return boolean representing whether the board is empty
     */
    public final boolean isFirst(){
        return this.getOccupiedTileCount() == 0;
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
