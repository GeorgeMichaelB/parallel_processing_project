package com.pathfinding.engine;

import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import java.util.List;

/**
 * FILE: SequentialPathfindingEngine.java
 * ASSIGNED TO: Member 3 (Concurrency Engineer)
 * 
 * PURPOSE:
 * Driver that handles a batch of PathRequest objects sequentially (one after another)
 * in the calling thread. Serves as the baseline for performance comparison.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Method execute(List<PathRequest> requests, PathFinder pathFinder):
 *    Step a: Record start time using System.nanoTime().
 *    Step b: Loop through each request in requests:
 *            - Call pathFinder.findPath(request)
 *            - Collect the resulting Path into a List<Path>.
 *    Step c: Record end time and compute elapsed nanoseconds.
 *    Step d: Return a new PathfindingResult("SequentialEngine", 1, results, elapsedNanos).
 */
public class SequentialPathfindingEngine {

    /**
     * Executes requests sequentially one by one in a single thread.
     *
     * @param requests list of pathfinding requests
     * @param pathFinder search algorithm implementation
     * @return PathfindingResult containing paths and timing metrics
     */
    public PathfindingResult execute(List<PathRequest> requests, PathFinder pathFinder) {
        // TODO: Record start time with System.nanoTime()
        // TODO: Loop through requests and call pathFinder.findPath(req)
        // TODO: Measure elapsed time
        // TODO: Return new PathfindingResult(...)
        return null;
    }
}
