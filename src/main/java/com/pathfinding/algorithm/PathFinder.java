package com.pathfinding.algorithm;

import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

/**
 * The common interface for all sequential pathfinding search algorithms on the grid.
 * Demonstrates clean OOP design (Strategy Pattern) so BFS, Dijkstra, or A* can be
 * plugged interchangeably into sequential and parallel execution engines.
 * 
 * CRITICAL CONCURRENCY REQUIREMENT:
 * - Implementations MUST be thread-safe.
 * - Each call to findPath() must allocate its own local traversal data structures
 *   (e.g., local visited array, local parent map, local queue) so multiple threads
 *   can invoke findPath() concurrently without interfering with each other.
 */
public interface PathFinder {

    /**
     * Finds a path from the start cell to the goal cell specified in the request.
     *
     * @param request the path request containing grid, start, goal, and options
     * @return the computed path, or {@link Path#empty()} if no path exists
     */
    Path findPath(PathRequest request);

    /**
     * Returns the human-readable name of this pathfinding algorithm.
     *
     * @return the algorithm name
     */
    String getName();
}
