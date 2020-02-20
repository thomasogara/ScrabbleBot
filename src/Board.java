public class Board {
    private int occupiedTileCount;
    private boolean wasFrameAccessed;

    public final int getOccupiedTileCount(){
        return this.occupiedTileCount;
    }

    private void incrementOccupiedTileCount(){
        this.occupiedTileCount++;
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
     */
    public final boolean isAvailable(Point p){
        return !this.points[p.getY()][p.getX()].isFilled();
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
    public final boolean addTile(Tile t, int x, int y){
        Point p = new Point(x, y);
        p.setBoard(this);
        p.setTile(t);
        return this.addPoint(p);
    }

    /**
     * The method which acts as the main interface for the class. It attempts to place a given point onto this board,
     * and returns a boolean indicating success/failure.
     * @param p The Point to be placed onto this board
     * @return boolean indicating the success/failure of the placement.
     */
    public final boolean addPoint(Point p){
        if(!this.isAvailable(p) || (!this.isCentered(p) && this.isFirst())){
            return false;
        }
        p.setBoard(this);
        this.points[p.getY()][p.getX()] = p;
        this.incrementOccupiedTileCount();
        return true;

    }

    /**
     *
     * @param p
     * @return
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
     * Confirm that the board contains no other points
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
}
