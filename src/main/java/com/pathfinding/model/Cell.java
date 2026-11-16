package com.pathfinding.model;

import java.util.Objects;

public class Cell {
    private final int row;
    private final int col;
    private final boolean walkable;
    private final double cost;

    public Cell(int row, int col) {
        this(row, col, true, 1.0);
    }

    public Cell(int row, int col, boolean walkable) {
        this(row, col, walkable, 1.0);
    }

    public Cell(int row, int col, boolean walkable, double cost) {
        this.row = row;
        this.col = col;
        this.walkable = walkable;
        this.cost = cost;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return walkable ? "(" + row + ", " + col + ")" : "(" + row + ", " + col + ")[WALL]";
    }
}
