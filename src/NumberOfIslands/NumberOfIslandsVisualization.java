package HomeCalc;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class NumberOfIslandsVisualization extends JFrame {
    // Grid dimensions
    private static final int ROWS = 20; // Number of rows
    private static final int COLS = 20; // Number of columns
    private static final int CELL_SIZE = 40; // Pixel size of each cell

    // Colors
    private static final Color LAND_COLOR = Color.GREEN;
    private static final Color WATER_COLOR = Color.BLUE;
    private static final Color VISITED_COLOR = Color.YELLOW;

    // The grid (0 = water, 1 = land), dynamically generated
    private int[][] grid;

    // Visited array to track DFS progress
    private boolean[][] visited;

    // Stack for DFS simulation
    private Stack<int[]> dfsStack = new Stack<>();

    // Framerate control
    private static final int FRAME_DELAY = 10; // Milliseconds between frames

    // Paths to sound files
    private static final String BOOP_SOUND = "src/Utilities/boop.wav"; // Sound for land
    private static final String DING_SOUND = "src/Utilities/ding.wav"; // Sound for island

    // Grid panel
    private GridPanel gridPanel;

    // Island counter
    private int islandCounter = 0;

    public NumberOfIslandsVisualization() {
        setTitle("Number of Islands Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(COLS * CELL_SIZE + 16, ROWS * CELL_SIZE + 39); // Account for window borders
        setLocationRelativeTo(null);

        // Generate the grid dynamically
        grid = GridGenerator.generateGrid(ROWS, COLS);

        // Print the grid to the console for debugging
        System.out.println("Generated Grid:");
        GridGenerator.printGrid(grid);

        // Initialize visited array
        visited = new boolean[ROWS][COLS];

        // Initialize grid panel
        gridPanel = new GridPanel();
        add(gridPanel);

        // Start the DFS simulation
        new Thread(this::startSimulation).start();
    }

    private void startSimulation() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 1 && !visited[i][j]) {
                    // Play sound for a new island
                    SoundPlayer.playSound(DING_SOUND);
                    islandCounter++;
                    System.out.println("Island #" + islandCounter + " discovered!");
                    dfs(i, j);
                }
            }
        }
        System.out.println("Total Islands: " + islandCounter);
    }

    private void dfs(int x, int y) {
        // Push the starting point onto the stack
        dfsStack.push(new int[]{x, y});
        visited[x][y] = true;

        // Directions for moving in the grid
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        // DFS simulation
        while (!dfsStack.isEmpty()) {
            int[] cell = dfsStack.pop();
            int cx = cell[0];
            int cy = cell[1];

            for (int[] dir : directions) {
                int nx = cx + dir[0];
                int ny = cy + dir[1];

                if (nx >= 0 && ny >= 0 && nx < ROWS && ny < COLS && !visited[nx][ny] && grid[nx][ny] == 1) {
                    // Play sound for finding land
                    SoundPlayer.playSound(BOOP_SOUND);
                    visited[nx][ny] = true;
                    dfsStack.push(new int[]{nx, ny});
                }
            }

            // Redraw the grid after each step
            gridPanel.repaint();

            // Delay for visualization
            try {
                Thread.sleep(FRAME_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the grid
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLS; j++) {
                    if (grid[i][j] == 1) {
                        g.setColor(LAND_COLOR);
                    } else {
                        g.setColor(WATER_COLOR);
                    }
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK); // Grid border
                    g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    // Highlight visited cells
                    if (visited[i][j]) {
                        g.setColor(VISITED_COLOR);
                        g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberOfIslandsVisualization app = new NumberOfIslandsVisualization();
            app.setVisible(true);
        });
    }
}
