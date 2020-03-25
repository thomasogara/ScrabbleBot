public class ScrabbleMainThread implements Runnable{
    @Override
    public void run() {
        try {
            Scrabble.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}