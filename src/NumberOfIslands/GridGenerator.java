package HomeCalc;
import java.util.Random;

public class GridGenerator {
    private static final Random RANDOM = new Random();

    /**
     * Generates a 2D grid with the given number of rows and columns.
     * Each cell is randomly assigned a value of 0 (water) or 1 (land).
     *
     * @param rows The number of rows in the grid.
     * @param cols The number of columns in the grid.
     * @return A 2D int array representing the grid.
     */
    public static int[][] generateGrid(int rows, int cols) {
        int[][] grid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Randomly assign 0 or 1
                grid[i][j] = RANDOM.nextInt(2);
            }
        }

        return grid;
    }

    /**
     * Prints the grid for visualization.
     *
     * @param grid The grid to print.
     */
    public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

