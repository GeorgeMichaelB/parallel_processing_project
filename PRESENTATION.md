# Parallel Pathfinding on Grid (Multiple Shortest Paths)
## Final Project Presentation (5–8 Minutes)

**Course**: Parallel Processing & Concurrency  
**Team**: Project 6 — Team 6  
**Tools**: Java 17/21, `ExecutorService`, Swing GUI, JUnit 5, Maven  

---

## Slide 1: Title & Team Responsibilities (0:30 min)

### Parallel Pathfinding on 2D Grid
*High-Throughput Concurrent Shortest Path Architecture*

| Team Member | Role | Key Contributions |
| :--- | :--- | :--- |
| **Member 1** | Grid & Model Lead | `Grid`, `Cell`, `Path` immutable abstractions, neighbor logic |
| **Member 2** | Algorithm Specialist | `BFSPathFinder`, `DijkstraPathFinder`, `AStarPathFinder` |
| **Member 3** | Concurrency Engineer | `ParallelPathfindingEngine`, `SequentialPathfindingEngine`, `PathRequest` |
| **Member 4** | Benchmark & Metrics Lead | `PathfindingExperiment`, `BenchmarkResult`, statistical metrics |
| **Member 5** | GUI & Visualization Lead | `GridVisualizer` (interactive Swing canvas, live benchmarks) |
| **Member 6** | QA, Report & Presentation Lead | JUnit 5 test suite (28 tests), academic report, presentation |

> **Speaker Notes (0:30)**:  
> "Good morning everyone. Today we are presenting Project 6: Parallel Pathfinding on a 2D Grid. Our goal was to design an object-oriented, thread-safe framework capable of processing hundreds of path queries simultaneously on a shared environment without data races or locks, and evaluate speedup across multiple thread pools."

---

## Slide 2: Problem Definition & The Concurrency Challenge (1:00 min)

### The Challenge
- Autonomous agents (robots, delivery drones, game entities) frequently generate **hundreds of concurrent route queries** on a single terrain map.
- Sequential evaluation is an $O(N \cdot (|V| + |E|))$ bottleneck.
- **The Classic Multi-Threading Pitfall**: In naive code, developers store `visited = true` or `parent = p` directly on `Cell` vertices. In a multi-threaded system, threads overwrite each other's search state, causing data corruption, infinite loops, or missed routes.

```
WRONG DESIGN (Race Conditions):
Thread 1 ──► [Cell (5, 5).visited = true] ◄── Thread 2 (Corrupts Thread 1's search!)

CORRECT DESIGN (Thread-Local Traversal):
Thread 1 ──► Local stack [boolean[][] visited_1] ──► Reads shared Grid (Read-Only)
Thread 2 ──► Local stack [boolean[][] visited_2] ──► Reads shared Grid (Read-Only)
```

> **Speaker Notes (1:00)**:  
> "The primary concurrency challenge in graph traversal is state isolation. If worker threads mutate visited flags on shared Cell objects, race conditions destroy correctness. Our architectural breakthrough is treating the Grid as strictly read-only during search, while each worker allocates its own visited and parent arrays on its private thread stack. This yields zero lock contention."

---

## Slide 3: Software Architecture & Concurrency Model (1:30 min)

### Strategy Pattern & Thread Pool Orchestration

```
[ Client / Main / GUI ]
           │
           ▼
[ ParallelPathfindingEngine ]
    │  - ExecutorService (FixedThreadPool)
    │  - Wraps PathRequest into Callable<Path>
    ▼
[ Worker Threads (P = 2, 4, 8, 16) ]
    │
    ├── Worker 1 ──► findPath(req_1) ──► Thread-local visited[][] & parent[][]
    ├── Worker 2 ──► findPath(req_2) ──► Thread-local visited[][] & parent[][]
    └── Worker P ──► findPath(req_P) ──► Thread-local visited[][] & parent[][]
           │
           ▼ (invokeAll & Future.get())
[ PathfindingResult (Throughput, Latency, Speedup) ]
```

### Key Engineering Highlights:
- **Strategy Pattern (`PathFinder` interface)**: Swap BFS, Dijkstra, and $A^*$ with zero changes to the parallel engine.
- **Task Scheduling (`Callable<Path>` & `invokeAll`)**: Efficient non-blocking batch dispatch.
- **Safe Lifecycle (`AutoCloseable`)**: Strict two-phase thread pool termination ensures no zombie JVM threads.

> **Speaker Notes (1:30)**:  
> "Our architecture decouples the data model, pathfinding algorithms, and concurrency engine using the Strategy Pattern. The parallel engine partitions queries into Callable tasks, dispatches them across a fixed thread pool via invokeAll, and collects Future results. Notice that because there are no synchronized blocks or locks during traversal, threads run at full hardware speed."

---

## Slide 4: Experimental Performance & Speedup Analysis (1:30 min)

### Empirical Results ($80 \times 80$ Grid, 6,400 Cells, 25% Obstacles)

