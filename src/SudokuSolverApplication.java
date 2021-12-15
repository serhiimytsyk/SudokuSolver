import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SudokuSolverApplication {
    public static void main(String[] args) {
        SudokuSolverApplication app = new SudokuSolverApplication();
    }

    private final int FIELD_SIZE = 40;  //Size of one input field in pixels
    public static final int GRID_SIZE = 9;  //Size of the grid, must be a perfect square
    private final List<String> allowedTexts;
    private final SudokuSolver solver;

    private JFrame frame;
    private JTextField[][] cells;
    private JButton solveButton;
    private JButton clearButton;
    private JLabel noSolutionLabel;
    private JLabel copyrightLabel;

    public SudokuSolverApplication() {
        solver = new SudokuSolver();

        allowedTexts = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE; i++) allowedTexts.add(String.valueOf(i));
        allowedTexts.add(null);
        allowedTexts.add("");

        initFrame();
        initCells();
        initSolveButton();
        initClearButton();
        initNoSolutionLabel();
        initCopyrightLabel();

        frame.setVisible(true);
    }

    private void initFrame() {
        frame = new JFrame();
        frame.setSize(FIELD_SIZE * GRID_SIZE + FIELD_SIZE * 2 / 5, FIELD_SIZE * (GRID_SIZE + 2));
        frame.setLocation(300, 200);
        frame.setTitle("Sudoku solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        try {
            Image image = ImageIO.read(new File("sudoku.png"));
            frame.setIconImage(image);
        } catch (Exception e) {
            System.err.println("Unable to load an icon");
        }

    }

    private void initCells() {
        cells = new JTextField[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JTextField field = new JTextField();
                field.setSize(FIELD_SIZE, FIELD_SIZE);
                field.setLocation(FIELD_SIZE * i, FIELD_SIZE * j);
                field.setEditable(true);
                field.setFont(new Font("Serif", Font.BOLD, 34));
                frame.add(field);
                cells[i][j] = field;
            }
        }
    }

    private void initSolveButton() {
        solveButton = new JButton("Solve");
        solveButton.setSize(FIELD_SIZE * 2, FIELD_SIZE / 2);
        solveButton.setLocation(FIELD_SIZE * (GRID_SIZE - 2) / 2 , FIELD_SIZE * GRID_SIZE);
        solveButton.addActionListener(e -> {
            char[][] board = new char[GRID_SIZE][GRID_SIZE];
            boolean hasErrors = fillBoardFromGUI(board);
            if (!hasErrors)
                displaySolution(board);
        });
        frame.add(solveButton);
    }

    private void initClearButton() {
        clearButton = new JButton("Clear");
        clearButton.setSize(FIELD_SIZE * 2, FIELD_SIZE / 2);
        clearButton.setLocation(FIELD_SIZE * (GRID_SIZE - 2) , FIELD_SIZE * GRID_SIZE);
        clearButton.addActionListener(e -> clearCells());
        frame.add(clearButton);
    }

    private void initNoSolutionLabel() {
        noSolutionLabel = new JLabel("No solution");
        noSolutionLabel.setSize(FIELD_SIZE * 2, FIELD_SIZE / 2);
        noSolutionLabel.setLocation(FIELD_SIZE * GRID_SIZE / 2 - FIELD_SIZE * 3 / 4, FIELD_SIZE * GRID_SIZE + FIELD_SIZE / 2);
        noSolutionLabel.setForeground(Color.RED);
        noSolutionLabel.setVisible(false);
        frame.add(noSolutionLabel);
    }

    private void initCopyrightLabel() {
        copyrightLabel = new JLabel("© Serhii Mytsyk, 2021");
        copyrightLabel.setSize(FIELD_SIZE * 3, FIELD_SIZE / 2);
        copyrightLabel.setLocation(0, FIELD_SIZE * GRID_SIZE + FIELD_SIZE / 2);
        frame.add(copyrightLabel);
    }

    private boolean fillBoardFromGUI(char[][] board) {
        boolean hasErrors = false;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String text = cells[i][j].getText();
                if (allowedTexts.contains(text)) {
                    if (text == null || text.length() == 0)
                        board[i][j] = '.';
                    else
                        board[i][j] = (char) ('0' + Integer.parseInt(text));

                    cells[i][j].setForeground(Color.BLACK);
                } else {
                    hasErrors = true;
                    cells[i][j].setForeground(Color.RED);
                }
            }
        }
        return hasErrors;
    }

    private void displaySolution(char[][] board) {
        boolean hasSolution = solver.solveSudoku(board);
        if (hasSolution) {
            noSolutionLabel.setVisible(false);
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    cells[i][j].setText(String.valueOf(board[i][j] - '0'));
                }
            }
        } else {
            noSolutionLabel.setVisible(true);
        }
    }

    private void clearCells() {
        noSolutionLabel.setVisible(false);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells[i][j].setText("");
                cells[i][j].setForeground(Color.BLACK);
            }
        }
    }
}
