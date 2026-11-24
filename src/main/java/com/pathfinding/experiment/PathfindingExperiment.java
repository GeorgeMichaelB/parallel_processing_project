package com.pathfinding.experiment;

import com.pathfinding.algorithm.BFSPathFinder;
import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.engine.ParallelPathfindingEngine;
import com.pathfinding.engine.PathfindingResult;
import com.pathfinding.engine.SequentialPathfindingEngine;
import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.PathRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Benchmarking harness that runs sequential and parallel pathfinding experiments,
 * collects timing metrics, and prints a formatted summary table.
 */
public class PathfindingExperiment {

    /**
     * Generates a list of random pathfinding requests on the given grid.
     *
     * @param grid           the grid to generate requests for
     * @param count          number of requests to generate
     * @param random         random number generator for reproducibility
     * @param allowDiagonal  whether diagonal movement is allowed (unused in request generation,
     *                       reserved for future pathfinder configuration)
     * @return a list of {@link PathRequest} objects with sequential IDs starting from 1
     */
    public static List<PathRequest> generateRandomRequests(Grid grid, int count, Random random,
                                                           boolean allowDiagonal) {
        // Collect all walkable cells
        List<Cell> walkableCells = new ArrayList<>();
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols(); c++) {
                if (grid.isWalkable(r, c)) {
                    walkableCells.add(grid.getCell(r, c));
                }
            }
        }

        if (walkableCells.size() < 2) {
            throw new IllegalStateException("Not enough walkable cells to generate path requests.");
        }

        List<PathRequest> requests = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cell start = walkableCells.get(random.nextInt(walkableCells.size()));
            Cell goal;
            do {
                goal = walkableCells.get(random.nextInt(walkableCells.size()));
            } while (goal.getRow() == start.getRow() && goal.getCol() == start.getCol());

            requests.add(new PathRequest(i + 1, grid, start, goal));
        }

        return requests;
    }

    /**
     * Runs a JIT warm-up phase by executing a small batch of sequential pathfinding requests.
     *
     * @param grid       the grid to run warm-up on
     * @param pathFinder the pathfinding algorithm to warm up
     */
    public static void runJitWarmup(Grid grid, PathFinder pathFinder) {
        List<PathRequest> warmupRequests = generateRandomRequests(grid, 20, new Random(0), false);
        SequentialPathfindingEngine engine = new SequentialPathfindingEngine();
        engine.execute(warmupRequests, pathFinder);
        System.out.println("JIT warm-up complete.");
    }

    /**
     * Runs the full benchmark suite: sequential and parallel runs for each batch size
     * and thread count combination.
     *
     * @param grid          the grid to benchmark on
     * @param pathFinder    the pathfinding algorithm to benchmark
     * @param requestBatches array of batch sizes to test (e.g. {10, 50, 100, 250})
     * @param threadCounts   array of thread counts for parallel runs (e.g. {2, 4, 8, 16})
     * @return a list of {@link BenchmarkResult} objects for all runs
     */
    public static List<BenchmarkResult> runSuite(Grid grid, PathFinder pathFinder,
                                                 int[] requestBatches, int[] threadCounts) {
        runJitWarmup(grid, pathFinder);

        List<BenchmarkResult> results = new ArrayList<>();

        for (int batchSize : requestBatches) {
            List<PathRequest> requests = generateRandomRequests(grid, batchSize, new Random(42), false);

            // Sequential run
            SequentialPathfindingEngine seqEngine = new SequentialPathfindingEngine();
            PathfindingResult seqResult = seqEngine.execute(requests, pathFinder);
            double seqTimeMillis = seqResult.getExecutionTimeMillis();
            double seqThroughput = seqResult.getThroughput();
            int seqSuccessful = seqResult.getSuccessfulPaths();

            results.add(new BenchmarkResult("Sequential", 1, batchSize,
                    seqTimeMillis, 1.0, seqThroughput, seqSuccessful));

            // Parallel runs
            for (int threadCount : threadCounts) {
                ParallelPathfindingEngine parEngine = new ParallelPathfindingEngine(threadCount);
                PathfindingResult parResult = parEngine.execute(requests, pathFinder);
                double parTimeMillis = parResult.getExecutionTimeMillis();
                double parThroughput = parResult.getThroughput();
                int parSuccessful = parResult.getSuccessfulPaths();
                double speedup = seqTimeMillis / parTimeMillis;

                results.add(new BenchmarkResult("Parallel", threadCount, batchSize,
                        parTimeMillis, speedup, parThroughput, parSuccessful));

                parEngine.shutdown();
            }
        }

        printSummaryTable(results);
        return results;
    }

    /**
     * Prints a formatted ASCII summary table of all benchmark results.
     *
     * @param results the list of benchmark results to display
     */
    public static void printSummaryTable(List<BenchmarkResult> results) {
        String separator = "+------------+------------+---------+-------------+---------+--------------------+---------+";
        String header    = "| Batch Size | Mode       | Threads | Time (ms)   | Speedup | Throughput (req/s) | Success |";

        System.out.println(separator);
        System.out.println(header);
        System.out.println(separator);

        for (BenchmarkResult r : results) {
            System.out.printf("| %10d | %-10s | %7d | %11.2f | %7.2f | %18.2f | %7d |%n",
                    r.getRequestCount(),
                    r.getMode(),
                    r.getThreadCount(),
                    r.getTimeMillis(),
                    r.getSpeedup(),
                    r.getThroughput(),
                    r.getSuccessfulPaths());
        }

        System.out.println(separator);
    }

    /**
     * Main entry point. Configures the grid and runs the benchmark suite.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Grid grid = new Grid(80, 80);
        grid.generateRandomObstacles(0.25, new Random(42));

        PathFinder pathFinder = new BFSPathFinder();

        int[] requestBatches = {10, 50, 100, 250};
        int[] threadCounts = {2, 4, 8, 16};

        runSuite(grid, pathFinder, requestBatches, threadCounts);
    }
}
