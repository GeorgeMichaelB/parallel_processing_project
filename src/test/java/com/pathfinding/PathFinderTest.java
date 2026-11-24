package com.pathfinding;

import com.pathfinding.algorithm.AStarPathFinder;
import com.pathfinding.algorithm.BFSPathFinder;
import com.pathfinding.algorithm.DijkstraPathFinder;
import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pathfinding Algorithms Test Suite")
class PathFinderTest {

    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(10, 10);
    }

    static Stream<PathFinder> providePathFinders() {
        return Stream.of(
                new BFSPathFinder(),
                new DijkstraPathFinder(),
                new AStarPathFinder()
        );
    }

    @ParameterizedTest
    @MethodSource("providePathFinders")
    @DisplayName("Finds straight-line shortest path on empty grid")
    void testDirectPath(PathFinder finder) {
        Cell start = grid.getCell(0, 0);
        Cell goal = grid.getCell(0, 5);
        PathRequest req = new PathRequest(1, grid, start, goal);

        Path path = finder.findPath(req);

        assertTrue(path.exists(), finder.getName() + " should find path");
        assertEquals(6, path.getLength()); // 6 cells: (0,0) to (0,5)
        assertEquals(5.0, path.getTotalCost(), 1e-6);
        assertEquals(start, path.getStart());
        assertEquals(goal, path.getGoal());
    }

    @ParameterizedTest
    @MethodSource("providePathFinders")
    @DisplayName("Handles identical start and goal nodes")
    void testStartEqualsGoal(PathFinder finder) {
        Cell cell = grid.getCell(3, 3);
        PathRequest req = new PathRequest(1, grid, cell, cell);

        Path path = finder.findPath(req);

        assertTrue(path.exists());
        assertEquals(1, path.getLength());
        assertEquals(0.0, path.getTotalCost(), 1e-6);
        assertEquals(cell, path.getStart());
        assertEquals(cell, path.getGoal());
    }

    @ParameterizedTest
    @MethodSource("providePathFinders")
    @DisplayName("Navigates around a wall obstacle")
    void testNavigateAroundObstacle(PathFinder finder) {
        // Create a wall in row 2 from col 0 to 8
        for (int c = 0; c < 9; c++) {
            grid.setObstacle(2, c, true);
        }

        Cell start = grid.getCell(0, 0);
        Cell goal = grid.getCell(4, 0);
        PathRequest req = new PathRequest(1, grid, start, goal);

        Path path = finder.findPath(req);

        assertTrue(path.exists());
        // Verify path does not step on any obstacle
        for (Cell c : path.getCells()) {
            assertTrue(c.isWalkable(), "Path should only contain walkable cells");
        }
        assertEquals(start, path.getStart());
        assertEquals(goal, path.getGoal());
    }

    @ParameterizedTest
    @MethodSource("providePathFinders")
    @DisplayName("Returns empty path when goal is completely enclosed by obstacles")
    void testUnreachableGoal(PathFinder finder) {
        // Enclose (9, 9)
        grid.setObstacle(8, 9, true);
        grid.setObstacle(9, 8, true);

        Cell start = grid.getCell(0, 0);
        Cell goal = grid.getCell(9, 9);
        PathRequest req = new PathRequest(1, grid, start, goal);

        Path path = finder.findPath(req);

        assertFalse(path.exists());
        assertEquals(0, path.getLength());
        assertTrue(Double.isInfinite(path.getTotalCost()));
    }

    @ParameterizedTest
    @MethodSource("providePathFinders")
    @DisplayName("Gracefully returns empty path for invalid/blocked requests")
    void testInvalidRequests(PathFinder finder) {
        // Block start
        grid.setObstacle(0, 0, true);
        PathRequest blockedStart = new PathRequest(1, grid, grid.getCell(0, 0), grid.getCell(5, 5));
        assertFalse(finder.findPath(blockedStart).exists());

        // Null request
        assertFalse(finder.findPath(null).exists());
    }

    @Test
    @DisplayName("Dijkstra chooses lower-cost path over shorter hop count")
    void testDijkstraWeightedTerrain() {
        Grid customGrid = new Grid(3, 3);
        // Start (0,0), Goal (0,2). Direct path via (0,1) costs 100.0
        customGrid.setCost(0, 1, 100.0);

        DijkstraPathFinder dijkstra = new DijkstraPathFinder();
        PathRequest req = new PathRequest(1, customGrid, customGrid.getCell(0, 0), customGrid.getCell(0, 2));

        Path path = dijkstra.findPath(req);
        assertTrue(path.exists());
        // Verify path avoided high cost cell (0, 1) by going around through row 1
        assertFalse(path.getCells().contains(customGrid.getCell(0, 1)));
        assertTrue(path.getTotalCost() < 100.0);
    }

    @Test
    @DisplayName("A* supports 8-way diagonal movement")
    void testAStarDiagonalMovement() {
        Cell start = grid.getCell(0, 0);
        Cell goal = grid.getCell(4, 4);
        PathRequest diagReq = new PathRequest(1, grid, start, goal, true);

        AStarPathFinder astar = new AStarPathFinder();
        Path diagPath = astar.findPath(diagReq);

        assertTrue(diagPath.exists());
        // Diagonal path directly from (0,0) to (4,4) is 5 cells: (0,0), (1,1), (2,2), (3,3), (4,4)
        assertEquals(5, diagPath.getLength());
    }
}
