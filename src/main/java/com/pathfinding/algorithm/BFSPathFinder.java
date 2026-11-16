package com.pathfinding.algorithm;

import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

/**
 * FILE: BFSPathFinder.java
 * ASSIGNED TO: Member 2 (Algorithm Specialist)
 * 
 * PURPOSE:
 * Breadth-First Search (BFS) pathfinding implementation for unweighted grids.
 * Guarantees finding the shortest path (minimum number of steps).
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Implement interface PathFinder.
 * 
 * 2. Method findPath(PathRequest request):
 *    Step a: Extract grid, start, and goal from request.
 *    Step b: Edge checks:
 *            - If start or goal is null or not walkable, return Path.empty().
 *            - If start.equals(goal), return a Path containing just the start cell.
 *    Step c: Allocate local data structures (DO NOT store on Grid or Cell):
 *            - boolean[][] visited = new boolean[rows][cols];
 *            - Cell[][] parent = new Cell[rows][cols]; (to reconstruct the path)
 *            - Queue<Cell> queue = new ArrayDeque<>();
 *    Step d: BFS Loop:
 *            - Add start to queue, mark visited[start.r][start.c] = true.
 *            - While queue is not empty:
 *                * Poll current cell.
 *                * If current equals goal -> break (path found!).
 *                * For each neighbor in grid.getNeighbors(current, request.isAllowDiagonal()):
 *                    if not visited[neighbor.r][neighbor.c]:
 *                        mark visited = true
 *                        set parent[neighbor.r][neighbor.c] = current
 *                        add neighbor to queue
 *    Step e: Path Reconstruction:
 *            - If goal was not reached, return Path.empty().
 *            - Backtrack from goal using the parent array back to start.
 *            - Reverse the list to get start -> goal order.
 *            - Return new Path(route, totalSteps).
 * 
 * 3. Method getName(): returns "Breadth-First Search (BFS)".
 */
public class BFSPathFinder implements PathFinder {

    // TODO: Implement getName()

    // TODO: Implement findPath(PathRequest request) following the steps outlined above
}
