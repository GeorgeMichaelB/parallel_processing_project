package com.pathfinding;

import com.pathfinding.algorithm.BFSPathFinder;
import com.pathfinding.algorithm.PathFinder;
import com.pathfinding.engine.ParallelPathfindingEngine;
import com.pathfinding.engine.PathfindingResult;
import com.pathfinding.engine.SequentialPathfindingEngine;
import com.pathfinding.experiment.PathfindingExperiment;
import com.pathfinding.model.Grid;
import com.pathfinding.model.Path;
import com.pathfinding.model.PathRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Concurrency and Parallel Engine Tests")
class ConcurrencyTest {

    @Test
    @DisplayName("Sequential and Parallel engines produce identical path results")
    void testSequentialAndParallelEquivalence() {
        Grid grid = new Grid(40, 40);
        grid.generateRandomObstacles(0.20, new Random(42));

        List<PathRequest> requests = PathfindingExperiment.generateRandomRequests(grid, 50, new Random(99), false);
        PathFinder pathFinder = new BFSPathFinder();

        // Run sequential
        SequentialPathfindingEngine seqEngine = new SequentialPathfindingEngine();
        PathfindingResult seqResult = seqEngine.execute(requests, pathFinder);

        // Run parallel with 4 threads
        try (ParallelPathfindingEngine parEngine = new ParallelPathfindingEngine(4)) {
            PathfindingResult parResult = parEngine.execute(requests, pathFinder);

            assertEquals(seqResult.getTotalRequests(), parResult.getTotalRequests());
            assertEquals(seqResult.getSuccessfulPaths(), parResult.getSuccessfulPaths());

            List<Path> seqPaths = seqResult.getPaths();
            List<Path> parPaths = parResult.getPaths();

            for (int i = 0; i < requests.size(); i++) {
                Path seqP = seqPaths.get(i);
                Path parP = parPaths.get(i);

                assertEquals(seqP.exists(), parP.exists(), "Path existence mismatch at index " + i);
                if (seqP.exists()) {
                    assertEquals(seqP.getLength(), parP.getLength(), "Path length mismatch at index " + i);
                    assertEquals(seqP.getTotalCost(), parP.getTotalCost(), 1e-6, "Cost mismatch at index " + i);
                }
            }
        }
    }

    @Test
    @DisplayName("Parallel engine scales under heavy concurrent load (100 requests, 8 threads)")
    void testConcurrentLoad() {
        Grid grid = new Grid(60, 60);
        grid.generateRandomObstacles(0.25, new Random(101));

        List<PathRequest> requests = PathfindingExperiment.generateRandomRequests(grid, 100, new Random(202), false);
        PathFinder pathFinder = new BFSPathFinder();

        try (ParallelPathfindingEngine parEngine = new ParallelPathfindingEngine(8)) {
            PathfindingResult result = parEngine.execute(requests, pathFinder);

            assertEquals(100, result.getTotalRequests());
            assertTrue(result.getSuccessfulPaths() > 0);
            assertTrue(result.getThroughput() > 0);
            assertEquals("ParallelEngine", result.getEngineType());
            assertEquals(8, result.getThreadCount());
        }
    }

    @Test
    @DisplayName("Engine lifecycle handles shutdown cleanly")
    void testEngineShutdown() {
        ParallelPathfindingEngine engine = new ParallelPathfindingEngine(2);
        Grid grid = new Grid(10, 10);
        List<PathRequest> requests = PathfindingExperiment.generateRandomRequests(grid, 5, new Random(1), false);

        PathfindingResult result = engine.execute(requests, new BFSPathFinder());
        assertEquals(5, result.getTotalRequests());

        // Clean shutdown without hanging
        assertDoesNotThrow(engine::shutdown);
    }

    @Test
    @DisplayName("Invalid thread count triggers IllegalArgumentException")
    void testInvalidThreadCount() {
        assertThrows(IllegalArgumentException.class, () -> new ParallelPathfindingEngine(0));
        assertThrows(IllegalArgumentException.class, () -> new ParallelPathfindingEngine(-4));
    }
}
