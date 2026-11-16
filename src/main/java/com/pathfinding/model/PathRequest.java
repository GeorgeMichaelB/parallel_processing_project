package com.pathfinding.model;

/**
 * FILE: PathRequest.java
 * ASSIGNED TO: Member 3 (Concurrency Engineer) & Member 1
 * 
 * PURPOSE:
 * Encapsulates a single path search task:
 * references the shared Grid, a starting cell, and a goal cell.
 * Multiple PathRequest instances will be processed independently sequentially and in parallel.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - id (int): unique identifier for tracking/debugging the request.
 *    - grid (Grid): the grid environment to search within.
 *    - start (Cell): source starting position.
 *    - goal (Cell): target destination position.
 *    - allowDiagonal (boolean, optional/bonus): whether 8-way diagonal moves are permitted.
 * 
 * 2. Constructors:
 *    - PathRequest(int id, Grid grid, Cell start, Cell goal)
 *    - PathRequest(int id, Grid grid, Cell start, Cell goal, boolean allowDiagonal)
 * 
 * 3. Getters:
 *    - getId(), getGrid(), getStart(), getGoal(), isAllowDiagonal()
 * 
 * 4. toString():
 *    - e.g., "PathRequest#1[Start=(0,0), Goal=(10,10)]"
 */
public class PathRequest {

    // TODO: Declare fields (id, grid, start, goal, allowDiagonal)

    // TODO: Implement constructors

    // TODO: Implement getters

    // TODO: Implement toString()
}
