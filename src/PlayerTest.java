import java.util.Arrays;

public class PlayerTest {
    public static void main(String[] args) {
        Player player1 = new Player("player 1");
        Player player2 = new Player("player 2");

        System.out.println("Player 1 Username: " + player1.displayUsername());
        System.out.println("Player 2 Username: " + player2.displayUsername());
        System.out.println("Player 1 Frame: " + Arrays.toString(player1.getFrame()));
        System.out.println("Player 2 Frame: " + Arrays.toString(player2.getFrame()));

        Pool pool = new Pool(); // create instance of pool

        System.out.println(Arrays.toString(pool.drawRandTiles(5)));

    }
}
