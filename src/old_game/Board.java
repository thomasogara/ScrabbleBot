package old_game;/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett Pierse (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel Nwabueze (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
     * @param p the old_game.Point to be queried
     * @return boolean representing emptiness of the old_game.Point p
     */
    public final boolean isAvailable(Point p){
        return !this.points[p.getY()][p.getX()].isFilled();
    }

    /**
     * Ensure that a move is valid, according to all constraints (essentially a boolean combination of all
     *  validity-checking methods)
     * @param p the old_game.Point to be queried
     * @return boolean representing whether the placement of the given old_game.Point p would be valid
     */
    public final boolean isValid(Point[] p, char d){
        boolean connecting = false; // initialised to false since it is used in a boolean combination using OR
        boolean centred = false; // initialised to false since it is used in a boolean combination using OR
        boolean valid = true; // initialised to true since it is used in a boolean combination using AND

        // Iterate over the old_game.Point[] array p, and combine the result of calling the validity checks on each element
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
            valid &= isInRange(p[i]) && (isAvailable(p[i]) || isOnBoard(p[i]));
            /*
                If at least one of the points in p[] has a neighbour, connecting will evaluate to true
             */
            connecting |= isConnecting(p[i]);
            /*
                Ensure that all words that are formed as connections to this word are valid dictionary words
             */
        }

        // The first move must only be valid and pass through the centre
        if(this.isFirst()){
            return valid && centred;
        }
        // ALl other moves must be valid and must connect to other words already on the board
        return valid && connecting;
    }

    /**
     * Confirm that a given old_game.Point p already has an equal counterpart at its x and y co-ords on the old_game.Board
     * @param p the old_game.Point to be tested
     * @return boolean representing the old_game.Point p's presence/absence on the old_game.Board
     */
    public final boolean isOnBoard(Point p){
        return points[p.getY()][p.getX()].isFilled() && points[p.getY()][p.getX()].getTile().getValue().equals(p.getTile().getValue());
    }

    public final boolean isInRange(Point p){
        return p.getX() >= 0 && p.getX() < 15 && p.getY() >= 0 && p.getY() < 15;
    }

    /**
     * An alternative to placePoint() which instead accepts a old_game.Tile as a parameter, as well as two integers x and y
     * representing the chosen square on the board. It attempts to place a given old_game.Tile onto this board at the given
     * position and returns a boolean indicating success/failure.
     * @param t The old_game.Tile to be placed on this board
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
     * @param p The old_game.Point to be placed onto this board
     * @return boolean indicating the success/failure of the placement.
     */
    protected boolean add(Point p){
        this.points[p.getY()][p.getX()].setTile(p.getTile());
        this.incrementOccupiedTileCount();
        return true;

    }

    /**
     * The method which acts as another primary interface for the class. It attempts to place old_game.Tile representations of
     * each character of the argument String onto this board, and returns a boolean indicating success/failure.
     * @param s String to be placed on the board
     * @param p old_game.Point encapsulating the x/y position where the first character of the String should be placed
     * @param d Character indicating the direction the word should be placed in ('D' => Down, 'R' => Right)
     */
    public final Scrabble.CommandReturnWrapper add(String s, Point p, char d, Player u){
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        Point[] query = createPointArrayFromQuery(s, p, d, this);
        Point[] required = getRequiredTilesAsPointArray(s, p, d);
        Tile[] requiredTiles = new Tile[required.length];
        ArrayList<String> formed_words = new ArrayList<>();

        for(int i = 0; i < required.length; i++){
            requiredTiles[i] = required[i].getTile();
        }
        // if the direction is not properly defined, or the player's old_game.Frame does not contain the necessary letters
        // to execute the turn (excluding the overlap tiles), then the turn cannot be executed
        if((d != 'R' && d != 'D' )|| !u.getFrame().hasLetters(requiredTiles)){
            return returnWrapper;
        }

        // If this point array is invalid, quit
        if(!isValid(query, d)){
            return returnWrapper;
        }
        /*
            ALL VALIDITY CHECKS COMPLETE, EXECUTE TURN
         */

        // Add each point to the board
        for(int i = 0; i < required.length; i++) {
            if (required[i].getTile() != null) {
                if (u.getFrame().hasLetter(required[i].getTile()))
                    this.add(required[i]);
                else {
                    Point point = required[i];
                    Point displayPoint = new Point(required[i].getX(), required[i].getY());
                    displayPoint.setTile(new Tile('0'));
                    displayPoint.getTile().setValue(point.getTile().getValue());
                    required[i] = new Point(required[i].getX(), required[i].getY());
                    required[i].setTile(new Tile('0'));
                    this.add(displayPoint);
                }
            }
        }

        //update required tiles to account for blanks
        for(int i = 0; i < required.length; i++){
            required[i].refreshGraphic();
            requiredTiles[i] = required[i].getTile();
        }

        ArrayList<Point[]> formed_words_req = new ArrayList<>();
        int j = 0;
        for(Point point : query){
            if(j == query.length - 1) {
                point.refreshFormedWords();
                formed_words.addAll(point.getFormedWords());
            }
            j++;
        }
        HashSet<String> formed_words_nodup = new HashSet<String>(formed_words);
        for(String word : formed_words_nodup){
            formed_words_req.add(createPointArrayFromQuery(word, p, d, this));
        }

        // Remove all necessary tiles form the old_game.Player's old_game.Frame
        u.getFrame().removeAll(Arrays.asList(requiredTiles));

        // The turn has been executed successfully
        returnWrapper.executed = true;
        returnWrapper.score = Scrabble.calculateScore(required, formed_words_req);

        u.played_words.add(s);
        u.played_points.add(required);
        u.scores_from_play.add(returnWrapper.score);

        return returnWrapper;
    }

    protected final Scrabble.CommandReturnWrapper remove(Point[] points){
        Scrabble.CommandReturnWrapper returnWrapper = new Scrabble.CommandReturnWrapper();
        for(Point p : points){
            this.remove(p);
        }
        returnWrapper.executed = true;
        returnWrapper.score = -1;
        return returnWrapper;
    }

    protected final void remove(Point p){
        this.points[p.getY()][p.getX()].setTile(null);
        this.points[p.getY()][p.getX()].renderGraphic();
    }

    /**
     * Get the tiles needed to make a play, as a old_game.Point array
     * @param s the word to be placed
     * @param p the origin of the placements
     * @param d the direction of the placement
     * @return the old_game.Point[] array representing the move to be made
     */
    public final Point[] getRequiredTilesAsPointArray(String s, Point p, char d){
        ArrayList<Point> required = new ArrayList<>();
        Point[] query = Board.createPointArrayFromQuery(s, p, d, this);
        for(Point point : query){
            if(!this.isOnBoard(point))
                required.add(point);
        }
        Point[] requiredPointsArray = new Point[required.size()];
        for(int i = 0; i < required.size(); i++){
            requiredPointsArray[i] = required.get(i);
        }
        return requiredPointsArray;
    }

    /**
     * Ensure that placing a old_game.Tile at a given old_game.Point p will result in it being connected to at least one square already
     * on this old_game.Board
     * @param p the old_game.Point at which the old_game.Tile is to be placed
     * @return boolean representing whether this old_game.Point has any neighbours on this old_game.Board
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
     * @param p the old_game.Point to be tested
     * @return boolean representing whether the point is at the center of the board
     */
    public final boolean isCentered(Point p){
        return p.getX() == 7 && p.getY() == 7;
    }

    /**
     * Confirm that the board contains no other points, .i.e. that this is the first placement on the old_game.Board
     * @return boolean representing whether the board is empty
     */
    public final boolean isFirst(){
        return this.getOccupiedTileCount() == 0;
    }

    /**
     * Utility method for translating a query into an alternative form, as a old_game.Point[] array
     * @param s the String from the parent query
     * @param p the old_game.Point of the origin of the parent query
     * @param d the direction of the parent query
     * @return old_game.Point[] array representing the Points to be placed on the board
     */
    public static Point[] createPointArrayFromQuery(String s, Point p, char d, Board b){
        /*
            Construct a point array to represent the old_game.Point's being placed onto the old_game.Board
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
                    point = new Point(p.getX() + i, p.getY(), b);
                    point.setTile(tile);
                    point_array[i] = point;
                    break;
                case 'D':
                    tile = new Tile(chars[i]);
                    point = new Point(p.getX(), p.getY() + i, b);
                    point.setTile(tile);
                    point_array[i] = point;
                    break;
            }
        }
        return point_array;
    }

    /**
     * temporary toString() method until old_game.Board.render() is complete <3
     * @return String representation of the old_game.Board
     */
    @Override
    public String toString() {
        String[] strings = new String[15];
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 15; i++){
            strings[i] = ( i + 1 < 10 ? " " + (i + 1) : i + 1) + " |";
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
     * return the number of Points on the old_game.Board which are occupied by a old_game.Tile
     * @return int representing the count of occupied old_game.Point's on this old_game.Board
     */
    public final int getOccupiedTileCount(){
        return this.occupiedTileCount;
    }

    /**
     * increment the count of occupied old_game.Point's on this board, in the event of a successful placement
     */
    private void incrementOccupiedTileCount(){
        this.occupiedTileCount++;
    }
}