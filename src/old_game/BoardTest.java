package old_game;

public class BoardTest {
    public static Board gameBoard;
    public static Pool gamePool;

    public static Player player1;
    public static Player player2;

    public static void main(String[] args) {
        // Intialise the game board and necessary objects
        gameBoard = new Board();
        System.out.print("\n\n");
        gamePool = new Pool();
        player1 = new Player("Dan");
        player2 = new Player("Thomas");

        // setup
        player1.getFrame().setPool(gamePool);
        player2.getFrame().setPool(gamePool);
        player1.getFrame().getLetters().clear();
        player2.getFrame().getLetters().clear();



        //First test

        player1.getFrame().getLetters().clear();
        player2.getFrame().getLetters().clear();
        player1.getFrame().addAll("AAPPLES");
        player2.getFrame().addAll("BATHXDE");
        System.out.println(player1.getUsername() + "'s old_game.Frame Before Move: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame Before Move: " + player2.getFrame());
        System.out.println(player1.getUsername() + " placing \"APPLE\" onto the old_game.Board");

        gameBoard.add("APPLE", new Point(6, 6), 'R', player1);

        System.out.println(gameBoard);
        System.out.println(player1.getUsername() + "'s old_game.Frame After Move: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame After Move: " + player2.getFrame());
        System.out.println("\nTest Pass?: " +player1.getFrame().toString().equals("[A, S]"));

        System.out.println("\n\n\n\n");



        // Second Test

        player1.getFrame().getLetters().clear();
        player2.getFrame().getLetters().clear();
        player1.getFrame().addAll("AAPPLES");
        player2.getFrame().addAll("BATHXDE");
        System.out.println(player1.getUsername() + "'s old_game.Frame Before Move: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame Before Move: " + player2.getFrame());
        System.out.println(player2.getUsername() + " placing \"BATH\" onto the old_game.Board");

        gameBoard.add("BATH", new Point(6, 5), 'D', player2);

        System.out.println(gameBoard);
        System.out.println(player1.getUsername() + "'s old_game.Frame After Test: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame After Test: " + player2.getFrame());
        System.out.println("\nTest Pass?: " +player2.getFrame().toString().equals("[A, X, D, E]"));

        System.out.println("\n\n\n\n");

        /*
            Check for single-letter placements at end of existing word
            e.g. |A|P|P|L|E|
            place |S| at end
            should result in |A|P|P|L|E|S| with only |S| removed from PLayer's frame
         */
        player1.getFrame().getLetters().clear();
        player2.getFrame().getLetters().clear();
        player1.getFrame().addAll("SSSSSSS");
        player2.getFrame().addAll("BATHXDE");
        System.out.println(player1.getUsername() + "'s old_game.Frame Before Move: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame Before Move: " + player2.getFrame());
        System.out.println(player1.getUsername() + " placing \"APPLES\" onto the old_game.Board");

        gameBoard.add("APPLES", new Point(6, 6), 'R', player1);

        System.out.println(gameBoard);
        System.out.println(player1.getUsername() + "'s old_game.Frame: " + player1.getFrame());
        System.out.println(player2.getUsername() + "'s old_game.Frame: " + player2.getFrame());
        System.out.println("\nTest Pass?: " +player1.getFrame().toString().equals("[S, S, S, S, S, S]"));
    }
}
