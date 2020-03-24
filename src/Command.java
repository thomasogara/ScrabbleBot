/**A functional interface used to allow for the storage of references to the commands of the game*/
@FunctionalInterface
public interface Command {
    public Scrabble.CommandReturnWrapper run(String[] tokens, Player p);
}
