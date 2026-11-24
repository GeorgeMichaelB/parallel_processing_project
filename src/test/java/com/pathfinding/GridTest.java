package com.pathfinding;

import com.pathfinding.model.Cell;
import com.pathfinding.model.Grid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Grid and Cell Model Tests")
class GridTest {

    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(10, 10);
    }

    @Test
    @DisplayName("Grid initialization creates walkable cells and correct dimensions")
    void testGridInitialization() {
        assertEquals(10, grid.getRows());
        assertEquals(10, grid.getCols());

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                assertTrue(grid.isValid(r, c));
                assertTrue(grid.isWalkable(r, c));
                Cell cell = grid.getCell(r, c);
                assertNotNull(cell);
                assertEquals(r, cell.getRow());
                assertEquals(c, cell.getCol());
                assertEquals(1.0, cell.getCost(), 1e-6);
            }
        }
    }

    @Test
    @DisplayName("Boundary validation correctly rejects out-of-bounds coordinates")
    void testBoundaryValidation() {
        assertTrue(grid.isValid(0, 0));
        assertTrue(grid.isValid(9, 9));
        assertFalse(grid.isValid(-1, 0));
        assertFalse(grid.isValid(0, -1));
        assertFalse(grid.isValid(10, 0));
        assertFalse(grid.isValid(0, 10));
        assertNull(grid.getCell(-1, -1));
        assertNull(grid.getCell(10, 10));
        assertFalse(grid.isWalkable(-1, 0));
    }

    @Test
    @DisplayName("Obstacle placement and clearing works accurately")
    void testObstacleOperations() {
        grid.setObstacle(2, 3, true);
        assertFalse(grid.isWalkable(2, 3));
        assertFalse(grid.getCell(2, 3).isWalkable());

        grid.setObstacle(2, 3, false);
        assertTrue(grid.isWalkable(2, 3));

        // Place multiple obstacles and clear
        grid.setObstacle(0, 0, true);
        grid.setObstacle(5, 5, true);
        grid.clearObstacles();
        assertTrue(grid.isWalkable(0, 0));
        assertTrue(grid.isWalkable(5, 5));
    }

    @Test
    @DisplayName("4-way neighbor queries return only valid adjacent walkable cells")
    void testNeighbors4Way() {
        // Corner cell (0, 0) has 2 neighbors: (1, 0) and (0, 1)
        Cell corner = grid.getCell(0, 0);
        List<Cell> cornerNeighbors = grid.getNeighbors(corner, false);
        assertEquals(2, cornerNeighbors.size());

        // Interior cell (5, 5) has 4 neighbors
        Cell interior = grid.getCell(5, 5);
        List<Cell> interiorNeighbors = grid.getNeighbors(interior, false);
        assertEquals(4, interiorNeighbors.size());

        // Block one neighbor
        grid.setObstacle(5, 6, true);
        List<Cell> updatedNeighbors = grid.getNeighbors(interior, false);
        assertEquals(3, updatedNeighbors.size());
        assertFalse(updatedNeighbors.contains(grid.getCell(5, 6)));
    }

    @Test
    @DisplayName("Diagonal neighbors prevent cutting blocked corners")
    void testDiagonalNeighborCornerCutting() {
        Cell center = grid.getCell(5, 5);

        // With no obstacles, 8 neighbors
        List<Cell> diagNeighbors = grid.getNeighbors(center, true);
        assertEquals(8, diagNeighbors.size());

        // Block adjacent cells (4, 5) and (5, 4) - diagonal move to (4, 4) should be disallowed
        grid.setObstacle(4, 5, true);
        List<Cell> neighborsAfterWall = grid.getNeighbors(center, true);
        assertFalse(neighborsAfterWall.contains(grid.getCell(4, 4)));
    }

    @Test
    @DisplayName("Random obstacle generation adheres to target probability")
    void testRandomObstacleGeneration() {
        Grid largeGrid = new Grid(50, 50);
        largeGrid.generateRandomObstacles(0.30, new Random(123));

        int blocked = 0;
        for (int r = 0; r < 50; r++) {
            for (int c = 0; c < 50; c++) {
                if (!largeGrid.isWalkable(r, c)) {
                    blocked++;
                }
            }
        }
        double ratio = (double) blocked / (50 * 50);
        assertEquals(0.30, ratio, 0.05); // within 5% tolerance
    }

    @Test
    @DisplayName("Cell value equality and hashCode contract")
    void testCellEquality() {
        Cell c1 = new Cell(3, 4, true, 2.0);
        Cell c2 = new Cell(3, 4, false, 1.0);
        Cell c3 = new Cell(3, 5, true, 2.0);

        assertEquals(c1, c2, "Cells at same row/col should be equal");
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}
