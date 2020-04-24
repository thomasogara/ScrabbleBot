import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        Dictionary d = new Dictionary();
        Scanner sc = new Scanner(new File("csw.txt"));
        while(sc.hasNextLine()){
            if(!d.areWords(new ArrayList<Word>() {{add(new Word(0,0,true,sc.nextLine()));}})){
                System.out.println("FAIL");
            }
        }
        sc.close();
    }
}
