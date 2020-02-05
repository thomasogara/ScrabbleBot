import java.util.ArrayList;
import java.util.Arrays;

public class FrameTest{
    public static Frame createDefaultFrame(){
        Frame frame = new Frame();
        Character[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Character c : letters) {
            tiles.add(new Tile(c));
        }
        frame.getLetters().addAll(tiles);
        return frame;
    }

    public static class ConstructorTests {
        public static void defaultConstructor() {
            Frame frame = new Frame();
            if (frame.getLetters() != null) {
                System.out.println("defaultConstructor Test Success");
            }else{
                System.out.println("defaultConstructor Test Failure");
            }
        }
    }

    public static class GetLettersTests{
        public static void getLettersAsString() {
            Frame frame = createDefaultFrame();
            if(frame.getLettersAsString().equals("A B C D E F G")){
                System.out.println("getLettersAsString Test Success");
            }else{
                System.out.println("getLettersAsString Test Failure");
            }
        }

        public static void getLettersAsCharArray(){
            Frame frame = createDefaultFrame();
            char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
            if(Arrays.equals(frame.getLettersAsCharArray(), chars)){
                System.out.println("getLettersAsCharArray Test Success");
            }else{
                System.out.println("getLettersAsCharArray Test Failure");
            }
        }

        public static void getLettersAsCharacterArray(){
            Frame frame = createDefaultFrame();
            Character[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
            if(Arrays.equals(frame.getLettersAsCharacterArray(), chars)){
                System.out.println("getLettersAsCharArray Test Success");
            }else{
                System.out.println("getLettersAsCharArray Test Failure");
            }
        }
    }

    public static class HasLetterTests{
        public static void hasLetterSingleChar(){
            Frame frame = createDefaultFrame();
            if(frame.hasLetter('A') && frame.hasLetter('B') && frame.hasLetter('C') && frame.hasLetter('D') && frame.hasLetter('E') && frame.hasLetter('F') && frame.hasLetter('G')){
                System.out.println("hasLetterSingleChar Test Success");
            }else{
                System.out.println("hasLetterSingleChar Test Success");
            }
        }

        public static void hasLetterSingleTile(){
            Frame frame = createDefaultFrame();
            if(frame.hasLetter(new Tile('A')) && frame.hasLetter(new Tile('B')) && frame.hasLetter(new Tile('C')) && frame.hasLetter(new Tile('D')) && frame.hasLetter(new Tile('E')) && frame.hasLetter(new Tile('F')) && frame.hasLetter(new Tile('G'))){
                System.out.println("hasLetterSingleChar Test Success");
            }else{
                System.out.println("hasLetterSingleChar Test Failure");
            }
        }

        public static void hasLettersString(){
            Frame frame = createDefaultFrame();
            if(frame.hasLetters("ABCDEFG")){
                System.out.println("hasLettersString Test Success");
            }else{
                System.out.println("hasLettersString Test Failure");
            }
        }
    }
}