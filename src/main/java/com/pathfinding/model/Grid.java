package com.pathfinding.model;

import java.util.List;
import java.util.Random;

/**
 * FILE: Grid.java
 * ASSIGNED TO: Member 1 (Grid & Model Lead)
 * 
 * PURPOSE:
 * Represents the 2D grid/map containing cells, obstacles, and boundary dimensions.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Fields:
 *    - rows (int): number of rows in the grid.
 *    - cols (int): number of columns in the grid.
 *    - cells (Cell[][]): 2D array holding all Cell instances.
 * 
 * 2. Constructor:
 *    - Grid(int rows, int cols): initialize the 2D array and instantiate each Cell(r, c).
 * 
 * 3. Core Methods to Implement:
 *    - getRows(), getCols()
 *    - isValid(int r, int c): returns true if (r, c) is inside grid bounds [0, rows) x [0, cols).
 *    - getCell(int r, int c): returns the Cell at (r, c) (with bounds check).
 *    - isWalkable(int r, int c): returns true if within bounds and cell is not an obstacle.
 *    - setObstacle(int r, int c, boolean isObstacle): sets a wall or clears it.
 *    - setCost(int r, int c, double cost): updates traversal cost (for bonus weighted Dijkstra).
 *    - getNeighbors(Cell cell, boolean allowDiagonal): returns a List<Cell> of adjacent walkable neighbors.
 *      * 4-directional moves: Up (-1, 0), Down (+1, 0), Left (0, -1), Right (0, +1).
 *      * Optional 8-directional: includes diagonals (check for corner cutting).
 *    - generateRandomObstacles(double probability, Random random): randomly places obstacles based on probability.
 *    - clearObstacles(): resets all cells to walkable.
 * 
 * THREAD SAFETY:
 * - Neighbor and walkability queries should be read-only so multiple threads can query the grid simultaneously.
 */
public class Grid {

    // TODO: Declare fields (rows, cols, Cell[][] cells)

    // TODO: Constructor Grid(int rows, int cols)

    // TODO: Bounds and walkability checks: isValid(r, c), isWalkable(r, c), getCell(r, c)

    // TODO: Query neighbors: getNeighbors(Cell cell, boolean allowDiagonal)

    // TODO: Obstacle setters and random obstacle generator: setObstacle, generateRandomObstacles, clearObstacles
}
