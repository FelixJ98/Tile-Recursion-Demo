package PA5;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * TILE RECURSION DEMONSTRATION
 * Author: Felix Jarquin June 2022
 *
 *
 *   This experiment has a way to make the tiles appear with J-Pane so the user can visually see the tiles formed with color coordination.
 * - Before running the program be sure to edit the size of the grid under "Edit Configurations" under CLI Arguments the grid is formed with "0 0 0"
 *   add the base number of tiles to the grid for example a 4x4 grid with the graphical position (0,0) would be "4 0 0"
 * - Tiles are shown inside the console with '+', '-', '|'. to form and 'L' shaped tile wrapped around a missing tile space '*'.
 * - You can change the pixel size with "TILE_SIZE".
 * - You can change the color with " Color.".
 *
 * Enjoy the cool designs!
 */

public class main extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int TILE_SIZE = 50; // Adjust this value to change the tile size
    private static final Color COLOR_HOLE = Color.RED;
    private static final Color COLOR_PLUS = Color.BLUE;
    private static final Color COLOR_VERTICAL = Color.MAGENTA;
    private static final Color COLOR_HORIZONTAL = Color.CYAN;
    private static final Color COLOR_NULL = Color.BLACK;
    private final int n;
    private static char[][] solution;

    public main(int n, char[][] solution) {
        this.n = n;
        main.solution = solution;
        setPreferredSize(new Dimension(n * TILE_SIZE, n * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                // Determine the color based on the tile value
                char tile = solution[row][col];
                Color color = switch (tile) {
                    case '*' -> COLOR_HOLE;
                    case '+' -> COLOR_PLUS;
                    case '|' -> COLOR_VERTICAL;
                    case '-' -> COLOR_HORIZONTAL;
                    default -> COLOR_NULL;
                };

                g.setColor(color);
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }
    public static void solver(int dim, int start_row, int start_col) {
        int hole_row = 0, hole_col = 0;
        //Base case: dim = 2, fill in the base-case quadrants
        if(dim == 2) {
            //Top-left quadrant
            if(solution[start_row][start_col] != '\u0000') {
                solution[start_row + 1][start_col + 1] = '+';
                solution[start_row][start_col + 1] = '|';
                solution[start_row + 1][start_col] = '-';
            }
            //Top-right quadrant
            else if(solution[start_row][start_col + 1] != '\u0000') {
                solution[start_row + 1][start_col] = '+';
                solution[start_row][start_col] = '|';
                solution[start_row + 1][start_col + 1] = '-';
            }
            //Bottom-left quadrant
            else if(solution[start_row + 1][start_col] != '\u0000') {
                solution[start_row][start_col + 1] = '+';
                solution[start_row + 1][start_col + 1] = '|';
                solution[start_row][start_col] = '-';
            }
            //Bottom-right quadrant
            else if(solution[start_row + 1][start_col + 1] != '\u0000') {
                solution[start_row][start_col] = '+';
                solution[start_row + 1][start_col] = '|';
                solution[start_row][start_col + 1] = '-';
            }
            return;
        }
        //Find hole if n > 2
        boolean found = false;
        for(int i = start_row; i < dim + start_row; i++) {
            for(int j = start_col; j < dim + start_col; j++) {
                if(solution[i][j] != '\u0000') {
                    found = true;
                    hole_row = i;
                    hole_col = j;
                    break;
                }
            }
            if(found) break;
        }
        //Placing middle tiles
        if(hole_row - start_row < dim / 2 && hole_col - start_col < dim / 2) { //Top-left quadrant
            solution[start_row + dim / 2][start_col + dim / 2] = '+';
            solution[start_row + dim / 2][start_col + (dim / 2) - 1] = '-';
            solution[start_row + (dim / 2) - 1][start_col + dim / 2] = '|';
        } else if(hole_row - start_row < dim / 2 && hole_col - start_col >= dim / 2) { //Top-right quadrant
            solution[start_row + dim / 2][start_col + (dim / 2) - 1] = '+';
            solution[start_row + dim / 2][start_col + dim / 2] = '-';
            solution[start_row + (dim / 2) - 1][start_col + dim / 2 - 1] = '|';
        } else if(hole_row - start_row >= dim / 2 && hole_col - start_col < dim / 2) { //Bottom-left quadrant
            solution[start_row + (dim / 2) - 1][start_col + dim / 2] = '+';
            solution[start_row + (dim / 2) - 1][start_col + (dim / 2) - 1] = '-';
            solution[start_row + dim / 2][start_col + dim / 2] = '|';
        } else { //if(hole_row - start_row >= dim / 2 && hole_col - start_col >= dim / 2) //Bottom-right quadrant
            solution[start_row + (dim / 2) - 1][start_col + (dim / 2) - 1] = '+';
            solution[start_row + (dim / 2) - 1][start_col + dim / 2] = '-';
            solution[start_row + dim / 2][start_col + (dim / 2) - 1] = '|';
        }
        //Recursion of solver method within each quadrant
        solver(dim / 2, start_row, start_col); //Top-left quadrant
        solver(dim / 2, start_row, start_col + dim / 2); //Top-right quadrant
        solver(dim / 2, start_row + dim / 2, start_col); //Bottom-left quadrant
        solver(dim / 2, start_row + dim / 2, start_col + dim / 2); //Bottom-right quadrant
    }
    public static final String OUTPUT_PATH = System.getProperty("user.dir") + "/output/";


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int rowIndex = Integer.parseInt(args[1]);
        int colIndex = Integer.parseInt(args[2]);
        System.out.printf("Inputs are n = %d, missing tile is at (%d, %d)%n", n, rowIndex, colIndex);
        solution = new char[n][n];  // Initialize the solution array with the correct dimensions
        solution[rowIndex][colIndex] = '*'; // the hole
        solver(n, 0, 0);


        JFrame frame = new JFrame("Tile Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new main(n, solution));
        frame.pack();
        frame.setVisible(true);
        printSolution(OUTPUT_PATH + "out.txt", n);
    }

    private static void printSolution(String fileName, int n) {
        PrintWriter p = null;
        try {
            p = new PrintWriter(fileName);
        } catch (FileNotFoundException exp) {
            System.out.println("Fatal error: illegal output file name!");
            System.exit(1);
        }
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (col != n - 1)//not the last column
                    p.print(solution[row][col] + " ");
                else
                    p.println(solution[row][col]);
        p.flush();
        p.close();
        }
    }