package com.pathfinding.engine;

import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;
import java.util.ArrayList;
import java.util.List;

public class SequentialPathfindingEngine {
    /**
     * Executes requests sequentially one by one in a single thread.
     * Step a: Record start time using System.nanoTime().
     * Step b: Loop through each request, call pathFinder.findPath(request), collect Path into a List<Path>.
     * Step c: Record end time and compute elapsed nanoseconds.
     * Step d: Return new PathfindingResult("SequentialEngine", 1, results, elapsedNanos).
     */
    public PathfindingResult execute(List<PathRequest> requests, PathFinder pathFinder) {
        long startTime = System.nanoTime();

        List<Path> results = new ArrayList<>();
        for (PathRequest request : requests) {
            results.add(pathFinder.findPath(request));
        }

        long elapsedNanos = System.nanoTime() - startTime;

        return new PathfindingResult("SequentialEngine", 1, results, elapsedNanos);
    }
}
