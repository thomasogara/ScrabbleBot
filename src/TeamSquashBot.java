import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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

    TeamSquashBot(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
    }

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

    public String getCommand() {
        // Add your code here to input your commands
        String command = "";
        switch (turnCount) {
            case 0:
                command = "NAME Bot1";
                break;
            default:
                ArrayList<String> validWords = new ArrayList<>();
                HashSet<String> substrings = new HashSet<>();
                HashSet<String> permutations = new HashSet<>();
                String rack = me.getFrameAsString().replaceAll("[^A-Z_]", "");
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

                Gaddag gaddag;
                gaddag = new Gaddag(permutations);

                System.out.println("Gaddag constructed");

                ArrayList<ArrayList<Integer>> anchors = new ArrayList<>();
                for(int r = 0; r < 15; r++){
                    for(int c = 0; c < 15; c++){
                        if(board.getSquareCopy(r, c).isOccupied()){
                            final int row = r;
                            final int column = c;
                            anchors.add(new ArrayList<Integer>(){{add(column); add(row);}});
                        }
                    }
                }
                System.out.println("anchors: " + anchors);
                ArrayList<Word> moves = new ArrayList<>();
                for(int i = 0; i < anchors.size(); i++){
                    moves.add(gaddag.sprawl(anchors.get(i).get(1), anchors.get(i).get(0), rack));
                }
                moves.removeAll(Collections.singleton(null));
                if(moves.isEmpty()){
                    command = "EXCHANGE " + rack;
                    break;
                }else {
                    for (Word move : moves) {
                        System.out.println("Word: " + move.getLetters());
                        System.out.println("x:" + move.getFirstColumn() + ", y: " + move.getFirstRow());
                    }
                    command = Character.toString((char) ('A' + (char)moves.get(0).getFirstColumn())) + (moves.get(0).getFirstRow()+1) + " A " + moves.get(0).getLetters();
                }
                break;
        }
        turnCount++;
        System.out.println("Bot command: \"" + command + "\"");
        return command;
    }

    public class Gaddag{
        private HashSet<String> words;
        private final GaddagNode root = new GaddagNode();
        private int x, y;
        private final ArrayList<Word> recordedMoves = new ArrayList<>();

        Gaddag(HashSet<String> words){
            this.words = words;
            for(String word : words) {
                ArrayList<String> arcs = new ArrayList<>();
                for (int i = 1; i <= word.length(); i++) {
                    String rev = new StringBuilder(word).delete(i, word.length()).reverse().toString();
                    String suffix = word.substring(i);
                    String arc = rev + (suffix.equals("") ? "" : "#" + suffix);
                    arcs.add(arc);
                }
                for(String arc : arcs){
                    GaddagNode currentNode = root;
                    for(char c : arc.toCharArray()){
                        currentNode.addChild(c);
                        currentNode = currentNode.getChild(c);
                    }
                    currentNode.setEndOfWord();
                }
            }
        }

        GaddagNode NextArc(GaddagNode arc, char letter){
            return arc.getChild(letter);
        }

        Word sprawl(int x, int y, String rack){
            this.x = x;
            this.y = y;
            recordedMoves.clear();
            generate(0, "", rack, root);
            this.x = 0;
            this.y = 0;
            return recordedMoves.stream().max(this::compare).orElse(null);
        }

        void generate(int position, String word, String rack, GaddagNode arc){
            if(x + position >= 15 || x + position < 0) return;
            if(board.getSquareCopy(y, x + position).isOccupied()){
                char letter = board.getSquareCopy(y, x + position).getTile().getLetter();
                search(position, letter, word, rack, NextArc(arc, letter), arc);
            }else if(!rack.isEmpty()){
                for(char letter : rack.toCharArray()){
                    if(letter == Tile.BLANK) continue;
                    search(position, letter, word, rack.replace(String.valueOf(letter), ""), NextArc(arc, letter), arc);
                }
                if(rack.contains(String.valueOf(Tile.BLANK))){
                    for(char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()){
                        search(position, letter, word, rack.replace(String.valueOf(Tile.BLANK), ""), NextArc(arc, letter), arc);
                    }
                }
            }
        }

        void search(int position, char letter, String word, String rack, GaddagNode newArc, GaddagNode oldArc){
            if(position <= 0){
                word = letter + word;
                if(oldArc.isChild(letter) && !board.getSquareCopy(y, x + position).isOccupied()){
                    record(word, position - 1, oldArc);
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
                    record(word, position  - word.length() - 1, oldArc);
                }
                if(newArc != null && !board.getSquareCopy(y, x + 1).isOccupied()){
                    generate(position + 1, word, rack, newArc);
                }
            }
        }

        void record(String word, int position, Node arc){
            Word newWord = new Word(y, x + position, true, word);
            if(newWord.getLastColumn() >= 15 || newWord.getFirstColumn() < 0) return;
            if(!arc.isEndOfWord()) return;
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

        private class GaddagNode extends Node{
            // 26 letters + prefix/suffix separator '#'
            private static final int NUM_LETTERS = 27;

            private GaddagNode[] children = new GaddagNode[NUM_LETTERS];
            private boolean endOfWord;

            GaddagNode () {
                for (int i=0; i<NUM_LETTERS-1; i++) {
                    children[i] = null;
                }
                endOfWord = false;
            }

            public GaddagNode addChild (char letter) {
                int index = (letter == '#' ? 26 :((int) letter) - ((int) 'A'));
                if (children[index] == null) {
                    children[index] = new GaddagNode();
                }
                return(children[index]);
            }

            public void setEndOfWord () {
                endOfWord = true;
            }

            public GaddagNode getChild (char letter) {
                int index = (letter == '#' ? 26 :((int) letter) - ((int) 'A'));
                return children[index];
            }

            public boolean isChild (char letter) {
                int index = (letter == '#' ? 26 :((int) letter) - ((int) 'A'));
                return children[index]!=null;
            }

            public boolean isEndOfWord () {
                return endOfWord;
            }
        }

    }

}
