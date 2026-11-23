package com.pathfinding.algorithm;

import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraPathFinder implements PathFinder {
    private static final double SQRT2 = Math.sqrt(2.0);

    private record NodeRecord(Cell cell, double distance) {
    }

    @Override
    public String getName() {
        return "Dijkstra's Algorithm";
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

        // Thread-local search state.
        int rows = grid.getRows();
        int cols = grid.getCols();
        double[][] dist = new double[rows][cols];
        for (double[] row : dist) {
            java.util.Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
        Cell[][] parent = new Cell[rows][cols];
        boolean[][] settled = new boolean[rows][cols];
        PriorityQueue<NodeRecord> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeRecord::distance));

        dist[start.getRow()][start.getCol()] = 0.0;
        pq.add(new NodeRecord(start, 0.0));

        
        boolean found = false;
        while (!pq.isEmpty()) {
            Cell current = pq.poll().cell();
            int cr = current.getRow();
            int cc = current.getCol();

            if (settled[cr][cc]) {
                continue; // stale queue entry
            }
            settled[cr][cc] = true;

            if (current.equals(goal)) {
                found = true;
                break;
            }

            for (Cell neighbor : grid.getNeighbors(current, request.isAllowDiagonal())) {
                int nr = neighbor.getRow();
                int nc = neighbor.getCol();
                if (settled[nr][nc]) {
                    continue;
                }
                boolean diagonal = nr != cr && nc != cc;
                double moveCost = neighbor.getCost() * (diagonal ? SQRT2 : 1.0);
                double tentative = dist[cr][cc] + moveCost;
                if (tentative < dist[nr][nc]) {
                    dist[nr][nc] = tentative;
                    parent[nr][nc] = current;
                    pq.add(new NodeRecord(neighbor, tentative));
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
        return new Path(route, dist[goal.getRow()][goal.getCol()]);
    }       
}
