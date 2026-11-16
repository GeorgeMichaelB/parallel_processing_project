package com.pathfinding.model;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Cell> cells;
    private final boolean exists;
    private final double totalCost;

    public Path(List<Cell> cells, double totalCost) {
        if (cells != null && !cells.isEmpty()) {
            this.cells = new ArrayList<>(cells);
            this.exists = true;
            this.totalCost = totalCost;
        } else {
            this.cells = new ArrayList<>();
            this.exists = false;
            this.totalCost = Double.POSITIVE_INFINITY;
        }
    }

    public static Path empty() {
        return new Path(null, Double.POSITIVE_INFINITY);
    }

    public boolean exists() {
        return exists;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public int getLength() {
        return cells.size();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Cell getStart() {
        if (!exists)
            return null;
        return cells.get(0);
    }

    public Cell getGoal() {
        if (!exists)
            return null;
        return cells.get(cells.size() - 1);
    }

    @Override
    public String toString() {
        if (!exists) {
            return "Path[No path found]";
        }
        return "Path[Length=" + getLength() + ", Cost=" + totalCost +
                ", Start=" + getStart() + ", Goal=" + getGoal() + "]";
    }
}
