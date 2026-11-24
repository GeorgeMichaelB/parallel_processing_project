package com.pathfinding;

import com.pathfinding.algorithm.AStarPathFinder;
import com.pathfinding.algorithm.BFSPathFinder;
import com.pathfinding.algorithm.DijkstraPathFinder;
import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.experiment.PathfindingExperiment;
import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;
import com.pathfinding.ui.GridVisualizer;

import java.util.Random;

/**
 * Application entry point.
 * Parses command-line flags and dispatches to the benchmark suite, GUI, or quick demo.
 */
public class Main {

    public static void main(String[] args) {
        String mode = "benchmark";
        for (String arg : args) {
            if (arg.equals("--gui") || arg.equals("-g")) {
                mode = "gui";
                break;
            } else if (arg.equals("--quick-demo") || arg.equals("-d")) {
                mode = "quick-demo";
                break;
            }
        }

        switch (mode) {
            case "gui" -> {
                System.out.println("Launching GUI Visualizer...");
                GridVisualizer.launch(40, 40);
            }
            case "quick-demo" -> runQuickDemo();
            default -> runBenchmark();
        }
    }

    /**
     * Runs a quick comparison of BFS, Dijkstra, and A* on a small grid.
     */
    private static void runQuickDemo() {
        System.out.println("=== Quick Algorithm Demo ===");
        System.out.println();

        Grid grid = new Grid(20, 20);
        grid.generateRandomObstacles(0.20, new Random(42));

        Cell start = grid.getCell(0, 0);
        Cell goal = grid.getCell(19, 19);
        // Ensure start and goal are walkable
        grid.setObstacle(0, 0, false);
        grid.setObstacle(19, 19, false);

        PathRequest request = new PathRequest(1, grid, start, goal);

        PathFinder[] finders = {
            new BFSPathFinder(),
            new DijkstraPathFinder(),
            new AStarPathFinder()
        };

        System.out.printf("Grid: 20x20, 20%% obstacles, Start=(0,0), Goal=(19,19)%n");
        System.out.println();
        System.out.printf("%-25s | %8s | %8s | %10s%n", "Algorithm", "Length", "Cost", "Time (ms)");
        System.out.println("-".repeat(60));

        for (PathFinder finder : finders) {
            long t0 = System.nanoTime();
            Path path = finder.findPath(request);
            double ms = (System.nanoTime() - t0) / 1_000_000.0;

            if (path.exists()) {
                System.out.printf("%-25s | %8d | %8.2f | %10.4f%n",
                        finder.getName(), path.getLength(), path.getTotalCost(), ms);
            } else {
                System.out.printf("%-25s | %8s | %8s | %10.4f%n",
                        finder.getName(), "N/A", "N/A", ms);
            }
        }

        System.out.println();
        System.out.println("Quick demo complete.");
    }

    /**
     * Runs the full benchmark suite with varying batch sizes and thread counts.
     */
    private static void runBenchmark() {
        System.out.println("=== Parallel Pathfinding Benchmark Suite ===");
        System.out.println();

        Grid grid = new Grid(80, 80);
        grid.generateRandomObstacles(0.25, new Random(42));

        PathFinder pathFinder = new BFSPathFinder();

        int[] requestBatches = {10, 50, 100, 250};
        int[] threadCounts = {2, 4, 8, 16};

        PathfindingExperiment.runSuite(grid, pathFinder, requestBatches, threadCounts);

        System.out.println();
        System.out.println("Benchmark complete.");
    }
}
