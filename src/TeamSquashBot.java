import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TeamSquashBot implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private PlayerAPI me;
    private OpponentAPI opponent;
    private BoardAPI board;
    private UserInterfaceAPI info;
    private DictionaryAPI dictionary;
    private int turnCount = 0;
    private Gaddag gaddag;

    TeamSquashBot(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
    }

    /*
    public static ArrayList<String> permute(String str) {
        ArrayList<String> permutations = new ArrayList<>();
        permute("", str, permutations);
        return permutations;
    }

    private static void permute(String prefix, String str, ArrayList<String> list) {
        int n = str.length();
        if (n == 0) list.add(prefix);
        else {
            for (int i = 0; i < n; i++)
                permute(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), list);
        }
    }
    */

    public String getCommand() {
        // Add your code here to input your commands
        String command = "";
        switch (turnCount) {
            case 0:
                command = "NAME Bot1";
                gaddag = new Gaddag();
                break;
            default:
                String rack = me.getFrameAsString().replaceAll("[^A-Z_]", "");
                /*
                ArrayList<String> validWords = new ArrayList<>();
                HashSet<String> substrings = new HashSet<>();
                HashSet<String> permutations = new HashSet<>();
                System.out.println(rack);
                ArrayList<String> possibleRacks = new ArrayList<>();
                ArrayList<String> tempRacks = new ArrayList<>();
                if(rack.contains(String.valueOf(Tile.BLANK))){
                    int i = -1;
                    while((i = rack.indexOf(Tile.BLANK)) != -1){
                        for(char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()){
                            if(tempRacks.size() == 0) {
                                tempRacks.add(rack.replace(Tile.BLANK, c));
                            } else{
                                ArrayList<String> temp = new ArrayList<>(tempRacks);
                                tempRacks.clear();
                                for(String tempRack : temp){
                                    tempRacks.add(tempRack.replace(Tile.BLANK, c));
                                }
                            }
                        }
                    }
                }else{
                    possibleRacks.add(rack);
                }

                System.out.println("all racks computed");

                for(String possibleRack : possibleRacks){
                    permutations.addAll(permute(possibleRack));
                }

                System.out.println("all permutations of all racks computed");

                // get all substrings of the permutations of the letters on the rack
                for(String permutation : permutations) {
                    for (int i = 0; i < permutation.length(); i++) {
                        for (int j = i + 1; j <= permutation.length(); j++) {
                            substrings.add(permutation.substring(i, j));
                        }
                    }
                }

                System.out.println("all substrings of all permutations computed");

                // clear permutations
                permutations.clear();
                // fill permutations with all permutations of all substrings of all permutations of letters on the rack
                for(String substring : substrings){
                    permutations.addAll(permute(substring));
                }

                System.out.println("all permutations of all substrings computed");

                // remove all invalid words
                permutations.removeIf(word -> (!dictionary.areWords(new ArrayList<Word>() {{add(new Word(0,0,true, word));}})));

                System.out.println("all dictionary checks computed");
                */

                ArrayList<ArrayList<Integer>> anchors = new ArrayList<>();
                for(int r = 0; r < 15; r++){
                    for(int c = 0; c < 15; c++){
                        if(board.getSquareCopy(r, c).isOccupied()){
                            final int row = r;
                            final int column = c;
                            ArrayList<Integer> anchor = new ArrayList<Integer>(){{add(column); add(row);}};
                            anchors.add(anchor);
                            System.out.println("anchor: " + anchor);
                            GaddagSprawler sprawler = new GaddagSprawler(anchor.get(0), anchor.get(1), rack, gaddag);
                            ArrayList<Word> moves = sprawler.sprawl();
                            for(Word move : moves) {
                                if (move != null) System.out.println("move: " + (char) ('A' + (char) move.getFirstColumn()) + (move.getFirstRow() + 1) + " " + move.getDesignatedLetters());
                                if (move != null) moves.add(move);
                            }
                        }
                    }
                }
                command = "EXCHANGE " + rack;
                break;
        }
        turnCount++;
        System.out.println("Bot command: \"" + command + "\"");
        return command;
    }

    private class GaddagSprawler{
        private final int x;
        private final int y;
        private final String rack;
        private final ArrayList<Word> recordedMoves;
        private final Gaddag gaddag;

        public GaddagSprawler(int x, int y, String rack, Gaddag gaddag){
            this.x = x;
            this.y = y;
            this.rack = rack;
            this.recordedMoves = new ArrayList<>();
            this.gaddag = gaddag;
        }

        ArrayList<Word> sprawl(){
            generate(0, "", rack, gaddag.root);
            return recordedMoves;
        }

        void generate(int position, String word, String rack, Gaddag.GaddagNode arc){
            if(board.getSquareCopy(y, x + position).isOccupied()){
                char letter = board.getSquareCopy(y, x + position).getTile().getLetter();
                //search(position, letter, word, rack, NextArc(arc, letter), arc);
            }else if(!rack.isEmpty()){
                for(char letter : rack.toCharArray()){
                    if(letter == Tile.BLANK) continue;
                    //search(position, letter, word, rack.replace(String.valueOf(letter), ""), NextArc(arc, letter), arc);
                }
                if(rack.contains(String.valueOf(Tile.BLANK))){
                    for(char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()){
                        //search(position, letter, word, rack.replace(String.valueOf(Tile.BLANK), ""), NextArc(arc, letter), arc);
                    }
                }
            }
        }

        /*
        void search(int position, char letter, String word, String rack, Gaddag.GaddagNode newArc, Gaddag.GaddagNode oldArc){
            if(position <= 0){
                word = letter + word;
                if(oldArc.isChild(letter) && !board.getSquareCopy(y, x + position).isOccupied()){
                    record(word, position, oldArc);
                }
                if(newArc != null){
                    if(!board.getSquareCopy(y, x + position).isOccupied()){
                        generate(position - 1, word, rack, newArc);
                    }
                    newArc = NextArc(newArc, '#');
                    if(newArc != null && !board.getSquareCopy(y, x + position).isOccupied() && !board.getSquareCopy(y, x + 1).isOccupied()){
                        generate(1, word, rack, newArc);
                    }
                }
            }else{
                word = word + letter;
                if(oldArc.isChild(letter) && !board.getSquareCopy(y, x + position).isOccupied()){
                    record(word, position, oldArc);
                }
                if(newArc != null && !board.getSquareCopy(y, x + position).isOccupied()){
                    generate(position + 1, word, rack, newArc);
                }
            }
        }
         */

        void record(String word, int position, Node arc){
            if(position > 0) position -= word.length();
            Word newWord = new Word(y, x + position, true, word);
            if(newWord.getLastColumn() >= 15 || newWord.getFirstColumn() < 0) return;
            recordedMoves.add(newWord);
        }

        int compare(Word a, Word b){
            return Integer.compare(score(a), score(b));
        }

        int score(Word word){
            int wordValue = 0;
            int wordMultipler = 1;
            int r = word.getFirstRow();
            int c = word.getFirstColumn();
            for (int i = 0; i<word.length(); i++) {
                int letterValue = word.getLetters().charAt(i);
                if (!board.getSquareCopy(r, c).isOccupied()) {
                    wordValue += letterValue * board.getSquareCopy(r, c).getLetterMuliplier();
                    wordMultipler *= board.getSquareCopy(r, c).getWordMultiplier();
                } else {
                    wordValue += letterValue;
                }
                if (word.isHorizontal()) {
                    c++;
                } else {
                    r++;
                }
            }
            return wordValue * wordMultipler;
        }
    }

    public class Gaddag{
        // end of word
        private final Character EOW = '>';
        // middle of word
        private final Character MOW = '#';
        // start of word
        private final Character SOW = '<';
        // root node of the dictionary, initialised with value SOW
        private final GaddagNode root = new GaddagNode(SOW);

        Gaddag(){
            try{
                Scanner sc = new Scanner(new File("csw.txt"));
                String line;
                int i = 0;
                while((line = sc.nextLine()) != null){
                    System.out.println("words encountered :" + i++);
                    add(line);
                }
                sc.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        private void add(String word){
            ArrayList<String> arcs = new ArrayList<>();
            String rev = null;
            String suffix = null;
            String arc = null;
            for (int i = 1; i <= word.length(); i++) {
                rev = new StringBuilder(word).delete(i, word.length()).reverse().toString();
                suffix = word.substring(i);
                arc = rev + (suffix.equals("") ? "" : "#" + suffix);
                arcs.add(arc);
                rev = null;
                suffix = null;
                arc = null;
            }
            for(String path : arcs){
                // TODO
                // can't just create naive gaddag because memory will run out,
                // need a more compressed version, but thats not easy to code
            }
            arcs = null;
        }

        private class GaddagNode{
            private Character data;
            private ArrayList<GaddagNode> children;
            private boolean terminal;

            public GaddagNode(Character data){
                this.data = data;
                children = new ArrayList<>();
                terminal = false;
            }

            public void add(String str) {
                if (!str.equals("")) {
                    if (!containsTree(str.charAt(0))) {
                        children.add(new GaddagNode(str.charAt(0)));
                    }
                    getTree(str.charAt(0)).add(str.substring(1));
                } else {
                    terminal = true;
                }
            }

            public boolean containsTree(Character c){
                for(GaddagNode g : children) if (g.getData() == c) return true;
                return false;
            }

            public GaddagNode getTree(Character c){
                for(GaddagNode g : children) if (g.getData() == c) return g;
                return null;
            }

            public boolean isTerminal(){ return terminal; }

            public Character getData() { return data; }

            public ArrayList<GaddagNode> getChildren(){ return children; }

            public boolean contains(Character c){
                if(containsTree(c)) return true;
                else{
                    for(GaddagNode g : children) if (g.containsTree(c)) return true;
                    return false;
                }
            }
        }

    }

}
