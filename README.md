# Smart Parking & Traffic Management System

A JavaFX desktop application that simulates a real-time smart parking system using five custom data structures (no Java Collections used for the core logic). Built as a group project for **WIA1002 — Data Structures** at Universiti Malaya.

---

## What It Does

The system manages a small parking lot (5 slots, 9-vertex road graph) and supports the full vehicle lifecycle:

- **Process Entry** — admit a vehicle, assign the closest available slot, and display the shortest route from the gate
- **Process Exit** — remove a vehicle, free its slot, and update records
- **Search** — find a vehicle by license plate across the entire parking history
- **Undo** — reverse the most recent action(s), with multi-step support
- **Live Statistics** — track current occupancy, lifetime entries/exits, peak entry hour, and a 24-hour entry distribution heatmap

A console-mode fallback (`ConsoleMain.java`) is preserved for testing without the GUI.

---

## How the Case Study Maps to Data Structures

| Case Study Requirement | Data Structure | Why |
|---|---|---|
| Dynamic add/remove/display of parking and vehicle records | **HashMap** + **AVL Tree** | O(1) plate→vehicle lookup; O(log n) ordered traversal |
| Process vehicles in arrival order, with undo support | **Queue** (FIFO arrivals) + **Stack** (LIFO undo log) | Natural fit for ordered processing and reverse operations |
| Assign slots by priority (nearest available) | **MinHeap** | O(log n) extraction of nearest slot vs O(n) linear scan |
| Guide drivers via shortest path | **Adjacency-List Graph** + **Dijkstra's Algorithm** | Handles weighted edges correctly (BFS would not); O((V+E) log V) routing |
| Efficient search and sort | **AVL Tree** | Self-balancing guarantees O(log n) worst case vs O(n) for a plain BST on sorted input |
| Fast access to frequently used data | **HashMap** | O(1) average-case lookup for high-frequency vehicle queries |

