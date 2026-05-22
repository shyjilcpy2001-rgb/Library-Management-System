# Requirements — Library Management System

> This document defines what the Library Management System must do, how it should perform, and the boundaries of the project.

---

## 1. Functional Requirements

> Functional requirements describe **what the system does** — the actual features and actions it supports.

| ID | Requirement | Description |
|---|---|---|
| FR-01 | Add a Book | The system shall allow adding a new book with an ID, title, and author |
| FR-02 | View All Books | The system shall display all books with their current availability status |
| FR-03 | Search Book by Title | The system shall allow searching for a book using its title (case-insensitive preferred) |
| FR-04 | Borrow a Book | The system shall allow a user to borrow an available book and update its status to "Borrowed" |
| FR-05 | Return a Book | The system shall allow a user to return a borrowed book and update its status to "Available" |
| FR-06 | Prevent Duplicate Borrowing | The system shall prevent a user from borrowing the same book twice |
| FR-07 | Validate Book Availability | The system shall reject a borrow request if the book is already borrowed |
| FR-08 | Display Borrowed Books | The system shall show a user the list of books they have currently borrowed |
| FR-09 | Console Menu Navigation | The system shall present a numbered menu for all actions and accept user input via keyboard |
| FR-10 | Exit Application | The system shall provide an option to safely exit the program |

---

## 2. Non-Functional Requirements

> Non-functional requirements describe **how well** the system works — quality, performance, and usability.

| ID | Category | Requirement |
|---|---|---|
| NFR-01 | Usability | Menu prompts must be clear and easy to understand for beginners |
| NFR-02 | Reliability | The system must not crash on invalid or unexpected user input |
| NFR-03 | Maintainability | Code must be organized in separate classes with clear responsibilities |
| NFR-04 | Readability | Class and method names must be descriptive and self-explanatory |
| NFR-05 | Performance | All operations (add, search, borrow, return) must complete instantly for small datasets |
| NFR-06 | Portability | The application must run on any OS that supports Java JDK 8 or higher |
| NFR-07 | Extensibility | The design should allow new features (e.g., reservations) to be added without rewriting existing code |

---

## 3. System Requirements

> These are the minimum hardware and software requirements to **run** this application.

### Hardware Requirements

| Component | Minimum |
|---|---|
| Processor | Any modern CPU (1 GHz or faster) |
| RAM | 256 MB or more |
| Disk Space | 50 MB (for JDK installation and project files) |
| Input | Keyboard (for console input) |

### Software Requirements

| Software | Version | Notes |
|---|---|---|
| Operating System | Windows 7+, macOS 10.10+, Linux (any modern distro) | Must support Java JDK |
| Java Development Kit (JDK) | 8 or higher | Required to compile and run Java code |
| Terminal / Command Prompt | Any | Used to compile and run the application |

---

## 4. Software Dependencies

> This project is **pure Java** with no external libraries. All dependencies are part of the standard Java SE (Standard Edition) library.

| Package / Class | Source | Purpose |
|---|---|---|
| `java.util.ArrayList` | Java SE Standard Library | Stores dynamic lists of `Book` objects |
| `java.util.Scanner` | Java SE Standard Library | Reads user keyboard input in `Main.java` |
| No third-party libraries | — | The project requires only the JDK |

---

## 5. User Roles

> This project defines a **single user role** for the initial version.

| Role | Description | Permissions |
|---|---|---|
| **Library User / Admin** | The person running the console application | Can add books, view books, search, borrow, return, and exit |

> **Note:** Future versions may introduce separate `Admin` and `Member` roles, where only admins can add books and members can only borrow/return.

---

## 6. Use Cases

> A use case describes a specific interaction between a user and the system to achieve a goal.

---

### UC-01: Add a Book

| Field | Detail |
|---|---|
| **Actor** | Library User |
| **Goal** | Add a new book to the library |
| **Precondition** | Application is running |
| **Steps** | 1. User selects "Add Book" from the menu  2. User enters Book ID, Title, and Author  3. System creates a `Book` object and adds it to `Library` |
| **Outcome** | Book is stored and available in the library |
| **Error Case** | N/A (basic version does not check for duplicate IDs) |

---

### UC-02: Search for a Book

| Field | Detail |
|---|---|
| **Actor** | Library User |
| **Goal** | Find a specific book by title |
| **Precondition** | At least one book exists in the library |
| **Steps** | 1. User selects "Search Book"  2. User enters a book title  3. System searches the `Library` list for a matching title |
| **Outcome** | Book details are displayed if found |
| **Error Case** | System displays "Book not found" message |

---

### UC-03: Borrow a Book

| Field | Detail |
|---|---|
| **Actor** | Library User |
| **Goal** | Borrow an available book |
| **Precondition** | The book exists and its `isAvailable` is `true` |
| **Steps** | 1. User selects "Borrow Book"  2. User enters the book title  3. System checks availability  4. System sets `isAvailable = false`  5. Book is added to the `User`'s `borrowedBooks` list |
| **Outcome** | Book is marked as borrowed and linked to the user |
| **Error Case** | Book not found → "Book not found" message. Book unavailable → "Book is already borrowed" message |

---

### UC-04: Return a Book

| Field | Detail |
|---|---|
| **Actor** | Library User |
| **Goal** | Return a book they have borrowed |
| **Precondition** | The user has borrowed the book |
| **Steps** | 1. User selects "Return Book"  2. User enters the book title  3. System sets `isAvailable = true`  4. Book is removed from the `User`'s `borrowedBooks` list |
| **Outcome** | Book is available again for others to borrow |
| **Error Case** | Book not found → "Book not found" message |

---

## 7. Project Scope

### In Scope (What this project includes)

- Console-based menu-driven interface
- Four core classes: `Book`, `Library`, `User`, `Main`
- Core CRUD-like operations: Add, View, Search, Borrow, Return
- In-memory data storage using `ArrayList` (data is lost when the program exits)
- Basic input validation and null checks
- Single-user session per run

### Out of Scope (What this project does NOT include)

- Graphical user interface (GUI)
- File or database storage (data does not persist)
- Multiple simultaneous users or login/authentication
- Admin vs Member role separation
- Book reservations (planned for a future version)
- Due date tracking or fine calculation
- Network or web access

---

## 8. Assumptions and Limitations

### Assumptions

- The person running the application is the only user per session.
- Book titles are used as the primary search/borrow/return identifier (not IDs).
- The application is used on a machine with Java JDK 8+ correctly installed.
- Input is expected to be provided in English.
- The library is small enough that an `ArrayList` performs adequately (no pagination needed).

### Limitations

| Limitation | Impact |
|---|---|
| **No data persistence** | All books and borrow records are lost when the program exits |
| **Single user session** | Only one user can interact at a time; no multi-user support |
| **No duplicate ID check** | Two books could be added with the same ID (not validated) |
| **Title-based operations** | Searching and borrowing rely on exact title matching; typos cause failures |
| **No authentication** | Any person running the program has full access to all operations |
| **In-memory only** | No database or file system is used; data is not saved between runs |

---

*Back to [README.md](../README.md)*  
*See also: [coding-practices.md](coding-practices.md)*
