package com.pathfinding.experiment;

/**
 * FILE: BenchmarkResult.java
 * ASSIGNED TO: Member 4 (Benchmark & Metrics Lead)
 * 
 * PURPOSE:
 * Container storing timing, speedup, and throughput metrics for a single experimental run.
 * Used to organize tabular data for the 3-4 page academic report.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - mode (String): "Sequential" or "Parallel".
 *    - threadCount (int): 1 for sequential, 2/4/8/16 for parallel.
 *    - requestCount (int): number of PathRequests in the batch (e.g. 10, 50, 100, 250).
 *    - timeMillis (double): total execution time in milliseconds.
 *    - speedup (double): T_sequential / T_parallel.
 *    - throughput (double): requests processed per second.
 *    - successfulPaths (int): number of valid paths found.
 * 
 * 2. Constructor:
 *    - BenchmarkResult(String mode, int threadCount, int requestCount, double timeMillis,
 *                      double speedup, double throughput, int successfulPaths)
 * 
 * 3. Getters for all fields.
 */
public class BenchmarkResult {

    // TODO: Declare fields

    // TODO: Implement constructor

    // TODO: Implement getters
}
