package com.pathfinding.engine;

import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Concurrency engine that executes multiple PathRequests in parallel across worker threads.
 * Uses Java's ExecutorService with Callable<Path> tasks as recommended by the project specification.
 */
public class ParallelPathfindingEngine implements AutoCloseable {

    private final int threadCount;
    private final ExecutorService executor;

    /**
     * Creates a parallel pathfinding engine with the specified thread pool size.
     *
     * @param threadCount number of worker threads (must be > 0)
     */
    public ParallelPathfindingEngine(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("threadCount must be greater than 0");
        }
        this.threadCount = threadCount;
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * Executes a batch of PathRequests concurrently using Callable<Path> tasks.
     *
     * @param requests   list of pathfinding requests
     * @param pathFinder search algorithm implementation
     * @return PathfindingResult containing paths and timing metrics
     */
    public PathfindingResult execute(List<PathRequest> requests, PathFinder pathFinder) {
        List<Callable<Path>> tasks = new ArrayList<>();
        for (PathRequest req : requests) {
            tasks.add(() -> pathFinder.findPath(req));
        }

        long startTime = System.nanoTime();

        List<Path> results = new ArrayList<>();
        try {
            List<Future<Path>> futures = executor.invokeAll(tasks);
            for (Future<Path> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    results.add(Path.empty());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Fill remaining with empty paths
            while (results.size() < requests.size()) {
                results.add(Path.empty());
            }
        }

        long elapsedNanos = System.nanoTime() - startTime;

        return new PathfindingResult("ParallelEngine", threadCount, results, elapsedNanos);
    }

    /**
     * Cleanly shuts down the ExecutorService.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        shutdown();
    }

    public int getThreadCount() {
        return threadCount;
    }
}
