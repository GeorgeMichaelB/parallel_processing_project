package com.pathfinding.algorithm;

import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

/**
 * FILE: PathFinder.java
 * ASSIGNED TO: Member 2 (Algorithm Specialist)
 * 
 * PURPOSE:
 * The common interface for all sequential pathfinding search algorithms on the grid.
 * Demonstrates clean OOP design (Strategy Pattern) so BFS, Dijkstra, or A* can be
 * plugged interchangeably into sequential and parallel execution engines.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Interface method: Path findPath(PathRequest request);
 *    - Takes a PathRequest containing grid, start, and goal.
 *    - Returns the computed Path (or Path.empty() if no route exists).
 * 
 * 2. Interface method: String getName();
 *    - Returns the name of the algorithm (e.g. "BFS", "Dijkstra", "A*").
 * 
 * CRITICAL CONCURRENCY REQUIREMENT:
 * - Implementations MUST be thread-safe.
 * - Each call to findPath() must allocate its own local traversal data structures
 *   (e.g., local visited array, local parent map, local queue) so multiple threads
 *   can invoke findPath() concurrently without interfering with each other.
 */
public interface PathFinder {

    // TODO: Define Path findPath(PathRequest request);

    // TODO: Define String getName();
}
