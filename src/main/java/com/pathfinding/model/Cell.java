package com.pathfinding.model;

/**
 * FILE: Cell.java
 * ASSIGNED TO: Member 1 (Grid & Model Lead)
 * 
 * PURPOSE:
 * Represents an individual position/node on a 2D grid.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - row (int): the row index in the grid.
 *    - col (int): the column index in the grid.
 *    - walkable (boolean): true if movable, false if an obstacle/wall.
 *    - cost (double): movement cost through this cell (default 1.0; used for weighted paths / Dijkstra).
 * 
 * 2. Constructors:
 *    - Cell(int row, int col) -> default walkable = true, cost = 1.0.
 *    - Cell(int row, int col, boolean walkable)
 *    - Cell(int row, int col, boolean walkable, double cost)
 * 
 * 3. Getters:
 *    - getRow(), getCol(), isWalkable(), getCost()
 * 
 * 4. Methods:
 *    - equals(Object o) and hashCode(): two cells are equal if they have the same row and col.
 *    - toString(): for debugging (e.g., "(row, col)").
 * 
 * IMPORTANT CONCURRENCY RULE:
 * - DO NOT store search state here (like `visited`, `distance`, or `parent`).
 * - Cells should be read-only during concurrent searches so multiple threads can read the same cell safely.
 */
public class Cell {

    // TODO: Declare fields (row, col, walkable, cost)

    // TODO: Implement constructors

    // TODO: Implement getters

    // TODO: Implement equals(Object o) and hashCode()

    // TODO: Implement toString()
}