Every backend operation uses one or more of the five data structures listed above — they are not just decorative.

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    JavaFX GUI (Sprint 3)                    │
│  ┌──────────────┐  ┌─────────────┐  ┌────────────────────┐  │
│  │ MapPaneCtrl  │  │ StatsCtrl   │  │ HistoryPaneCtrl    │  │
│  │ (M2 Graph)   │  │ (M1 Stats)  │  │ (M3 History)       │  │
│  └──────────────┘  └─────────────┘  └────────────────────┘  │
│  ┌──────────────┐  ┌─────────────┐  ┌────────────────────┐  │
│  │ ActionPane   │  │ SearchPane  │  │ RouteOverlayHelper │  │
│  └──────────────┘  └─────────────┘  │ (M4 Dijkstra)      │  │
│                                     └────────────────────┘  │
│                MainController + AppContext (M5)             │
├─────────────────────────────────────────────────────────────┤
│                  Manager Layer (Sprint 2)                   │
│  GateManager │ SlotManager │ HashMapManager │ RouteManager  │
│  SearchManager │ StatsManager │ HistoryManager │ ...        │
├─────────────────────────────────────────────────────────────┤
│             Custom Data Structures (Sprint 1)               │
│  CustomHashMap │ CustomAVL │ CustomMinHeap │ CustomGraph    │
│  CustomQueue   │ CustomStack │ EdgeLinkedList │ ...         │
└─────────────────────────────────────────────────────────────┘
```

---

## Tech Stack

- **Java 16** (OpenJDK)
- **JavaFX 16** (Gluon SDK — must be installed separately, see Setup below)
- **JUnit 4** (testing)
- **NetBeans 12.5** (IDE — project is an Ant-based NetBeans project)
- **Scene Builder** (FXML editing — optional)

No external dependencies beyond what JavaFX ships with. No databases.

---

## Setup Instructions

### Prerequisites
- **JDK 16** installed and registered in NetBeans
- **NetBeans 12.5** (other versions may work but are untested)
- **OpenJFX 16 SDK** — download from https://gluonhq.com/products/javafx/

### One-time JavaFX setup

1. Extract OpenJFX 16 to `C:\openjfx\` (Windows) or equivalent path on Mac/Linux. **Path must match exactly** — the project's `project.properties` references this location.
2. **Delete `src.zip`** from `C:\openjfx\javafx-sdk-16\lib\` if present. JavaFX 16 ships a source archive in `lib/` that breaks Java's module path scanner. The system will fail to launch otherwise.
3. In NetBeans: **Tools → Libraries → New Library** → name it exactly `JavaFX 16` → on the Classpath tab, add all `.jar` files from `C:\openjfx\javafx-sdk-16\lib\` (do NOT add `src.zip`).
4. Open the project. NetBeans should automatically link the `JavaFX 16` library on first build.

### Running

- **GUI mode** (default): Right-click project → **Run** (or F6). Main class is `app.ParkingApp`.
- **Console mode**: Change main class to `ConsoleMain` in **Project Properties → Run**, then run.

### Running Tests

- **Run → Test Project** (Alt+F6 in NetBeans).
- All ~88 JUnit tests across the data structures and manager classes passed.

---

## Project Structure

```
src/
├── app/                  # JavaFX entry point + DI container
│   ├── AppContext.java
│   └── ParkingApp.java
├── controllers/          # JavaFX FXML controllers
│   ├── MainController.java
│   ├── MapPaneController.java
│   ├── StatsPaneController.java
│   ├── HistoryPaneController.java
│   ├── ActionPaneController.java
│   ├── SearchPaneController.java
│   └── RouteOverlayHelper.java
├── datastructures/       # The five custom DSes + supporting classes
│   ├── CustomHashMap.java
│   ├── CustomAVL.java
│   ├── CustomMinHeap.java
│   ├── CustomGraph.java
│   ├── CustomQueue.java
│   ├── CustomStack.java
│   ├── Entry.java
│   ├── Edge.java
│   └── EdgeLinkedList.java
├── managers/             # Business logic layer
│   ├── GateManager.java
│   ├── SlotManager.java
│   ├── HashMapManager.java
│   ├── SearchManager.java
│   ├── StatsManager.java
│   ├── HistoryManager.java
│   ├── RouteManager.java
│   ├── DijkstraSolver.java
│   ├── RecordManager.java
│   └── Action.java
├── models/               # Plain data classes
│   ├── Vehicle.java
│   ├── ParkingSlot.java
│   └── Route.java
├── fxml/                 # JavaFX layout files
│   ├── MainWindow.fxml
│   ├── MapPane.fxml
│   ├── StatsPane.fxml
│   ├── HistoryPane.fxml
│   ├── ActionPane.fxml
│   └── SearchPane.fxml
├── css/
│   └── styles.css        # Cream / maple / chocolate color palette
└── ConsoleMain.java      # Sprint 2 console fallback
test/                     # JUnit test suite (Sprint 4)
├── datastructures/
└── managers/
```

---

## Team Members

| Role | Member | Domain |
|---|---|---|
| Member 1 | **Ammar Kapadia** | HashMap + StatsManager (statistics dashboard) |
| Member 2 | **Nicolas Nicodemus** | Graph + Edges (parking map rendering) |
| Member 3 | **Sun Chengyang** | Queue + Stack + HistoryManager (undo logic) |
| Member 4 | **Yim Zi Hao** | MinHeap + Dijkstra (slot assignment + routing) |
| Member 5 | **Xu Zimao** | AVL Tree + SearchManager + JavaFX integration |

---

## Sprint History

This project was developed across four sprints over the course of the semester:

- **Sprint 1** — Built the five custom data structures from scratch (no `java.util.*` collections in the core).
- **Sprint 2** — Built the manager layer that combines the data structures into a working console application. All 11 console-menu features implemented.
- **Sprint 3** — Built the JavaFX GUI on top of the Sprint 2 backend. 5 inter-connected panels, live route highlighting, statistics heatmap.
- **Sprint 4** — JUnit testing, complexity analysis report, UML diagrams, demo rehearsal.

---

## Course Info

**Course:** WIA1002 — Data Structures
**Institution:** Universiti Malaya
**Academic Session:** 2025/2026 Semester 2
**Tutor:** Prof. Muhammad Lubani
**Demonstator:** Mohammad Shahid Akhtar
---
