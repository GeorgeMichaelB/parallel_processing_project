package com.pathfinding.algorithm;

import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/*
 * Breadth-First Search pathfinder for unweighted grids.
 */
public class BFSPathFinder implements PathFinder {

    @Override
    public String getName() {
        return "Breadth-First Search (BFS)";
    }

    @Override
    public Path findPath(PathRequest request) {
        if (request == null) {
            return Path.empty();
        }

        Grid grid = request.getGrid();
        Cell start = request.getStart();
        Cell goal = request.getGoal();

        // Edge cases: missing/blocked/out-of-bounds endpoints.
        if (grid == null || start == null || goal == null
                || !grid.isWalkable(start.getRow(), start.getCol())
                || !grid.isWalkable(goal.getRow(), goal.getCol())) {
            return Path.empty();
        }

        // Use the grid's own cell instances so lookups are consistent.
        start = grid.getCell(start.getRow(), start.getCol());
        goal = grid.getCell(goal.getRow(), goal.getCol());

        if (start.equals(goal)) {
            List<Cell> single = new ArrayList<>(1);
            single.add(start);
            return new Path(single, 0);
        }

        // Thread-local search state (never stored on Grid or Cell).
        int rows = grid.getRows();
        int cols = grid.getCols();
        boolean[][] visited = new boolean[rows][cols];
        Cell[][] parent = new Cell[rows][cols];
        Queue<Cell> queue = new ArrayDeque<>();

        visited[start.getRow()][start.getCol()] = true;
        queue.add(start);

        boolean found = false;
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            if (current.equals(goal)) {
                found = true;
                break;
            }
            for (Cell neighbor : grid.getNeighbors(current, request.isAllowDiagonal())) {
                int r = neighbor.getRow();
                int c = neighbor.getCol();
                if (!visited[r][c]) {
                    visited[r][c] = true;
                    parent[r][c] = current;
                    queue.add(neighbor);
                }
            }
        }

        if (!found) {
            return Path.empty();
        }

        // Backtrack from goal to start, then reverse into start -> goal order.
        List<Cell> route = new ArrayList<>();
        for (Cell at = goal; at != null; at = parent[at.getRow()][at.getCol()]) {
            route.add(at);
        }
        Collections.reverse(route);

        int totalSteps = route.size() - 1;
        return new Path(route, totalSteps);
    }
}
