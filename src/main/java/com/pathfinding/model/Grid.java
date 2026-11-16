package com.pathfinding.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    private static final int[][] DIRS_4 = {
            { -1, 0 }, // up
            { 1, 0 }, // down
            { 0, -1 }, // left
            { 0, 1 } // right
    };

    private static final int[][] DIRS_DIAGONAL = {
            { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c, true);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell getCell(int row, int col) {
        if (!isValid(row, col)) {
            return null;
        }
        return cells[row][col];
    }

    public boolean isWalkable(int row, int col) {
        return isValid(row, col) && cells[row][col].isWalkable();
    }

    public void setObstacle(int row, int col, boolean isObstacle) {
        if (isValid(row, col)) {
            double currentCost = cells[row][col].getCost();
            cells[row][col] = new Cell(row, col, !isObstacle, currentCost);
        }
    }

    public void clearObstacles() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c, true);
            }
        }
    }

    public void generateRandomObstacles(double probability, Random random) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                boolean wall = random.nextDouble() < probability;
                setObstacle(r, c, wall);
            }
        }
    }

    public List<Cell> getNeighbors(Cell cell) {
        return getNeighbors(cell, false);
    }

    public List<Cell> getNeighbors(Cell cell, boolean allowDiagonal) {
        List<Cell> neighbors = new ArrayList<>();
        if (cell == null)
            return neighbors;

        int r = cell.getRow();
        int c = cell.getCol();

        for (int[] dir : DIRS_4) {
            int newR = r + dir[0];
            int newC = c + dir[1];
            if (isWalkable(newR, newC)) {
                neighbors.add(cells[newR][newC]);
            }
        }

        if (allowDiagonal) {
            for (int[] dir : DIRS_DIAGONAL) {
                int newR = r + dir[0];
                int newC = c + dir[1];
                if (isWalkable(newR, newC)) {
                    if (isWalkable(r, newC) && isWalkable(newR, c)) {
                        neighbors.add(cells[newR][newC]);
                    }
                }
            }
        }

        return neighbors;
    }
}
