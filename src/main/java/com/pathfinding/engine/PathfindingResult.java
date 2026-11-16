package com.pathfinding.engine;

import com.pathfinding.model.Path;

import java.util.List;

/**
 * FILE: PathfindingResult.java
 * ASSIGNED TO: Member 3 (Concurrency Engineer) & Member 4 (Benchmark Lead)
 * 
 * PURPOSE:
 * Encapsulates the execution metrics and output results of running a batch of PathRequests.
 * Stores throughput, total execution time, thread configuration, and the computed paths.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - engineType (String): e.g. "SequentialEngine" or "ParallelEngine".
 *    - threadCount (int): number of threads utilized (1 for sequential).
 *    - totalRequests (int): total number of PathRequests processed.
 *    - successfulPaths (int): count of requests where a valid path was found.
 *    - executionTimeNanos (long): elapsed wall-clock time in nanoseconds.
 *    - paths (List<Path>): list of resulting Path objects.
 * 
 * 2. Constructor:
 *    - PathfindingResult(String engineType, int threadCount, List<Path> paths, long executionTimeNanos)
 *      * Calculate successfulPaths by iterating through paths and counting those where path.exists() == true.
 * 
 * 3. Query & Metric Methods:
 *    - getEngineType(): String
 *    - getThreadCount(): int
 *    - getTotalRequests(): int
 *    - getSuccessfulPaths(): int
 *    - getExecutionTimeNanos(): long
 *    - getExecutionTimeMillis(): double (executionTimeNanos / 1,000,000.0)
 *    - getThroughput(): double (totalRequests / executionTimeInSeconds)
 *    - getPaths(): List<Path>
 *    - toString(): formatted summary string.
 */
public class PathfindingResult {

    // TODO: Declare fields

    // TODO: Constructor PathfindingResult(...)

    // TODO: Implement metric getters: getExecutionTimeMillis(), getThroughput(), etc.

    // TODO: toString()
}
