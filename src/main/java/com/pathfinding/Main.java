package com.pathfinding;

/**
 * FILE: Main.java
 * ASSIGNED TO: Member 6 (Integration & Presentation Lead) / Entire Team
 * 
 * PURPOSE:
 * Application entry point for the project.
 * Parses command-line flags and dispatches to either:
 * 1. The benchmarking experiment suite (default or --benchmark).
 * 2. The interactive GUI visualizer (--gui or -g).
 * 3. A quick console demonstration (--quick-demo or -d).
 * 
 * WHAT YOU NEED TO WRITE IN THIS FILE:
 * 1. Parse command-line args (args array):
 *    - Check if "--gui" is passed.
 *    - Check if "--quick-demo" is passed.
 * 
 * 2. If --gui is selected:
 *    - Call GridVisualizer.launch(rows, cols).
 * 
 * 3. If --quick-demo is selected:
 *    - Create a small grid (e.g. 20x20).
 *    - Run a single request through BFS, Dijkstra, and A*, printing time and path length.
 * 
 * 4. Otherwise (default):
 *    - Call PathfindingExperiment.runSuite(...) to run the full benchmark suite
 *      and print the summary comparison table.
 */
public class Main {

    public static void main(String[] args) {
        // TODO: Parse command-line arguments

        // TODO: Launch GUI visualizer, quick demo, or full benchmark experiment
    }
}
