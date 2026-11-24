package com.pathfinding.ui;

import com.pathfinding.algorithm.AStarPathFinder;
import com.pathfinding.algorithm.BFSPathFinder;
import com.pathfinding.algorithm.DijkstraPathFinder;
import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.engine.ParallelPathfindingEngine;
import com.pathfinding.engine.PathfindingResult;
import com.pathfinding.engine.SequentialPathfindingEngine;
import com.pathfinding.experiment.PathfindingExperiment;
import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Interactive Swing GUI for visualizing the 2D grid, drawing obstacles,
 * marking start/goal nodes, displaying computed paths, and running
 * sequential vs. parallel comparisons live.
 */
public class GridVisualizer extends JFrame {

    private final Grid grid;
    private Cell startCell;
    private Cell goalCell;
    private List<Path> currentPaths;
    private final Set<Cell> pathCells;

    // UI components
    private final GridCanvas canvas;
    private final JComboBox<String> algorithmSelector;
    private final JComboBox<Integer> threadSelector;
    private final JLabel statusLabel;

    /**
     * Constructs the visualizer for a grid of the given dimensions.
     *
     * @param rows number of grid rows
     * @param cols number of grid columns
     */
    public GridVisualizer(int rows, int cols) {
        super("Pathfinding Grid Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.grid = new Grid(rows, cols);
        this.startCell = grid.getCell(0, 0);
        this.goalCell = grid.getCell(rows - 1, cols - 1);
        this.currentPaths = new ArrayList<>();
        this.pathCells = new HashSet<>();

        // --- Toolbar ---
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        toolbar.add(new JLabel("Algorithm:"));
        algorithmSelector = new JComboBox<>(new String[]{"BFS", "Dijkstra", "A*"});
        toolbar.add(algorithmSelector);

        toolbar.add(new JLabel("Threads:"));
        threadSelector = new JComboBox<>(new Integer[]{2, 4, 8, 16});
        toolbar.add(threadSelector);

        JButton findPathBtn = new JButton("Find Path");
        findPathBtn.addActionListener(e -> onFindPath());
        toolbar.add(findPathBtn);

        JButton batchSeqBtn = new JButton("Run Batch (Seq)");
        batchSeqBtn.addActionListener(e -> onRunBatchSequential());
        toolbar.add(batchSeqBtn);

        JButton batchParBtn = new JButton("Run Batch (Par)");
        batchParBtn.addActionListener(e -> onRunBatchParallel());
        toolbar.add(batchParBtn);

        JButton randomizeBtn = new JButton("Randomize");
        randomizeBtn.addActionListener(e -> onRandomize());
        toolbar.add(randomizeBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> onClear());
        toolbar.add(clearBtn);

        // --- Canvas ---
        canvas = new GridCanvas();
        canvas.setPreferredSize(new Dimension(600, 600));

        // --- Status bar ---
        statusLabel = new JLabel("Ready. Left-click: toggle wall | Shift+click: set start | Right-click: set goal");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        // --- Layout ---
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Returns the PathFinder implementation selected in the dropdown.
     */
    private PathFinder getSelectedPathFinder() {
        String name = (String) algorithmSelector.getSelectedItem();
        return switch (name) {
            case "Dijkstra" -> new DijkstraPathFinder();
            case "A*" -> new AStarPathFinder();
            default -> new BFSPathFinder();
        };
    }

    /**
     * Returns the thread count selected in the dropdown.
     */
    private int getSelectedThreadCount() {
        return (Integer) threadSelector.getSelectedItem();
    }

    /**
     * Rebuilds the set of cells that lie on any currently displayed path.
     */
    private void rebuildPathCells() {
        pathCells.clear();
        for (Path p : currentPaths) {
            if (p.exists()) {
                pathCells.addAll(p.getCells());
            }
        }
    }

    // ---- Event Handlers ----

    private void onFindPath() {
        PathFinder finder = getSelectedPathFinder();
        PathRequest req = new PathRequest(1, grid, startCell, goalCell);

        long t0 = System.nanoTime();
        Path path = finder.findPath(req);
        double ms = (System.nanoTime() - t0) / 1_000_000.0;

        currentPaths.clear();
        currentPaths.add(path);
        rebuildPathCells();
        canvas.repaint();

        if (path.exists()) {
            statusLabel.setText(String.format("[%s] Path found — length=%d, cost=%.2f, time=%.2f ms",
                    finder.getName(), path.getLength(), path.getTotalCost(), ms));
        } else {
            statusLabel.setText(String.format("[%s] No path found — time=%.2f ms", finder.getName(), ms));
        }
    }

    private void onRunBatchSequential() {
        statusLabel.setText("Running sequential batch...");
        new SwingWorker<Void, Void>() {
            PathfindingResult result;

            @Override
            protected Void doInBackground() {
                PathFinder finder = getSelectedPathFinder();
                List<PathRequest> requests = PathfindingExperiment.generateRandomRequests(grid, 50, new Random(), false);
                SequentialPathfindingEngine engine = new SequentialPathfindingEngine();
                result = engine.execute(requests, finder);
                return null;
            }

            @Override
            protected void done() {
                currentPaths = result.getPaths();
                rebuildPathCells();
                canvas.repaint();
                statusLabel.setText(String.format("[Sequential] %d requests — %d successful, time=%.2f ms, throughput=%.0f req/s",
                        result.getTotalRequests(), result.getSuccessfulPaths(),
                        result.getExecutionTimeMillis(), result.getThroughput()));
            }
        }.execute();
    }

    private void onRunBatchParallel() {
        statusLabel.setText("Running parallel batch...");
        new SwingWorker<Void, Void>() {
            PathfindingResult result;

            @Override
            protected Void doInBackground() {
                PathFinder finder = getSelectedPathFinder();
                int threads = getSelectedThreadCount();
                List<PathRequest> requests = PathfindingExperiment.generateRandomRequests(grid, 50, new Random(), false);
                ParallelPathfindingEngine engine = new ParallelPathfindingEngine(threads);
                result = engine.execute(requests, finder);
                engine.shutdown();
                return null;
            }

            @Override
            protected void done() {
                currentPaths = result.getPaths();
                rebuildPathCells();
                canvas.repaint();
                statusLabel.setText(String.format("[Parallel %d threads] %d requests — %d successful, time=%.2f ms, throughput=%.0f req/s",
                        getSelectedThreadCount(), result.getTotalRequests(), result.getSuccessfulPaths(),
                        result.getExecutionTimeMillis(), result.getThroughput()));
            }
        }.execute();
    }

    private void onRandomize() {
        grid.generateRandomObstacles(0.25, new Random());
        // Ensure start and goal are walkable
        grid.setObstacle(startCell.getRow(), startCell.getCol(), false);
        grid.setObstacle(goalCell.getRow(), goalCell.getCol(), false);
        currentPaths.clear();
        pathCells.clear();
        canvas.repaint();
        statusLabel.setText("Obstacles randomized (25% density). Start and goal kept clear.");
    }

    private void onClear() {
        grid.clearObstacles();
        currentPaths.clear();
        pathCells.clear();
        canvas.repaint();
        statusLabel.setText("Grid and paths cleared.");
    }

    // ---- Custom Canvas ----

    private class GridCanvas extends JPanel {

        GridCanvas() {
            setBackground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int rows = grid.getRows();
                    int cols = grid.getCols();
                    int cellW = Math.max(1, getWidth() / cols);
                    int cellH = Math.max(1, getHeight() / rows);
                    int col = e.getX() / cellW;
                    int row = e.getY() / cellH;

                    if (!grid.isValid(row, col)) return;

                    if (e.isShiftDown() && SwingUtilities.isLeftMouseButton(e)) {
                        // Shift + left-click: set start
                        grid.setObstacle(row, col, false);
                        startCell = grid.getCell(row, col);
                        statusLabel.setText("Start set to (" + row + ", " + col + ")");
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        // Right-click: set goal
                        grid.setObstacle(row, col, false);
                        goalCell = grid.getCell(row, col);
                        statusLabel.setText("Goal set to (" + row + ", " + col + ")");
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        // Left-click: toggle obstacle
                        boolean isCurrentlyWalkable = grid.isWalkable(row, col);
                        grid.setObstacle(row, col, isCurrentlyWalkable);
                        statusLabel.setText("Toggled wall at (" + row + ", " + col + ")");
                    }

                    // Clear paths after grid modification
                    currentPaths.clear();
                    pathCells.clear();
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int rows = grid.getRows();
            int cols = grid.getCols();
            int cellW = Math.max(1, getWidth() / cols);
            int cellH = Math.max(1, getHeight() / rows);

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    int x = c * cellW;
                    int y = r * cellH;

                    Cell cell = grid.getCell(r, c);

                    // Choose fill color
                    if (!cell.isWalkable()) {
                        g.setColor(Color.DARK_GRAY);
                    } else if (cell.equals(startCell)) {
                        g.setColor(new Color(0, 200, 0));       // green
                    } else if (cell.equals(goalCell)) {
                        g.setColor(new Color(220, 50, 50));     // red
                    } else if (pathCells.contains(cell)) {
                        g.setColor(new Color(0, 200, 255));     // cyan
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    g.fillRect(x, y, cellW, cellH);

                    // Grid lines
                    g.setColor(new Color(200, 200, 200));
                    g.drawRect(x, y, cellW, cellH);
                }
            }
        }
    }

    /**
     * Launches the GridVisualizer on the Swing Event Dispatch Thread.
     *
     * @param rows number of grid rows
     * @param cols number of grid columns
     */
    public static void launch(int rows, int cols) {
        SwingUtilities.invokeLater(() -> {
            GridVisualizer visualizer = new GridVisualizer(rows, cols);
            visualizer.setVisible(true);
        });
    }
}
