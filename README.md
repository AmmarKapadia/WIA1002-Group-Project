# Smart Parking & Traffic Management System

A Java-based Smart Parking & Traffic Management System built as a group project for the **Data Structures (WIA1002)** course. The system efficiently manages parking locations, vehicle records, and traffic flow using a combination of fundamental data structures — each chosen to optimize a specific operation in the system.

This project corresponds to **Topic 5** of the assigned case studies.

---

## Course Information

| | |
|---|---|
| **Course Code** | WIA1002 |
| **Course Name** | Data Structures |
| **Lecturer** | Mr. Muhammad Lubani |
| **Faculty** | Faculty of Computer Science and Information Technology (FCSIT) |
| **University** | Universiti Malaya (UM) |
| **Academic Session** | 2025/2026, Semester 2 |
| **Project Topic** | Topic 5 — Smart Parking & Traffic Management System |

---

## Group Members

| No. | Name | Matric Number | Role / Module |
|-----|------|---------------|---------------|
| 1   |      |               |               |
| 2   |      |               |               |
| 3   |      |               |               |
| 4   |      |               |               |
| 5   |      |               |               |

---

## Project Overview

The Smart Parking & Traffic Management System is designed to handle the full lifecycle of vehicles within a parking facility — from entry, slot assignment, navigation, and record-keeping, to exit. Rather than relying on a single data structure, the system integrates **six different data structures**, each addressing a specific real-world operational requirement. This design demonstrates how appropriate data structure selection directly impacts performance, scalability, and maintainability.

---

## Implemented Modules

All modules listed in the project marking rubric have been implemented and integrated into a single working system. The table below maps each module to its corresponding data structure and the rationale for its selection.

| # | Module | Data Structure Used | Purpose & Justification |
|---|--------|--------------------|--------------------------|
| 1 | **Parking & Vehicle Management** | Array / Linked List | Stores and dynamically updates parking slot and vehicle records. Supports `add`, `delete`, and `display` operations with predictable complexity. |
| 2 | **Entry & Exit Processing** | Queue + Stack | A **Queue (FIFO)** processes vehicles in arrival order, while a **Stack (LIFO)** enables an `undo` feature to reverse the most recent action in case of errors. |
| 3 | **Parking Slot Assignment** | Priority Queue (Min-Heap) | Efficiently assigns the best available slot based on priority (e.g., nearest slot, shortest waiting time) in **O(log n)**, far faster than a linear search through all slots. |
| 4 | **Route Navigation** | Graph + Dijkstra's Algorithm | Models parking locations and connecting routes as a weighted graph, then computes the shortest path to guide drivers to the nearest available parking location. |
| 5 | **Search System** | Binary Search Tree (BST) / AVL Tree | Enables efficient searching and ordered traversal of vehicle and parking records in **O(log n)** average time, significantly outperforming a basic linear list. |
| 6 | **Fast Data Retrieval** | Hash Table (HashMap) | Provides **O(1)** average-time lookup for frequently accessed data such as vehicle details and current parking status using key-value pairs. |
| 7 | **System Integration** | All of the above | All modules above are connected to operate as a single, cohesive system rather than as standalone components. |

---

## Tech Stack

- **Language:** Java 16
- **IDE:** Apache NetBeans 12.5
- **Build:** Standard Java project (no external dependencies)

---

## Project Structure

The project follows a standard Java package layout. Source files are organised by responsibility under the `src/` directory:

```
SmartParkingSystem/
├── src/
│   ├── models/              # Domain classes (e.g., Vehicle, ParkingSlot, Location)
│   │   ├── Vehicle.java
│   │   ├── ParkingSlot.java
│   │   └── Location.java
│   ├── structures/          # Custom data structure implementations
│   │   ├── LinkedList.java
│   │   ├── Queue.java
│   │   ├── Stack.java
│   │   ├── MinHeap.java
│   │   ├── Graph.java
│   │   ├── AVLTree.java
│   │   └── HashTable.java
│   ├── modules/             # Feature modules using the structures
│   │   ├── VehicleManager.java
│   │   ├── EntryExitProcessor.java
│   │   ├── SlotAssigner.java
│   │   ├── RouteNavigator.java
│   │   ├── SearchEngine.java
│   │   └── DataRetriever.java
│   ├── ui/                  # User interface / menu handling
│   │   └── MainMenu.java
│   └── Main.java            # Application entry point
└── README.md
```

> **Note:** The structure above reflects the intended layout. Update file names here if your final implementation differs.

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK):** version 16 or later
- **Apache NetBeans IDE** 12.5 (recommended)

### Running the Project in NetBeans

1. Clone or download this repository to your local machine.
2. Open NetBeans 12.5.
3. Go to **File → Open Project** and select the project folder.
4. Once the project is loaded, right-click the project in the **Projects** panel and select **Clean and Build**.
5. Run the project by pressing **F6**, or right-click and choose **Run**.
6. The application's main menu will appear in the NetBeans output console.

### Running from the Command Line (Optional)

```bash
# Compile all .java files
javac -d out src/**/*.java

# Run the main class
java -cp out Main
```

---

## Features

- Dynamic add, delete, and display of vehicle and parking slot records
- FIFO-based real-time processing of vehicle entries and exits
- Undo mechanism to reverse the most recent action
- Priority-based intelligent parking slot assignment
- Shortest-path route guidance to the nearest available parking
- Efficient logarithmic-time searching and sorting of records
- Constant-time retrieval of frequently accessed data

---

## Acknowledgements

We would like to thank our lecturer, **Mr. Muhammad Lubani**, for his guidance throughout the WIA1002 Data Structures course, and the Faculty of Computer Science and Information Technology, Universiti Malaya, for providing the resources and learning environment for this project.

---

*This project is submitted as part of the coursework requirement for WIA1002 Data Structures, Semester 2, Academic Session 2025/2026.*
