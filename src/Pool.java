/*
  Code Authors:
  Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
  Jarrett (18375813 (jarrett.pierse@ucdconnect.ie)
  Daniel (17481174) (daniel.nwabueze@ucdconnect.ie)
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Pool {

    // Stores the value of the tiles (i.e the points garnered from each tile)
    public static HashMap<Character, Integer> tileValues = new HashMap<>() {{
        put('A', 1);
        put('E', 1);
        put('I', 1);
        put('O', 1);
        put('U', 1);
        put('L', 1);
        put('N', 1);
        put('S', 1);
        put('T', 1);
        put('R', 1);
        put('D', 2);
        put('G', 2);
        put('B', 3);
        put('C', 3);
        put('M', 3);
        put('P', 3);
        put('F', 4);
        put('H', 4);
        put('V', 4);
        put('W', 4);
        put('Y', 4);
        put('K', 5);
        put('J', 8);
        put('X', 8);
        put('Q', 10);
        put('Z', 10);
        put('0', 0);
    }};

    // Stores the tiles on the pool currently - MAY be changed to HashMap<Tile, Integer> once Tile class is integrated.
    private HashMap<Tile, Integer> poolTiles;

    public Pool(){
        this.initPoolTiles();
    }

    /**
     * Sets the tiles currently in the pool (i.e in the bag) that are available for draw.
     */
    public void initPoolTiles(){
        this.poolTiles = new HashMap<Tile, Integer>() {{
            put(new Tile('A'), 9);
            put(new Tile('B'), 2);
            put(new Tile('C'), 2);
            put(new Tile('D'), 4);
            put(new Tile('E'), 12);
            put(new Tile('F'), 2);
            put(new Tile('G'), 3);
            put(new Tile('H'), 2);
            put(new Tile('I'), 9);
            put(new Tile('J'), 1);
            put(new Tile('K'), 1);
            put(new Tile('L'), 4);
            put(new Tile('M'), 2);
            put(new Tile('N'), 6);
            put(new Tile('O'), 8);
            put(new Tile('P'), 2);
            put(new Tile('Q'), 1);
            put(new Tile('R'), 6);
            put(new Tile('S'), 4);
            put(new Tile('T'), 6);
            put(new Tile('U'), 4);
            put(new Tile('V'), 2);
            put(new Tile('W'), 2);
            put(new Tile('X'), 1);
            put(new Tile('Y'), 2);
            put(new Tile('Z'), 1);
            put(new Tile('0'), 2);
        }};
    }

    /**
     * Resets the tiles inside of the pool (i.e in the bag; i.e fills up the bag)
     */
    public void resetPool(){

        // Clear the pool if any tiles are inside
        if(this.poolTiles != null && this.poolTiles.size() > 0) {
            this.poolTiles.clear();
        }

        this.initPoolTiles();
    }

    /**
     * Prints the count/quantity of each tile in the pool
     */
    public void displayPool(){
        for(Map.Entry<Tile, Integer> tile : this.poolTiles.entrySet()) {
            System.out.println("There are " + tile.getValue() + " " + tile.getKey().toString() + " tiles.");
        }
    }

    /**
     * Gets the quantity of a tiles in the pool
     * @return the quantity of tiles in the pool
     */
    public Integer getTilePoolCount(){
        return this.poolTiles.values().stream().reduce(0, Integer::sum);
    }

    /**
     * Gets the value of a tile (i.e the points) garnered from a play using that tile
     * @param tile that we wish to query the value of
     * @return the value of that tile
     */
    public Integer getTileValue(Tile tile) {
        return Pool.tileValues.get(tile.getValue());
    }

    /**
     * Gets the available tiles in the pool (i.e tiles with a quantity/count > 0)
     * @return hashmap of available tiles in the pool that can be drawn
     */
    public HashMap<Tile, Integer> getAvailableTiles(){

        HashMap<Tile, Integer> available = new HashMap<>();
        for(Map.Entry<Tile, Integer> tile : this.poolTiles.entrySet()) {
            if(tile.getValue() > 0)
                available.put(tile.getKey(), tile.getValue());
        }

        return available;
    }

    /**
     * Draws a random tile from the pool & decrements that tiles quantity in the pool by 1
     * @return a character value representing the tile
     */
    public ArrayList<Tile> drawRandTiles(int number){

        ArrayList<Tile> randomTiles = new ArrayList<>();

        for(int i = 0; i < number; i++) {

            // Get list of available tiles
            HashMap<Tile, Integer> availableTiles = this.getAvailableTiles();

            // Select a random tile from poolTiles HashMap
            Tile randTile = (Tile) availableTiles.keySet().toArray()[new Random().nextInt(availableTiles.keySet().toArray().length)];

            // Get the random tiles current quantity & decrement it's quantity/count by 1
            Integer currentCount = this.poolTiles.get(randTile);
            this.poolTiles.replace(randTile, currentCount, currentCount - 1);

            randomTiles.add(randTile);

        }

        return randomTiles;
    }

    /**
     * Checks if the pool is empty
     * @return true if the pool is empty false if not
     */
    public boolean isEmpty(){

        for(Integer count : this.poolTiles.values()) {
            if(count > 0) return false;
        }
        return true;
    }

}
