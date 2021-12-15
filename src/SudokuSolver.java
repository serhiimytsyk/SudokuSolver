import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {
    private final int GRID_SIZE = SudokuSolverApplication.GRID_SIZE;
    private final int SMALL_SQUARE_SIZE = (int) Math.sqrt(GRID_SIZE);

    public boolean solveSudoku(char[][] board) {
        List<Point> emptyPoints = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (board[i][j] == '.') {   //We assume that empty cells contain dots
                    Point point = new Point(i, j);
                    emptyPoints.add(point);
                }
            }
        }
        return solve(board, emptyPoints, 0);
    }

    private boolean solve(char[][] board, List<Point> emptyPoints, int depth) {
        if (depth < emptyPoints.size()) {
            Point point = emptyPoints.get(depth);
            for (int i = 1; i <= GRID_SIZE; i++) {
                board[point.x][point.y] = (char) ('0' + i);
                if (isValidSudoku(board)) {
                    if (solve(board, emptyPoints, depth + 1)) return true;
                }
            }
            board[point.x][point.y] = '.';
            return false;
        } else {
            return isValidSudoku(board);
        }
    }

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                for (int k = j + 1; k < GRID_SIZE; k++) {
                    if (board[i][j] != '.' && board[i][j] == board[i][k])
                        return false; //check if there are two equal digits in one row
                    if (board[j][i] != '.' && board[j][i] == board[k][i])
                        return false; //check if there are two equal digits in one column
                }
            }
        }
        for (int i = 0; i < SMALL_SQUARE_SIZE; i++) {
            for (int j = 0; j < SMALL_SQUARE_SIZE; j++) {
                for (int p = 0; p < GRID_SIZE; p++) {
                    for (int q = p + 1; q < GRID_SIZE; q++) {
                        int x1 = SMALL_SQUARE_SIZE * i + p / SMALL_SQUARE_SIZE;
                        int y1 = SMALL_SQUARE_SIZE * j + p % SMALL_SQUARE_SIZE;

                        int x2 = SMALL_SQUARE_SIZE * i + q / SMALL_SQUARE_SIZE;
                        int y2 = SMALL_SQUARE_SIZE * j + q % SMALL_SQUARE_SIZE;

                        if (board[x1][y1] != '.' && board[x1][y1] == board[x2][y2])
                            return false; //check if there are two equal digits in one small square
                    }
                }
            }
        }
        return true;
    }
}
