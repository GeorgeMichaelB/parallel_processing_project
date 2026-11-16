package com.pathfinding.engine;

import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import java.util.List;

/**
 * FILE: ParallelPathfindingEngine.java
 * ASSIGNED TO: Member 3 (Concurrency Engineer)
 * 
 * PURPOSE:
 * Concurrency engine that executes multiple PathRequests in parallel across worker threads.
 * Uses Java's ExecutorService with Callable<Path> tasks as recommended by the project specification.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - threadCount (int): size of the worker thread pool.
 *    - executor (ExecutorService): initialized via Executors.newFixedThreadPool(threadCount).
 * 
 * 2. Constructor:
 *    - ParallelPathfindingEngine(int threadCount):
 *      * Validates threadCount > 0.
 *      * Creates the ExecutorService with a fixed thread pool.
 * 
 * 3. Method execute(List<PathRequest> requests, PathFinder pathFinder):
 *    Step a: Create a List<Callable<Path>> tasks.
 *    Step b: For each PathRequest req, wrap it in a Callable:
 *            tasks.add(() -> pathFinder.findPath(req));
 *    Step c: Record start time using System.nanoTime().
 *    Step d: Call executor.invokeAll(tasks) to execute all tasks concurrently.
 *    Step e: Iterate through the resulting List<Future<Path>>, extracting future.get().
 *    Step f: Record elapsed nanoseconds.
 *    Step g: Return new PathfindingResult("ParallelEngine", threadCount, results, elapsedNanos).
 * 
 * 4. Lifecycle / Cleanup Methods:
 *    - shutdown(): cleanly shuts down the ExecutorService (executor.shutdown(), awaitTermination).
 *    - close(): optionally implement AutoCloseable so it can be used in try-with-resources.
 */
public class ParallelPathfindingEngine implements AutoCloseable {

    // TODO: Declare threadCount and ExecutorService fields

    // TODO: Constructor ParallelPathfindingEngine(int threadCount)

    /**
     * Executes a batch of PathRequests concurrently using Callable<Path> tasks.
     */
    public PathfindingResult execute(List<PathRequest> requests, PathFinder pathFinder) {
        // TODO: Wrap each PathRequest into a Callable<Path>
        // TODO: Time the execution of executor.invokeAll(tasks)
        // TODO: Extract results from Futures
        // TODO: Return new PathfindingResult(...)
        return null;
    }

    // TODO: Implement shutdown() and close() for graceful thread pool termination
    @Override
    public void close() {
        shutdown();
    }

    public void shutdown() {
        // TODO: executor.shutdown() and awaitTermination
    }
}
