package com.pathfinding.ui;

import javax.swing.JFrame;

/**
 * FILE: GridVisualizer.java
 * ASSIGNED TO: Member 5 (GUI & Visualization Lead)
 * (Fulfills Bonus Requirement: Up to +3 Points for GUI/Visualizer)
 * 
 * PURPOSE:
 * Interactive Graphical User Interface (Swing / JavaFX) for visualizing the 2D grid,
 * drawing obstacles, marking start and goal nodes, displaying computed paths in colors,
 * and running live demonstrations of sequential vs. parallel executions during the presentation.
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. UI Window (extends JFrame):
 *    - Setup a top toolbar/panel with buttons and controls:
 *        * Algorithm selector dropdown: "BFS", "Dijkstra", "A*"
 *        * Thread count selector dropdown: 2, 4, 8, 16
 *        * "Find Single Path" button
 *        * "Run Batch (Parallel)" button
 *        * "Run Batch (Sequential)" button
 *        * "Randomize Obstacles" button
 *        * "Clear Paths" button
 *        * Bottom status bar label for elapsed time & throughput.
 * 
 * 2. Custom Canvas Component (extends JPanel):
 *    - In paintComponent(Graphics g):
 *        * Calculate cell width = getWidth() / cols, cell height = getHeight() / rows.
 *        * For each cell (r, c):
 *            - If wall/obstacle -> fill with dark gray/black.
 *            - If start cell -> fill with Green.
 *            - If goal cell -> fill with Red.
 *            - If part of computed path -> fill with Cyan/Blue.
 *            - Else walkable -> fill with White.
 *            - Draw grid outline borders.
 *    - Add MouseListener to canvas:
 *        * Left-click: toggle obstacle wall at (r, c).
 *        * Shift+Left-click: move start cell to (r, c).
 *        * Right-click: move goal cell to (r, c).
 * 
 * 3. Event Handlers:
 *    - Connect buttons to run the pathfinders and repaint the canvas.
 *    - For batch runs, execute in a background thread and call SwingUtilities.invokeLater()
 *      to update UI without freezing the window.
 * 
 * 4. static launch(int rows, int cols):
 *    - Helper to instantiate and show the JFrame on the Swing Event Dispatch Thread (EDT).
 */
public class GridVisualizer extends JFrame {

    // TODO: Declare UI components (canvas, buttons, dropdowns, status label)

    // TODO: Constructor GridVisualizer(int rows, int cols)

    // TODO: Custom Canvas JPanel with paintComponent(Graphics g) and mouse interaction

    // TODO: Event handlers for single solve, batch solve, and random map generation

    // TODO: static void launch(int rows, int cols)
}