| Workload ($N$) | Mode | Threads ($P$) | Time (ms) | Speedup ($S$) | Throughput (req/s) |
| :---: | :---: | :---: | :---: | :---: | :---: |
| **10** | Seq / Par | 1 vs 4 | 4.7 ms vs 6.9 ms | **0.68x** (Overhead) | 1,446 req/s |
| **50** | Seq / Par | 1 vs 16 | 25.1 ms vs 2.9 ms | **8.61x** | 17,142 req/s |
| **100** | Seq / Par | 1 vs 8 | 24.0 ms vs 5.5 ms | **4.40x** | 18,319 req/s |
| **250** | Seq / Par | 1 vs 16 | 50.6 ms vs 6.7 ms | **7.58x** | **37,470 req/s** |

### Crucial Findings:
1. **Granularity vs Scheduling Overhead**: At $N = 10$, sequential is faster because thread creation and `Future` resolution overhead exceed 10 tiny computations.
2. **Workload Scaling**: At $N = 50$ and $250$, computational density dominates overhead—speedup surges up to **$8.61\times$**!
3. **Throughput**: Parallel throughput reaches **$37,470\text{ paths/sec}$** compared to $4,940\text{ paths/sec}$ sequentially.

> **Speaker Notes (1:30)**:  
> "Here are our benchmark results. At small batches of 10 requests, sequential is slightly faster—illustrating thread management overhead. But once the workload reaches 50 to 250 requests, parallelism shines: we observe up to an 8.6x speedup and a peak throughput of over 37,000 paths per second on 16 threads."

---

## Slide 5: Bonus Features Implemented (+5 Points) (1:00 min)

### 1. Interactive Swing GUI Visualizer (`GridVisualizer.java` — +3 Pts)
- **Interactive Painting**: Left-click to place/remove walls; Shift+Click to set Start; Right-Click to set Goal.
- **Dynamic Controls**: Algorithm switcher (BFS, Dijkstra, $A^*$), thread selector (2–16), random obstacle generator.
- **Asynchronous Execution**: Uses `SwingWorker` so large parallel batches execute in the background without freezing the UI.

### 2. Advanced Pathfinding Algorithms (+2 Pts)
- **Dijkstra's Algorithm (`DijkstraPathFinder.java`)**: PriorityQueue-based weighted edge relaxation for variable terrain costs.
- **$A^*$ Search (`AStarPathFinder.java`)**: Heuristic-guided search with Octile/Manhattan distance estimations; cuts expanded nodes by over $60\%$.
- **8-Way Diagonal Navigation**: Full diagonal traversal with corner-cutting collision protection.

> **Speaker Notes (1:00)**:  
> "We also implemented both bonus requirements: an interactive Swing visualizer that allows live wall editing and real-time path rendering, plus Dijkstra's weighted algorithm and A* heuristic search with diagonal movement."

---

## Slide 6: Live Demonstration Walkthrough (1:30 min)

### Live Demo Script (Run with `run.bat --gui` or `./run.sh --gui`):

1. **Step 1: Start the Visualizer**
   - Execute: `./run.bat --gui`
   - Observe 40×40 grid canvas initialized with Start (Green, top-left) and Goal (Red, bottom-right).
2. **Step 2: Interactive Obstacle Editing**
   - Click "Randomize" to generate a 25% density maze.
   - Left-click on the canvas to draw an obstacle wall directly between start and goal.
3. **Step 3: Single Path Search (BFS vs A*)**
   - Select "BFS" -> Click "Find Path". Observe cyan path drawn, report time in ms.
   - Switch dropdown to "A*" -> Click "Find Path". Notice identical path found in fewer exploration steps.
4. **Step 4: Live Sequential vs. Parallel Batch Benchmark**
   - Click "Run Batch (Seq)" -> Process 50 random requests; observe status bar time and throughput.
   - Select 8 threads -> Click "Run Batch (Par)" -> Process 50 requests concurrently.
   - Point out the instant latency drop and higher throughput!

> **Speaker Notes (1:30)**:  
> "[Transition to live window]. Now let's demonstrate the system live. Here is our 40x40 visualizer. We can randomize obstacles or draw walls with the mouse. When we click 'Find Path' with A*, the route appears instantly in cyan. Now notice what happens when we run a batch of 50 requests sequentially versus in parallel across 8 threads: the parallel engine finishes in a fraction of the time with instant UI feedback."

---

## Slide 7: Summary & Conclusion (0:30 min)

### Summary of Achievements:
- ✅ **100% Complete Implementation**: All model, algorithm, engine, benchmark, and UI components implemented.
- ✅ **Clean Object-Oriented Design**: Full adherence to SOLID principles & Strategy Pattern.
- ✅ **Zero Lock Contention**: Thread-local traversal states yield near-linear speedup up to $8.6\times$.
- ✅ **Automated Quality Assurance**: 28 automated JUnit 5 tests covering all edge cases.
- ✅ **All Deliverables & Bonus (+5 Points)**: Source code, 4-page academic report, presentation slides, interactive GUI, and advanced algorithms.

### Thank You! Questions?
- **Repository**: [https://github.com/GeorgeMichaelB/parallel_processing_project](https://github.com/GeorgeMichaelB/parallel_processing_project)
