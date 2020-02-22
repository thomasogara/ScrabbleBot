import java.util.ArrayList;

public class Board {
    private int occupiedTileCount;

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
    public final boolean isValid(Point[] p){
        boolean connecting = false; // initialised to false to allow for boolean algebra hackz
        boolean centred = false; // initialised to false to allow for boolean algebra hackz
        boolean valid = true; // initialised to true to allow for boolean algebra hackz

        // Iterate over the Point[] array p, and combine the result of calling the validity checks on each element
        for(int i = 0; i < p.length; i++){
            if(this.isFirst()){
                /*
                    If at least one of the points in p[] is in the centre of the board, centred will evaluate to true
                 */
                centred |= isCentered(p[i]);
            }
            /*
                Validity is determined by:
                    - the point being in range
                    - the point on the board being available, OR the point on the board already having the same value as the one being placed there
             */
            valid &= (isAvailable(p[i]) || isOnBoard(p[i])) && isInRange(p[i]);
            /*
                If at least one of the points in p[] has a neighbour, connecting will evaluate to true
             */
            connecting |= isConnecting(p[i]);
        }

        // The first move must only be valid and pass through the centre
        if(this.isFirst()){
            return valid && centred;
        }
        // ALl other moves must be valid and must connect to other words already on the board
        return valid && connecting;
    }

    /**
     * Confirm that a given Point p already has an equal counterpart at its x and y co-ords on the Board
     * @param p the Point to be tested
     * @return boolean representing the Point p's presence/absence on the Board
     */
    public final boolean isOnBoard(Point p){
        return points[p.getY()][p.getX()].isFilled() && points[p.getY()][p.getX()].getTile().getValue().equals(p.getTile().getValue());
    }

    public final boolean isInRange(Point p){
        return p.getX() >= 0 && p.getX() < 15 && p.getY() >= 0 && p.getY() < 15;
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
    protected boolean add(Tile t, int x, int y){
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
    protected boolean add(Point p){
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
    public final boolean add(String s, Point p, char d, Player u){
        // Construct a String containing all the overlapping tiles
        String overlap = this.getOverlap(s, p, d);

        // if the direction is not properly defined, or the player's Frame does not contain the necessary letters
        // to execute the turn (excluding the overlap tiles), then the turn cannot be executed
        if((d != 'R' && d != 'D' )|| !u.getFrame().hasLetters((u.getFrame().getLettersAsString() + overlap).toCharArray())){
            return false;
        }

        // Translate the input information into a more manageable form, a Point[] array
        // This array can then be checked for validity, and placed onto the Board
        Point[] point_array = createPointArrayFromQuery(s, p, d);

        // If this point array is invalid, quit
        if(!isValid(point_array)){
            return false;
        }

        /*
            ALL VALIDITY CHECKS COMPLETE, EXECUTE TURN
         */

        // Add each point to the board
        for(Point point : point_array)
            this.add(point);

        // Remove all necessary tiles form the Player's Frame
        removeTilesFromPLayerFrame(s, overlap, u);

        // The turn has been executed successfully
        return true;
    }

    /**
     * A utility method to allow all necessary tiles for a given turn to be removed from the Player's Frame
     * @param s String representing thr word being played
     * @param overlap String representing the characters which are already on the board in the path of this turn
     * @param u the Player who's Frame is to be altered
     */
    public final void removeTilesFromPLayerFrame(String s, String overlap, Player u){

        // Construct a new ArrayList which will contain Tile representing all of the Points to be placed during this turn
        ArrayList<Tile> s_list = new ArrayList<>();

        // Add the Tile representation of each letter of the input to the ArrayList s_list
        for(char c : s.toCharArray())
            s_list.add(new Tile(c));

        // Remove the Tile representation of each letter of the overlap from the ArrayList s_list, as these
        // tiles mustn't be removed from the Player's Frame, since they already exist on the board
        for(char c : overlap.toCharArray())
            s_list.remove(new Tile(c));

        // Remove all the letters from the player's frame which they would have to use in order to execute this turn
        u.getFrame().removeAll(s_list);
    }

    /**
     * Construct a String representing the values of the Points which already exist on the board, in the path of the turn
     * @param s the String to be placed on the Board
     * @param p the Point representing the origin of the move
     * @param d the direction of the move ('R' => Right, 'D' => Down)
     * @return String representing the overlap
     */
    public final String getOverlap(String s, Point p, char d){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            Tile t;
            if(d == 'R'){
                t = points[p.getY()][p.getX() + i].getTile();
                if(t != null && t.getValue() != null){
                    sb.append(t.getValue());
                }
            }else{
                t = points[p.getY() + i][p.getX()].getTile();
                if(t != null && t.getValue() != null){
                    sb.append(t.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * Ensure that placing a Tile at a given Point p will result in it being connected to at least one square already
     * on this Board
     * @param p the Point at which the Tile is to be placed
     * @return boolean representing whether this Point has any neighbours on this Board
     */
    public final boolean isConnecting(Point p){
        return points[p.getY()][p.getX()].isFilled() ||
                (p.getRight()!= null && p.getRight().isFilled()) ||
                (p.getLeft()!= null && p.getLeft().isFilled()) ||
                (p.getDown()!= null && p.getDown().isFilled()) ||
                (p.getUp()!= null && p.getUp().isFilled());
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
     * Utility method for translating a query into an alternative form, as a Point[] array
     * @param s the String from the parent query
     * @param p the Point of the origin of the parent query
     * @param d the direction of the parent query
     * @return Point[] array representing the Points to be placed on the board
     */
    public final Point[] createPointArrayFromQuery(String s, Point p, char d){
        /*
            Construct a point array to represent the Point's being placed onto the Board
            this point array is used to check the validity of the move
         */
        Point[] point_array = new Point[s.length()];
        char[] chars = s.toCharArray();
        for(int i = 0; i < s.length(); i++){
            Tile tile;
            Point point;
            switch (d){
                case 'R':
                    tile = new Tile(chars[i]);
                    point = new Point(p.getX() + i, p.getY(), this);
                    point.setTile(tile);
                    point_array[i] = point;
                    break;
                case 'D':
                    tile = new Tile(chars[i]);
                    point = new Point(p.getX(), p.getY() + i, this);
                    point.setTile(tile);
                    point_array[i] = point;
                    break;
            }
        }
        return point_array;
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