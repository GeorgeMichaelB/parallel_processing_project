package com.pathfinding.algorithm;

import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarPathFinder implements PathFinder {

    private static final double SQRT2 = Math.sqrt(2.0);

    /** Priority-queue entry ordered by fScore = g + h. */
    private record NodeRecord(Cell cell, double fScore) {
    }

    @Override
    public String getName() {
        return "A* (A-Star) Search";
    }

     double heuristic(Cell a, Cell b, boolean allowDiagonal) {
        int dx = Math.abs(a.getRow() - b.getRow());
        int dy = Math.abs(a.getCol() - b.getCol());
        if (allowDiagonal) {
            return (dx + dy) + (SQRT2 - 2.0) * Math.min(dx, dy);
        }
        return dx + dy;
    }

     @Override
    public Path findPath(PathRequest request) {
        if (request == null) {
            return Path.empty();
        }

        Grid grid = request.getGrid();
        Cell start = request.getStart();
        Cell goal = request.getGoal();

        if (grid == null || start == null || goal == null
                || !grid.isWalkable(start.getRow(), start.getCol())
                || !grid.isWalkable(goal.getRow(), goal.getCol())) {
            return Path.empty();
        }

        start = grid.getCell(start.getRow(), start.getCol());
        goal = grid.getCell(goal.getRow(), goal.getCol());

        if (start.equals(goal)) {
            List<Cell> single = new ArrayList<>(1);
            single.add(start);
            return new Path(single, 0.0);
        }

         boolean diag = request.isAllowDiagonal();

        // Thread-local search state.
        int rows = grid.getRows();
        int cols = grid.getCols();
        double[][] gScore = new double[rows][cols];
        for (double[] row : gScore) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
        Cell[][] parent = new Cell[rows][cols];
        boolean[][] closed = new boolean[rows][cols];
        PriorityQueue<NodeRecord> open = new PriorityQueue<>(Comparator.comparingDouble(NodeRecord::fScore));

        gScore[start.getRow()][start.getCol()] = 0.0;
        open.add(new NodeRecord(start, heuristic(start, goal, diag)));

        boolean found = false;
        while (!open.isEmpty()) {
            Cell current = open.poll().cell();
            int cr = current.getRow();
            int cc = current.getCol();

         if (closed[cr][cc]) {
                continue; // stale queue entry
            }
            closed[cr][cc] = true;

            if (current.equals(goal)) {
                found = true;
                break;
            }

            for (Cell neighbor : grid.getNeighbors(current, diag)) {
                int nr = neighbor.getRow();
                int nc = neighbor.getCol();
                if (closed[nr][nc]) {
                    continue;
                }
                boolean diagonalMove = nr != cr && nc != cc;
                double moveCost = neighbor.getCost() * (diagonalMove ? SQRT2 : 1.0);
                double tentative = gScore[cr][cc] + moveCost;
                if (tentative < gScore[nr][nc]) {
                    gScore[nr][nc] = tentative;
                    parent[nr][nc] = current;
                    open.add(new NodeRecord(neighbor, tentative + heuristic(neighbor, goal, diag)));
                }
            }
        }

     if (!found) {
            return Path.empty();
        }

        List<Cell> route = new ArrayList<>();
        for (Cell at = goal; at != null; at = parent[at.getRow()][at.getCol()]) {
            route.add(at);
        }
        Collections.reverse(route);
        return new Path(route, gScore[goal.getRow()][goal.getCol()]);
    }
} 
