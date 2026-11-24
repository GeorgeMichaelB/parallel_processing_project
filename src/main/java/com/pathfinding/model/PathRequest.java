package com.pathfinding.model;

/**
 * FILE: PathRequest.java
 * ASSIGNED TO: Member 3 (Concurrency Engineer) & Member 1
 * 
 * PURPOSE:
 * Encapsulates a single path search task:
 * references the shared Grid, a starting cell, and a goal cell.
 * Multiple PathRequest instances will be processed independently sequentially and in parallel.
 */
public class PathRequest {

    private final int id;
    private final Grid grid;
    private final Cell start;
    private final Cell goal;
    private final boolean allowDiagonal;

    /**
     * Creates a PathRequest with diagonal movement disabled by default.
     *
     * @param id   unique identifier for this request
     * @param grid the shared grid on which pathfinding is performed
     * @param start the starting cell
     * @param goal  the goal cell
     */
    public PathRequest(int id, Grid grid, Cell start, Cell goal) {
        this(id, grid, start, goal, false);
    }

    /**
     * Creates a PathRequest with explicit diagonal movement setting.
     *
     * @param id            unique identifier for this request
     * @param grid          the shared grid on which pathfinding is performed
     * @param start         the starting cell
     * @param goal          the goal cell
     * @param allowDiagonal whether diagonal movement is permitted
     */
    public PathRequest(int id, Grid grid, Cell start, Cell goal, boolean allowDiagonal) {
        this.id = id;
        this.grid = grid;
        this.start = start;
        this.goal = goal;
        this.allowDiagonal = allowDiagonal;
    }

    public int getId() {
        return id;
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell getStart() {
        return start;
    }

    public Cell getGoal() {
        return goal;
    }

    public boolean isAllowDiagonal() {
        return allowDiagonal;
    }

    @Override
    public String toString() {
        return "PathRequest#" + id + "[Start=(" + start.getRow() + "," + start.getCol()
                + "), Goal=(" + goal.getRow() + "," + goal.getCol() + ")]";
    }
}
