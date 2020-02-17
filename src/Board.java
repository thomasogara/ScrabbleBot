public class Board {


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
                points[x][y] = new Point(x, y, this);
            }
        }
    }

}
