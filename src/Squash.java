import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Squash implements BotAPI {

    // The public API of Bot must not change
    // This is ONLY class that you can edit in the program
    // Rename Bot to the name of your team. Use camel case.
    // Bot may not alter the state of the game objects
    // It may only inspect the state of the board and the player objects

    private final PlayerAPI me;
    private final OpponentAPI opponent;
    private final BoardAPI board;
    private final UserInterfaceAPI info;
    private final DictionaryAPI dictionary;
    private final Character START_OF_WORD = '^';
    private final Character MIDDLE_OF_WORD = '#';
    private String lastCommand;
    private int turnCount = 0;
    private Gaddag gaddag;
    private boolean stalling = false;

    Squash(PlayerAPI me, OpponentAPI opponent, BoardAPI board, UserInterfaceAPI ui, DictionaryAPI dictionary) {
        this.me = me;
        this.opponent = opponent;
        this.board = board;
        this.info = ui;
        this.dictionary = dictionary;
    }

    public String getCommand() {
        // System.out.println(String.format("%s %d - %d %s", me.getName(), me.getScore(), opponent.getScore(), opponent.getName()));
        String command = "";
        String rack = me.getFrameAsString().replaceAll("[^A-Z_]", "");
        String latestInfo = info.getLatestInfo().replaceAll("[^A-Z_]", "");
        System.out.println("rack: " + rack);
        System.out.println("latest info: " + latestInfo);
        boolean challengeIssued = false;
        if (turnCount == 0) {
            command = "NAME SQUASH";
            gaddag = new Gaddag();
        } else if (board.isFirstPlay()) {
            ArrayList<Word> validMoves = new ArrayList<>();
            getMoves(7, 7, rack, gaddag, validMoves);
            command = chooseCommand(validMoves, rack);
        } else if (latestInfo.matches("[A-O]\\d{1,2} [AD] [A-Z]+.*")) {
            String[] tokens = latestInfo.split(" ");
            String location = tokens[0];
            String direction = tokens[1];
            String word = tokens[2];
            if (tokens.length > 3) {
                String blankDesignations = tokens[3];
                for (int i = 0; i < blankDesignations.length(); i++) {
                    word = word.replace('_', blankDesignations.charAt(i));
                }
            }
            int col = Character.getNumericValue(location.charAt(0));
            int row = Character.getNumericValue(location.charAt(1));
            boolean isHorizontal = direction.equals("A");
            Word placedWord = new Word(col, row, isHorizontal, word);
            if (!dictionary.areWords(new ArrayList<Word>() {{
                add(placedWord);
            }})) {
                command = "CHALLENGE";
                challengeIssued = true;
            }
        }
        if (turnCount > 0 && !board.isFirstPlay() && !challengeIssued) {
            ArrayList<Word> validMoves = new ArrayList<>();
            HashSet<int[]> anchors = new HashSet<>();
            findAnchors(anchors);
            for (int[] anchor : anchors) {
                getMoves(anchor[0], anchor[1], rack, gaddag, validMoves);
            }
            command = chooseCommand(validMoves, rack);
        }
        // if the bot has stalled and cannot compute a next move
        if (command.equals(lastCommand)) stalling = true;
        if (stalling) command = "PASS";
        lastCommand = command;
        ++turnCount;
        System.out.println("Bot command: \"" + command + "\"");
        return command;
    }

    public void getMoves(int x, int y, String rack, Gaddag gaddag, ArrayList<Word> validMoves) {
        GaddagSprawler sprawler = new GaddagSprawler(x, y, rack, gaddag);
        ArrayList<Word> moves = sprawler.sprawl();
        for (Word move : moves) {
            if (move != null) {
                validMoves.add(move);
            }
        }
    }

    public void findAnchors(HashSet<int[]> anchors) {
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 15; c++) {
                if (board.getSquareCopy(r, c).isOccupied()) {
                    if (r - 1 >= 0 && !board.getSquareCopy(r - 1, c).isOccupied()) {
                        anchors.add(new int[]{c, r - 1});
                    }
                    if (r + 1 < 15 && !board.getSquareCopy(r + 1, c).isOccupied()) {
                        anchors.add(new int[]{c, r + 1});
                    }
                    if (c - 1 >= 0 && !board.getSquareCopy(r, c - 1).isOccupied()) {
                        anchors.add(new int[]{c - 1, r});
                    }
                    if (c + 1 < 15 && !board.getSquareCopy(r, c + 1).isOccupied()) {
                        anchors.add(new int[]{c + 1, r});
                    }
                }
            }
        }
    }

    public String chooseCommand(ArrayList<Word> validMoves, String rack) {
        String command = "";
        validMoves.removeIf(word -> {
            Frame frame = new Frame();
            ArrayList<Tile> tiles = new ArrayList<>();
            for (char c : word.getDesignatedLetters().toCharArray()) {
                tiles.add(new Tile(c));
            }
            frame.addTiles(tiles);
            return !board.isLegalPlay(frame, word);
        });
        if (validMoves.isEmpty()) {
            command = "EXCHANGE " + rack;
        } else {
            Word maxWord = validMoves.stream().max(this::compare).get();
            StringBuilder wordString = new StringBuilder();
            StringBuilder blankString = new StringBuilder();
            ArrayList<Character> rackArrayList = new ArrayList<>();
            for (char c : rack.toCharArray()) {
                rackArrayList.add(c);
            }
            int row = maxWord.getFirstRow();
            int column = maxWord.getFirstColumn();
            for (int i = 0; i < maxWord.length(); i++) {
                if (!rackArrayList.contains(maxWord.getLetter(i)) && !board.getSquareCopy(row, column).isOccupied()) {
                    wordString.append('_');
                    blankString.append(maxWord.getLetter(i));
                } else {
                    wordString.append(maxWord.getLetter(i));
                }
                rackArrayList.remove(new Character(maxWord.getLetter(i)));
                if (maxWord.isHorizontal()) column++;
                else row++;
            }
            command = String.format("%c%d %c %s %s", ('A' + (char) maxWord.getFirstColumn()), (maxWord.getFirstRow() + 1), (maxWord.isHorizontal() ? 'A' : 'D'), wordString.toString(), blankString.toString());
        }
        return command;
    }

    int compare(Word a, Word b) {
        return Integer.compare(score(a), score(b));
    }

    int score(Word word) {
        int wordValue = 0;
        int wordMultipler = 1;
        int r = word.getFirstRow();
        int c = word.getFirstColumn();
        for (int i = 0; i < word.length(); i++) {
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

    private class GaddagSprawler {
        private int x;
        private int y;
        private boolean isHorizontal;
        private final String rack;
        private final ArrayList<Word> recordedMoves;
        private final Gaddag gaddag;

        public GaddagSprawler(int x, int y, String rack, Gaddag gaddag) {
            this.x = x;
            this.y = y;
            this.rack = rack;
            this.recordedMoves = new ArrayList<>();
            this.gaddag = gaddag;
        }

        ArrayList<Word> sprawl() {
            sprawlAcross();
            sprawlDown();
            return recordedMoves;
        }

        void sprawlAcross() {
            isHorizontal = true;
            generateAcross(-1, "", rack, gaddag.root);
        }

        void sprawlDown() {
            this.isHorizontal = false;
            generateDown(-1, "", rack, gaddag.root);
        }

        void generateAcross(int position, String word, String rack, Gaddag.GaddagNode arc) {
            if (x + position >= 15 || x + position < 0) return;
            if (board.getSquareCopy(y, x + position).isOccupied()) {
                char letter = board.getSquareCopy(y, x + position).getTile().getLetter();
                searchAcross(position, letter, word, rack, arc.getTree(letter), arc);
            } else if (!rack.isEmpty()) {
                for (char letter : rack.toCharArray()) {
                    if (letter == Tile.BLANK) continue;
                    searchAcross(position, letter, word, rack.replace(String.valueOf(letter), ""), arc.getTree(letter), arc);
                }
                if (rack.contains(String.valueOf(Tile.BLANK))) {
                    for (char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                        searchAcross(position, letter, word, rack.replace(String.valueOf(Tile.BLANK), ""), arc.getTree(letter), arc);
                    }
                }
            }
        }

        void searchAcross(int position, char letter, String word, String rack, Gaddag.GaddagNode newArc, Gaddag.GaddagNode oldArc) {
            if (x + position >= 15 || x + position < 0) return;
            if (position <= 0) {
                word = letter + word;
                if (oldArc.containsTree(letter) && !board.getSquareCopy(y, x + position).isOccupied()) {
                    record(word, position, oldArc.getTree(letter));
                }
                if (newArc != null) {
                    if (!board.getSquareCopy(y, x + position).isOccupied()) {
                        generateAcross(position - 1, word, rack, newArc);
                    }
                    newArc = newArc.getTree(MIDDLE_OF_WORD);
                    if (x + 1 < 15 && newArc != null && !board.getSquareCopy(y, x + position).isOccupied() && !board.getSquareCopy(y, x + 1).isOccupied()) {
                        generateAcross(1, word, rack, newArc);
                    }
                }
            } else {
                word = word + letter;
                if (oldArc.containsTree(letter) && !board.getSquareCopy(y, x + position).isOccupied()) {
                    record(word, position, oldArc.getTree(letter));
                }
                if (newArc != null && !board.getSquareCopy(y, x + position).isOccupied()) {
                    generateAcross(position + 1, word, rack, newArc);
                }
            }
        }

        void generateDown(int position, String word, String rack, Gaddag.GaddagNode arc) {
            if (y + position >= 15 || y + position < 0) return;
            if (board.getSquareCopy(y + position, x).isOccupied()) {
                char letter = board.getSquareCopy(y + position, x).getTile().getLetter();
                searchDown(position, letter, word, rack, arc.getTree(letter), arc);
            } else if (!rack.isEmpty()) {
                for (char letter : rack.toCharArray()) {
                    if (letter == Tile.BLANK) continue;
                    searchDown(position, letter, word, rack.replace(String.valueOf(letter), ""), arc.getTree(letter), arc);
                }
                if (rack.contains(String.valueOf(Tile.BLANK))) {
                    for (char letter : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
                        searchDown(position, letter, word, rack.replace(String.valueOf(Tile.BLANK), ""), arc.getTree(letter), arc);
                    }
                }
            }
        }

        void searchDown(int position, char letter, String word, String rack, Gaddag.GaddagNode newArc, Gaddag.GaddagNode oldArc) {
            if (y + position >= 15 || y + position < 0) return;
            if (position <= 0) {
                word = letter + word;
                if (oldArc.containsTree(letter) && !board.getSquareCopy(y + position, x).isOccupied()) {
                    record(word, position, oldArc.getTree(letter));
                }
                if (newArc != null) {
                    if (!board.getSquareCopy(y + position, x).isOccupied()) {
                        generateDown(position - 1, word, rack, newArc);
                    }
                    newArc = newArc.getTree(MIDDLE_OF_WORD);
                    if (y + 1 < 15 && newArc != null && !board.getSquareCopy(y + position, x).isOccupied() && !board.getSquareCopy(y + 1, x).isOccupied()) {
                        generateDown(1, word, rack, newArc);
                    }
                }
            } else {
                word = word + letter;
                if (oldArc.containsTree(letter) && !board.getSquareCopy(y + position, x).isOccupied()) {
                    record(word, position, oldArc.getTree(letter));
                }
                if (newArc != null && !board.getSquareCopy(y + position, x).isOccupied()) {
                    generateDown(position + 1, word, rack, newArc);
                }
            }
        }

        void record(String word, int position, Gaddag.GaddagNode arc) {
            if (position >= 0) position -= word.length();
            Word newWord;
            if (isHorizontal) newWord = new Word(y, x + position, this.isHorizontal, word);
            else newWord = new Word(y + position, x, this.isHorizontal, word);
            if (newWord.getLastColumn() >= 15 || newWord.getFirstColumn() < 0) return;
            if (newWord.getLastRow() >= 15 || newWord.getFirstRow() < 0) return;
            if (!arc.isTerminal()) return;
            recordedMoves.add(newWord);
        }
    }

    public class Gaddag {
        // root node of the dictionary, initialised with value SOW
        private final GaddagNode root = new GaddagNode(START_OF_WORD);

        public Gaddag() {
            try {
                Scanner sc = new Scanner(new File("csw.txt"));
                int i = 0;
                while (sc.hasNextLine()) {
                    add(sc.nextLine());
                }
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void add(String word) {
            ArrayList<String> arcs = new ArrayList<>();
            String rev = null;
            String suffix = null;
            String arc = null;
            for (int i = 1; i <= word.length(); i++) {
                rev = new StringBuilder(word).delete(i, word.length()).reverse().toString();
                suffix = word.substring(i);
                arc = rev + (suffix.equals("") ? "" : MIDDLE_OF_WORD + suffix);
                arcs.add(arc);
                // immediately nullify references for gc optimisation
                rev = null;
                suffix = null;
                arc = null;
            }
            for (String path : arcs) {
                root.add(path);
            }
            arcs = null;
        }

        private class GaddagNode {
            private final Character data;
            private final ArrayList<GaddagNode> children;
            private boolean terminal;

            public GaddagNode(Character data) {
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

            public boolean containsTree(Character c) {
                for (GaddagNode g : children) if (g.getData() == c) return true;
                return false;
            }

            public GaddagNode getTree(Character c) {
                for (GaddagNode g : children) if (g.getData() == c) return g;
                return null;
            }

            public boolean isTerminal() {
                return terminal;
            }

            public Character getData() {
                return data;
            }

            public ArrayList<GaddagNode> getChildren() {
                return children;
            }

            public boolean contains(Character c) {
                if (containsTree(c)) return true;
                else {
                    for (GaddagNode g : children) if (g.containsTree(c)) return true;
                    return false;
                }
            }
        }

    }

}