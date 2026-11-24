package com.pathfinding.experiment;

/**
 * Container storing timing, speedup, and throughput metrics for a single experimental run.
 */
public class BenchmarkResult {

    private final String mode;
    private final int threadCount;
    private final int requestCount;
    private final double timeMillis;
    private final double speedup;
    private final double throughput;
    private final int successfulPaths;

    /**
     * Constructs a BenchmarkResult with all metric fields.
     *
     * @param mode            "Sequential" or "Parallel"
     * @param threadCount     1 for sequential, 2/4/8/16 for parallel
     * @param requestCount    number of PathRequests in the batch
     * @param timeMillis      total execution time in milliseconds
     * @param speedup         T_sequential / T_parallel
     * @param throughput      requests processed per second
     * @param successfulPaths number of valid paths found
     */
    public BenchmarkResult(String mode, int threadCount, int requestCount,
                           double timeMillis, double speedup, double throughput,
                           int successfulPaths) {
        this.mode = mode;
        this.threadCount = threadCount;
        this.requestCount = requestCount;
        this.timeMillis = timeMillis;
        this.speedup = speedup;
        this.throughput = throughput;
        this.successfulPaths = successfulPaths;
    }

    public String getMode() {
        return mode;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public double getTimeMillis() {
        return timeMillis;
    }

    public double getSpeedup() {
        return speedup;
    }

    public double getThroughput() {
        return throughput;
    }

    public int getSuccessfulPaths() {
        return successfulPaths;
    }

    @Override
    public String toString() {
        return String.format("BenchmarkResult{mode='%s', threads=%d, requests=%d, " +
                        "time=%.2f ms, speedup=%.2fx, throughput=%.2f req/s, successful=%d}",
                mode, threadCount, requestCount, timeMillis, speedup, throughput, successfulPaths);
    }
}
