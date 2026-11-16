# Parallel Pathfinding on Grid (Multiple Shortest Paths)
**Project 6 — Concurrency & Object-Oriented Design Framework**

This repository contains the architecture, starter templates, and work plan for **Project 6 (Parallel Pathfinding on Grid)**. All classes have been created as clean skeleton files with comprehensive Javadoc and `TODO` comments detailing the exact fields, constructors, methods, data structures, and concurrency rules you and your team need to write.

---

## 📋 Table of Contents
1. [Project Overview & Learning Goals](#1-project-overview--learning-goals)
2. [Compliance with Project Specifications](#2-compliance-with-project-specifications)
3. [6-Member Team Task Breakdown](#3-6-member-team-task-breakdown)
4. [Master Work Plan & Milestones](#4-master-work-plan--milestones)
5. [File Guide: What to Write in Each File](#5-file-guide-what-to-write-in-each-file)
6. [Critical Concurrency Rules (Do Not Violate)](#6-critical-concurrency-rules-do-not-violate)
7. [How to Build & Run When You Write the Code](#7-how-to-build--run-when-you-write-the-code)
8. [Deliverables & Bonus Points Checklist (+5 Points)](#8-deliverables--bonus-points-checklist-5-points)

---

## 1. Project Overview & Learning Goals
- **Domain**: Concurrent 2D grid pathfinding with obstacles.
- **Algorithms**: Breadth-First Search (BFS), Dijkstra's Algorithm, A* Heuristic Search.
- **Concurrency**: Java `ExecutorService` with `Callable<Path>` worker tasks.
- **OOP Design**: Strict separation between the grid data model, pathfinding algorithms, and parallel batch orchestration.
- **Evaluation**: Measure sequential vs. parallel throughput (requests/second) and speedup ($S = T_{seq} / T_{par}$).

---

## 2. Compliance with Project Specifications

The framework strictly complies with all specifications from `ParallelProcessing_ProjectIdeas_Fall2026.pdf`:

| Requirement | Assigned File | Description | Status |
| :--- | :--- | :--- | :---: |
| **Grid Representation** | `Grid.java` & `Cell.java` | 2D matrix, obstacle tracking, neighbor queries, bounds validation. | Skeleton Ready |
| **Sequential Pathfinding** | `BFSPathFinder.java` | Queue-based shortest path with path reconstruction. | Skeleton Ready |
| **Multiple Path Requests** | `PathRequest.java` | Encapsulates Grid, start cell, goal cell, and search configuration. | Skeleton Ready |
| **Parallel Execution** | `ParallelPathfindingEngine.java` | `ExecutorService` with `Callable<Path>` tasks. | Skeleton Ready |
| **Benchmarking Driver** | `PathfindingExperiment.java` | Tests varying request counts (10, 50, 100, 250) & thread counts (1, 2, 4, 8, 16). | Skeleton Ready |
| **Bonus (+3 pts) GUI** | `GridVisualizer.java` | Interactive Swing visualizer with path rendering and obstacle editing. | Skeleton Ready |
| **Bonus (+2 pts) Advanced Algorithms** | `DijkstraPathFinder.java` & `AStarPathFinder.java` | Weighted terrain costs and heuristic-guided A* search. | Skeleton Ready |

---

## 3. 6-Member Team Task Breakdown

To ensure equal workload and avoid merge conflicts, tasks are divided among the 6 members:

| Member | Title | Responsibilities | Assigned Files |
| :--- | :--- | :--- | :--- |
| **Member 1** | **Grid & Model Lead** | • Manage 2D grid matrix, boundaries, and obstacle logic.<br>• Implement map generation (random density, obstacle toggling).<br>• Ensure thread-safe, read-only cell queries. | `Cell.java`<br>`Grid.java`<br>`Path.java` |
| **Member 2** | **Algorithm Specialist** | • Implement and verify `BFSPathFinder`.<br>• Implement `DijkstraPathFinder` (weighted movement).<br>• Implement `AStarPathFinder` (heuristics + diagonal moves). | `PathFinder.java`<br>`BFSPathFinder.java`<br>`DijkstraPathFinder.java`<br>`AStarPathFinder.java` |
| **Member 3** | **Concurrency Engineer** | • Implement `SequentialPathfindingEngine` baseline.<br>• Implement `ParallelPathfindingEngine` with `ExecutorService`.<br>• Oversee task scheduling (`Callable<Path>`), thread pool lifecycle, and error handling. | `PathRequest.java`<br>`ParallelPathfindingEngine.java`<br>`SequentialPathfindingEngine.java` |
| **Member 4** | **Benchmark & Metrics Lead** | • Develop `PathfindingExperiment` harness.<br>• Execute benchmarks across workloads (10, 50, 100, 250 reqs) and threads (1 to 16).<br>• Calculate speedup, throughput, and export data/charts for the report. | `BenchmarkResult.java`<br>`PathfindingExperiment.java`<br>`PathfindingResult.java` |
| **Member 5** | **GUI & Visualization Lead** *(Bonus +3)* | • Build and polish `GridVisualizer` (interactive canvas).<br>• Implement visual path tracing, obstacle painting, and start/goal picking.<br>• Connect GUI buttons to run sequential vs. parallel comparisons live. | `GridVisualizer.java`<br>`Main.java` |
| **Member 6** | **QA, Report & Presentation Lead** | • Set up JUnit unit tests and boundary validation.<br>• Author the **3–4 page academic project report**.<br>• Design the **5–8 minute slide deck** and orchestrate the live demo. | Unit Tests<br>`Report (3-4 pages)`<br>`Presentation Slides` |

---

## 4. Master Work Plan & Milestones

```
Phase 1: Setup & Core Model  ──►  Phase 2: Search Algorithms  ──►  Phase 3: Parallel Engine  ──►  Phase 4: Benchmarks & GUI  ──►  Phase 5: Report & Demo
(Days 1–3)                       (Days 4–6)                      (Days 7–10)                    (Days 11–14)                   (Days 15–18)
```

- **Phase 1 (Days 1–3)**: Member 1 completes `Cell`, `Grid`, and `Path`. Verify grid bounds and neighbor querying with unit tests.
- **Phase 2 (Days 4–6)**: Member 2 completes `PathFinder` interface and `BFSPathFinder`. Test on small grids with known paths.
- **Phase 3 (Days 7–10)**: Member 3 completes `SequentialPathfindingEngine` and `ParallelPathfindingEngine` using `Callable<Path>`.
- **Phase 4 (Days 11–14)**: Member 4 builds `PathfindingExperiment` for benchmarking; Member 5 builds `GridVisualizer` (GUI); Member 2 finishes `DijkstraPathFinder` and `AStarPathFinder`.
- **Phase 5 (Days 15–18)**: Member 6 leads writing the 3–4 page report with Member 4's benchmark tables/graphs, prepares the 5–8 min slides, and rehearses the live demo with Member 5.

---

## 5. File Guide: What to Write in Each File

### Package: `com.pathfinding.model`

#### 1. `Cell.java` (Member 1)
- **Fields**: `int row`, `int col`, `boolean walkable`, `double cost`.
- **Constructors**: `Cell(row, col)`, `Cell(row, col, walkable)`, `Cell(row, col, walkable, cost)`.
- **Methods**: Standard getters, `equals(Object o)` and `hashCode()` based on `row` and `col`, and `toString()`.
- **Rule**: *Never* store search state (`visited`, `parent`) here.

#### 2. `Grid.java` (Member 1)
- **Fields**: `int rows`, `int cols`, `Cell[][] cells`.
- **Constructor**: Initializes dimensions and allocates `cells[r][c]`.
- **Methods**:
  - `isValid(r, c)`: checks if coordinate is within `[0, rows)` and `[0, cols)`.
  - `getCell(r, c)`: returns cell at position.
  - `isWalkable(r, c)`: returns true if cell exists and is walkable.
  - `setObstacle(r, c, boolean isObstacle)`: sets a wall or clears it.
  - `getNeighbors(Cell cell, boolean allowDiagonal)`: returns adjacent walkable cells (4-way, or 8-way diagonal without cutting blocked corners).
  - `generateRandomObstacles(double probability, Random random)`: sets random walls.

#### 3. `Path.java` (Member 1)
- **Fields**: `List<Cell> cells`, `boolean exists`, `double totalCost`.
- **Methods**: `static Path empty()`, `exists()`, `getCells()`, `getLength()`, `getTotalCost()`, `getStart()`, `getGoal()`.

#### 4. `PathRequest.java` (Member 3 & 1)
- **Fields**: `int id`, `Grid grid`, `Cell start`, `Cell goal`, `boolean allowDiagonal`.
- **Constructor & Getters**: encapsulate the search query so it can be passed into engines.

---

### Package: `com.pathfinding.algorithm`

#### 5. `PathFinder.java` (Member 2)
- Strategy interface defining:
  - `Path findPath(PathRequest request);`
  - `String getName();`

#### 6. `BFSPathFinder.java` (Member 2)
- Unweighted shortest-path algorithm using a standard `Queue<Cell>`.
- In `findPath(PathRequest request)`:
  - Allocate local `boolean[][] visited` and `Cell[][] parent`.
  - Traverse neighbors with queue.
  - Backtrack from goal using `parent` pointers to construct the `Path`.

#### 7. `DijkstraPathFinder.java` (Member 2 — Bonus +2 pts)
- Weighted shortest-path algorithm using `PriorityQueue<NodeRecord>`.
- Local `dist[][]` initialized to infinity; relaxes edges based on `cell.getCost()`.

#### 8. `AStarPathFinder.java` (Member 2 — Bonus +2 pts)
- Heuristic search combining `gScore` (actual cost) + `hScore` (Manhattan or Octile heuristic to goal).

---

### Package: `com.pathfinding.engine`

#### 9. `SequentialPathfindingEngine.java` (Member 3)
- Executes a `List<PathRequest>` one by one in a simple `for` loop.
- Measures total execution time with `System.nanoTime()` and returns a `PathfindingResult`.

#### 10. `ParallelPathfindingEngine.java` (Member 3)
- Manages an `ExecutorService` (`Executors.newFixedThreadPool(threadCount)`).
- Wraps each `PathRequest` into a `Callable<Path>`:
  ```java
  tasks.add(() -> pathFinder.findPath(req));
  ```
- Calls `executor.invokeAll(tasks)` to run all searches concurrently.
- Iterates through `Future<Path>` results and computes throughput ($req/sec$).
- Provides `shutdown()` to terminate threads cleanly.

#### 11. `PathfindingResult.java` (Member 3 & 4)
- Encapsulates execution results: `String engineType`, `int threadCount`, `int totalRequests`, `int successfulPaths`, `long executionTimeNanos`, `List<Path> paths`.
- Computes `getExecutionTimeMillis()` and `getThroughput()`.

---

### Package: `com.pathfinding.experiment`

#### 12. `BenchmarkResult.java` (Member 4)
- Data model holding metrics for a single test trial: `mode`, `threadCount`, `requestCount`, `timeMillis`, `speedup`, `throughput`.

#### 13. `PathfindingExperiment.java` (Member 4)
- **`generateRandomRequests(grid, count, random)`**: generates valid random start/goal pairs on walkable cells.
- **`runJitWarmup(grid, pathFinder)`**: runs throwaway queries to warm up the JVM JIT compiler before measuring.
- **`runSuite(...)`**: tests batch sizes (e.g. 10, 50, 100, 250) across thread counts (1, 2, 4, 8, 16), calculating speedup ($S = T_{seq} / T_{par}$).
- **`printSummaryTable(...)`**: prints a formatted ASCII table for the report.

---

### Package: `com.pathfinding.ui` & `com.pathfinding`

#### 14. `GridVisualizer.java` (Member 5 — Bonus +3 pts)
- Extends `JFrame`. Contains:
  - Toolbar with algorithm selector, thread selector, and trigger buttons ("Find Path", "Run Batch", "Randomize").
  - Canvas (`JPanel`) that renders walls (black), start (green), goal (red), and paths (blue).
  - Mouse click listeners to toggle walls or set start/goal.

#### 15. `Main.java` (Member 5 / Team)
- Parses CLI flags:
  - `--gui`: launches `GridVisualizer`.
  - `--quick-demo`: runs a quick path comparison.
  - Default: runs `PathfindingExperiment.runSuite(...)`.

---

## 6. Critical Concurrency Rules (Do Not Violate)

> [!WARNING]
> 1. **Do not modify the `Grid` during search**: Worker threads must treat the `Grid` as strictly read-only.
> 2. **Never store `visited` or `parent` in `Cell`**: Every call to `findPath()` must allocate its own local arrays or sets (`boolean[][] visited = new boolean[rows][cols];`). If threads write to shared fields on `Cell`, searches will corrupt each other.
> 3. **Always shut down the `ExecutorService`**: Unclosed thread pools keep the JVM process running indefinitely.

---

## 7. How to Build & Run When You Write the Code

Once you implement the methods:

```bash
# Compile and run full benchmark suite
./run.sh

# Run quick algorithm demo
./run.sh --quick-demo

# Launch GUI Visualizer
./run.sh --gui
```

Or using standard Maven:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.pathfinding.Main"
```

---

## 8. Deliverables & Bonus Points Checklist (+5 Points)

- [ ] **Deliverable 1: Source Code** (Grid, Cell, Path, PathRequest, PathFinder, Engines, Experiment).
- [ ] **Deliverable 2: Academic Report (3–4 pages)** (OOP design diagram, concurrency discussion, benchmark tables, speedup interpretation).
- [ ] **Deliverable 3: Presentation (5–8 minutes)** (Slides + live GUI demo on sample grids).
- [ ] **Bonus +3 points**: Interactive GUI Visualizer (`GridVisualizer.java`).
- [ ] **Bonus +2 points**: Dijkstra (weighted) & A* (heuristics) algorithms + diagonal movement.
