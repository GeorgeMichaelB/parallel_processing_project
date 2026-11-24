package com.pathfinding.engine;

import com.pathfinding.model.Path;

import java.util.List;

/**
 * FILE: PathfindingResult.java
 * 
 * Holds the aggregate results of running a pathfinding engine over a set of
 * {@link com.pathfinding.model.PathRequest}s, including timing statistics and
 * the resulting {@link Path} objects.
 */
public class PathfindingResult {

    private final String engineType;
    private final int threadCount;
    private final int totalRequests;
    private final int successfulPaths;
    private final long executionTimeNanos;
    private final List<Path> paths;

    /**
     * Constructs a PathfindingResult.
     *
     * @param engineType         descriptive name of the engine (e.g. "SequentialEngine")
     * @param threadCount        number of threads utilized (1 for sequential)
     * @param paths              list of resulting Path objects
     * @param executionTimeNanos elapsed wall-clock time in nanoseconds
     */
    public PathfindingResult(String engineType, int threadCount, List<Path> paths, long executionTimeNanos) {
        this.engineType = engineType;
        this.threadCount = threadCount;
        this.paths = paths;
        this.executionTimeNanos = executionTimeNanos;
        this.totalRequests = paths.size();

        int count = 0;
        for (Path path : paths) {
            if (path.exists()) {
                count++;
            }
        }
        this.successfulPaths = count;
    }

    public String getEngineType() {
        return engineType;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getSuccessfulPaths() {
        return successfulPaths;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }

    /**
     * Returns the execution time converted to milliseconds.
     *
     * @return execution time in milliseconds
     */
    public double getExecutionTimeMillis() {
        return executionTimeNanos / 1_000_000.0;
    }

    /**
     * Returns the throughput in requests per second.
     *
     * @return number of requests processed per second
     */
    public double getThroughput() {
        return totalRequests / (executionTimeNanos / 1_000_000_000.0);
    }

    public List<Path> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        return String.format(
                "PathfindingResult{engine='%s', threads=%d, requests=%d, successful=%d, " +
                "time=%.2f ms, throughput=%.2f req/s}",
                engineType, threadCount, totalRequests, successfulPaths,
                getExecutionTimeMillis(), getThroughput());
    }
}
