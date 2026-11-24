package com.pathfinding;

import com.pathfinding.ui.GridVisualizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.GraphicsEnvironment;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GUI Visualizer Initialization Test")
class GridVisualizerTest {

    @Test
    @DisplayName("GridVisualizer initializes components, grid, and listeners without exception")
    void testVisualizerInitialization() {
        if (!GraphicsEnvironment.isHeadless()) {
            GridVisualizer visualizer = new GridVisualizer(20, 20);
            assertNotNull(visualizer);
            assertEquals("Pathfinding Grid Visualizer", visualizer.getTitle());
            visualizer.dispose();
        }
    }
}
