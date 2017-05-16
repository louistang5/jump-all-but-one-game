import java.util.*;

public class JumpGame {

    public static int[][] board;
    public static int SIZE = 5;
    public static int steps;
    public static List<int[][]> result;

    public void playGame(int row, int n) {
        board = new int[SIZE][SIZE];
        result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], -1);
            for (int j = 0; j <= i; j++) {
                board[i][j] = 1;
            }
        }
        board[row-1][n-1] = 0; // choose initial board setting from 00,10,20,21
        System.out.println("Initial Board:");
        printBoard(board);

        steps = 0;
        backtrack();
    }

    public class Pattern {
        public int x;
        public int y;
        public String dir;
        public Pattern(int x, int y, String dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

    public void backtrack() {
        if (onesLeft() == 1) {
            for (int[][] b : result) {
                steps++;
                System.out.println("Step " + steps);
                printBoard(b);
            }
            System.exit(0);
        }
        List<Pattern> moves = findPatterns();
        for (Pattern p : moves) {
            int x = p.x;
            int y = p.y;
            if (p.dir.equals("left")) {
                board[x - 2][y] = 0;
                board[x - 1][y] = 0;
                board[x][y] = 1;
            } else if (p.dir.equals("right")) {
                board[x][y] = 1;
                board[x + 1][y] = 0;
                board[x + 2][y] = 0;
            } else if (p.dir.equals("up")) {
                board[x][y - 2] = 0;
                board[x][y - 1] = 0;
                board[x][y] = 1;
            } else if (p.dir.equals("down")){
                board[x][y] = 1;
                board[x][y + 1] = 0;
                board[x][y + 2] = 0;
            } else if (p.dir.equals("downright")) {
                board[x][y] = 1;
                board[x+1][y+1] = 0;
                board[x+2][y+2] = 0;
            } else { // upleft
                board[x-2][y-2] = 0;
                board[x-1][y-1] = 0;
                board[x][y] = 1;
            }
            int[][] copy = new int[board.length][board.length];
            for (int i = 0; i < board.length; i++) {
                copy[i] = Arrays.copyOf(board[i], board.length);
            }
            result.add(copy);
            backtrack();
            result.remove(result.get(result.size() - 1));
            if (p.dir.equals("left")) {
                board[x - 2][y] = 1;
                board[x - 1][y] = 1;
                board[x][y] = 0;
            } else if (p.dir.equals("right")) {
                board[x][y] = 0;
                board[x + 1][y] = 1;
                board[x + 2][y] = 1;
            } else if (p.dir.equals("up")) {
                board[x][y - 2] = 1;
                board[x][y - 1] = 1;
                board[x][y] = 0;
            } else if (p.dir.equals("down")){
                board[x][y] = 0;
                board[x][y + 1] = 1;
                board[x][y + 2] = 1;
            } else if (p.dir.equals("downright")) {
                board[x][y] = 0;
                board[x+1][y+1] = 1;
                board[x+2][y+2] = 1;
            } else { //upleft
                board[x-2][y-2] = 1;
                board[x-1][y-1] = 1;
                board[x][y] = 0;
            }
        }
    }

    /**
     * find patterns for next move, in any of the four directions
     * @return a list of patterns with x,y of ZERO, and direction
     * "left"  if 110
     * "right" if 011
     * similar with up and down
     */
    public List<Pattern> findPatterns() {
        List<Pattern> result = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (board[i][j] == 1) continue;
                if (i+2<board.length && board[i+1][j] == 1 && board[i+2][j] == 1)
                    result.add(new Pattern(i, j, "right"));
                if (i-2>=0 && board[i-1][j] == 1 && board[i-2][j] == 1)
                    result.add(new Pattern(i, j, "left"));
                if (j+2<board.length && board[i][j+1] == 1 && board[i][j+2] == 1)
                    result.add(new Pattern(i, j, "down"));
                if (j-2>=0 && board[i][j-1] == 1 && board[i][j-2] == 1)
                    result.add(new Pattern(i, j, "up"));
                if (i+2<board.length && j+2<board.length && board[i+1][j+1] == 1 && board[i+2][j+2] == 1)
                    result.add(new Pattern(i, j, "downright"));
                if (i-2>=0 && j-2>=0 && board[i-2][j-2] == 1 && board[i-1][j-1] == 1)
                    result.add(new Pattern(i, j, "upleft"));
            }
        }
        return result;
    }

    public int onesLeft() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j <= i; j++) {
                if (board[i][j] == 1) count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        JumpGame newGame = new JumpGame();
        System.out.println("Choose one pin to take it out.");
        System.out.println("Specify row and n, for example: playGame(3, 2) means take out 2rd pin in row 3.");
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter row number:");
        int row = reader.nextInt();
        System.out.println("Enter n:");
        int n = reader.nextInt();
        if (n <= 0 || n > 5 || row <= 0 || row > 5 || n > row) System.out.println("Invalid input, please try again.");
        else newGame.playGame(row, n);
    }

    public void printBoard(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int k = 0; k <= 4-i; k++)
                System.out.print(" ");
            for (int j = 0; j <= i; j++) {
                System.out.print(arr[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
