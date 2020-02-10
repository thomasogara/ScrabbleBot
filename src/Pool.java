/*
   Code Authors:
   Thomas O'Gara (18379576) (thomas.ogara@ucdconnect.ie)
   Jarrett (?) (?)
   Daniel (17481174) (daniel.nwabueze@ucdconnect.ie)

   TODO;;;
   1. Change references of <Character, Integer> to <Tile, Integer> once Tile.java has been imported

 */

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Pool {

    // Stores the value of the tiles (i.e the points garnered from each tile)
    private final HashMap<Character, Integer> tileValues = new HashMap<>() {{
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
    private HashMap<Character, Integer> poolTiles;

    public Pool(){
        this.initPoolTiles();
    }

    /**
     * Sets the tiles currently in the pool (i.e in the bag) that are available for draw.
     */
    public void initPoolTiles(){
        this.poolTiles = new HashMap<>() {{
            put('A', 9);
            put('B', 2);
            put('C', 2);
            put('D', 4);
            put('E', 12);
            put('F', 2);
            put('G', 3);
            put('H', 2);
            put('I', 9);
            put('J', 1);
            put('K', 1);
            put('L', 4);
            put('M', 2);
            put('N', 6);
            put('O', 8);
            put('P', 2);
            put('Q', 1);
            put('R', 6);
            put('S', 4);
            put('T', 6);
            put('U', 4);
            put('V', 2);
            put('W', 2);
            put('X', 1);
            put('Y', 2);
            put('Z', 1);
            put('0', 2);
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
        for(Map.Entry<Character, Integer> tile : this.poolTiles.entrySet()) {
            System.out.println("There are " + tile.getValue() + " " + tile.getKey() + " tiles.");
        }
    }

    /**
     * Gets the quantity of a specific tile (i.e letter) in the pool
     * @param tile that we wish to grab the quantity of
     * @return the tiles quantity
     */
    public Integer getTilePoolCount(Character tile){
        return this.poolTiles.get(tile);
    }

    /**
     * Gets the value of a tile (i.e the points) garnered from a play using that tile
     * @param tile that we wish to query the value of
     * @return the value of that tile
     */
    public Integer getTileValue(Character tile) {
        return this.tileValues.get(tile);
    }

    /**
     * Gets the available tiles in the pool (i.e tiles with a quantity/count > 0)
     * @return hashmap of available tiles in the pool that can be drawn
     */
    public HashMap<Character, Integer> getAvailableTiles(){

        HashMap<Character, Integer> available = new HashMap<>();
        for(Map.Entry<Character, Integer> tile : this.poolTiles.entrySet()) {
            if(tile.getValue() > 0)
                available.put(tile.getKey(), tile.getValue());
        }

        return available;
    }

    /**
     * Draws a random tile from the pool & decrements that tiles quantity in the pool by 1
     * @return a character value representing the tile
     */
    public Character drawRandTile(){

        // Get list of available tiles
        HashMap<Character, Integer> availableTiles = this.getAvailableTiles();

        // Select a random tile from poolTiles HashMap
        Character randTile = (Character) availableTiles.keySet().toArray()[new Random().nextInt(availableTiles.keySet().toArray().length)];

        // Get the random tiles current quantity & decrement it's quantity/count by 1
        Integer currentCount = this.poolTiles.get(randTile);
        this.poolTiles.replace(randTile, currentCount, currentCount - 1);

        // Since we have drawn that tile, decrement it's count available in
        return randTile;
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
