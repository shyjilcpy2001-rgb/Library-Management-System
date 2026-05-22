# Coding Practices — Library Management System

> This document explains the coding standards, design principles, and best practices used in this Java project. It is written for **beginner Java developers**.

---

## 1. Naming Conventions

> Good names make code **readable without comments**. Here are the rules used in this project.

### General Rules

| Element | Convention | Example |
|---|---|---|
| Class | `PascalCase` — every word starts with uppercase | `Book`, `Library`, `User` |
| Method | `camelCase` — first word lowercase, rest start with uppercase | `addBook()`, `displayBookInfo()` |
| Variable | `camelCase` | `bookTitle`, `isAvailable`, `borrowedBooks` |
| Constant | `UPPER_SNAKE_CASE` — all caps, words separated by `_` | `MAX_BORROW_LIMIT` |
| Package | All lowercase | `com.library.model` |
| Boolean variable | Start with `is`, `has`, or `can` | `isAvailable`, `hasBorrowed` |

### ✅ Good vs ❌ Bad Examples

```java
// ✅ GOOD — clear, descriptive names
String bookTitle;
boolean isAvailable;
void displayBookInfo() {}
ArrayList<Book> borrowedBooks;

// ❌ BAD — vague or unclear names
String s;
boolean flag;
void show() {}
ArrayList<Book> list;
```

---

## 2. Class Design Guidelines

> A class should represent **one thing** and do it well. This is called the **Single Responsibility Principle** (more on that in Section 7).

### Rules Used in This Project

- Each class has **one clear purpose**:
  - `Book` → Represents a book's data
  - `Library` → Manages the collection of books
  - `User` → Represents a user and their borrowed books
  - `Main` → Handles the menu and program flow

- Fields should be **private** (hidden from outside) — accessed only through getters/setters.

- Classes should have a **constructor** to initialize required data.

```java
// ✅ GOOD class structure
public class Book {
    // Private fields (hidden from outside)
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    // Constructor — sets up the object when created
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true; // All new books start as available
    }

    // Getter — lets outside code READ the value
    public String getTitle() {
        return title;
    }

    // Setter — lets outside code CHANGE the value
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
}
```

---

## 3. Method Writing Best Practices

> A method should do **one thing** and be **short enough to understand at a glance**.

### Rules

| Rule | Explanation |
|---|---|
| **One job per method** | If a method does many things, split it into smaller methods |
| **Descriptive name** | The name should tell you exactly what it does |
| **Short and focused** | Aim for methods under 20 lines when possible |
| **Use parameters** | Pass data into methods rather than relying on global state |
| **Return meaningful values** | Return results instead of just printing everything inside the method |

### ✅ Good Example

```java
// This method does ONE thing: search for a book by title
public Book searchBookByTitle(String title) {
    for (Book book : books) {
        if (book.getTitle().equalsIgnoreCase(title)) {
            return book;
        }
    }
    return null; // Book not found
}
```

### ❌ Bad Example

```java
// ❌ This method does too many things at once
public void doEverything(String title) {
    for (Book book : books) {
        if (book.getTitle().equals(title)) {
            System.out.println("Found: " + book.getTitle());
            book.setAvailable(false);
            System.out.println("Borrowed successfully");
            // ... and more ...
        }
    }
}
```

---

## 4. Exception Handling Rules

> Exception handling prevents your program from **crashing** when something unexpected happens.

### Common Issues to Guard Against

| Problem | What Happens | How to Handle |
|---|---|---|
| `NullPointerException` | Calling a method on a `null` object | Check `if (object != null)` before use |
| Invalid menu input | User types a letter instead of a number | Wrap `Scanner.nextInt()` in `try-catch` |
| Book not found | Searching/borrowing a book that doesn't exist | Check the return value for `null` |
| Borrowing unavailable book | User tries to borrow an already-borrowed book | Check `isAvailable` before borrowing |

### ✅ Good Exception Handling Example

```java
// Always check if a book was actually found before using it
public void borrowBook(String title) {
    Book book = searchBookByTitle(title);

    // Null check: what if the book doesn't exist?
    if (book == null) {
        System.out.println("Book not found: " + title);
        return;
    }

    // Availability check: is the book currently available?
    if (!book.isAvailable()) {
        System.out.println("Sorry, this book is currently not available.");
        return;
    }

    book.setAvailable(false);
    System.out.println("Book borrowed successfully!");
}
```

### ✅ Handling Invalid User Input in Main

```java
// Wrap nextInt() in try-catch to prevent crashes from bad input
try {
    int choice = scanner.nextInt();
    // process choice...
} catch (InputMismatchException e) {
    System.out.println("Please enter a valid number.");
    scanner.nextLine(); // Clear the bad input from the buffer
}
```

---

## 5. Code Formatting Standards

> Consistent formatting makes code **easier to read and review**.

### Rules

| Rule | Standard |
|---|---|
| **Indentation** | Use 4 spaces (not tabs) per level |
| **Braces** | Opening `{` on the same line as the statement |
| **Blank lines** | One blank line between methods; two between class sections |
| **Line length** | Keep lines under ~100 characters |
| **One statement per line** | Never put two statements on one line |
| **Space around operators** | `int x = a + b;` not `int x=a+b;` |

### ✅ Well-Formatted Code

```java
public class Library {

    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
            System.out.println("Book added: " + book.getTitle());
        }
    }

    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book book : books) {
            book.displayBookInfo();
        }
    }
}
```

### ❌ Poorly Formatted Code

```java
public class Library{
private ArrayList<Book> books;
public Library(){this.books=new ArrayList<>();}
public void addBook(Book book){if(book!=null){books.add(book);System.out.println("added");}}
}
```

---

## 6. Commenting Guidelines

> Comments explain the **why**, not the **what**. Good code mostly explains itself; comments fill in the gaps.

### When to Comment

| Situation | What to Write |
|---|---|
| Complex logic | Explain *why* you chose a certain approach |
| Method purpose | Brief Javadoc comment above the method |
| Important decision | Note any assumptions made |
| TODO items | Mark unfinished or planned improvements |

### Comment Types Used

```java
// ─── Single-line comment ───────────────────────────
// Sets availability to false when a book is borrowed
book.setAvailable(false);


// ─── Javadoc comment (for methods and classes) ─────
/**
 * Searches the library for a book by its title.
 *
 * @param title The title to search for (case-insensitive)
 * @return The matching Book object, or null if not found
 */
public Book searchBookByTitle(String title) { ... }


// ─── TODO comment ──────────────────────────────────
// TODO: Add case-insensitive search support
```

### ❌ Avoid Useless Comments

```java
// ❌ This comment adds no value — the code already says this
int id = book.getId(); // get the id

// ✅ This comment explains WHY, not WHAT
book.setAvailable(true); // Reset availability so others can borrow it
```

---

## 7. SOLID Principles Used

> SOLID is a set of 5 design principles that make code easier to understand, maintain, and grow. Here's how this project applies them.

---

### S — Single Responsibility Principle (SRP)

> **"A class should have only one reason to change."**

Each class in this project has exactly one job:

| Class | Single Responsibility |
|---|---|
| `Book` | Store and display data about one book |
| `Library` | Manage the collection of books (add, search, borrow, return) |
| `User` | Represent a user and track their borrowed books |
| `Main` | Handle the console menu and connect everything together |

```java
// ✅ Book only manages book data — it doesn't talk to the database or draw UI
public class Book {
    private String title;
    private boolean isAvailable;
    // ... getters, setters, displayBookInfo()
}
```

---

### O — Open/Closed Principle (OCP)

> **"Classes should be open for extension, but closed for modification."**

For example, to add a `ReservedBook` type, you could **extend** `Book` without changing the original `Book` class:

```java
// ✅ Extend Book to add reservation behavior — don't modify Book itself
public class ReservedBook extends Book {
    private String reservedBy;
    // ... new fields and methods
}
```

---

### L — Liskov Substitution Principle (LSP)

> **"A subclass should be usable wherever its parent class is expected."**

If you create a subclass of `Book`, it must still behave correctly as a `Book`. Any method expecting a `Book` should work with a `ReservedBook` too.

---

### I — Interface Segregation Principle (ISP)

> **"Don't force a class to implement methods it doesn't need."**

In a future version, you might create interfaces like `Borrowable` and `Searchable` instead of one large interface, so each class only implements what it actually uses.

---

### D — Dependency Inversion Principle (DIP)

> **"Depend on abstractions, not concrete implementations."**

In a future layered architecture, `Main` should depend on a `LibraryService` interface rather than a specific `Library` class, making it easier to swap implementations.

---

## 8. Clean Code Principles

> Clean code is code that **any developer can read and understand quickly**.

| Principle | Explanation | Example from This Project |
|---|---|---|
| **Meaningful Names** | Names should reveal intent | `isAvailable` instead of `flag` |
| **Small Functions** | Each function does one thing | `searchBookByTitle()` only searches |
| **DRY (Don't Repeat Yourself)** | Avoid copy-pasting logic; put it in a shared method | Borrow validation logic in one place |
| **Avoid Magic Numbers** | Use named constants instead of bare numbers | Use `MAX_BOOKS = 100` instead of `100` |
| **Fail Fast** | Check for errors early and return/exit immediately | `if (book == null) return;` at the top of a method |
| **No Dead Code** | Remove unused variables, methods, or commented-out code | Clean up debug `System.out.println` before final version |
| **Consistent Style** | The whole codebase looks like it was written by one person | Same indentation, same brace style throughout |

### ✅ Fail Fast Pattern (used in this project)

```java
public void borrowBook(String title) {
    Book book = searchBookByTitle(title);

    // Fail fast: exit the method early if something is wrong
    if (book == null) {
        System.out.println("Book not found.");
        return;
    }

    if (!book.isAvailable()) {
        System.out.println("Book is already borrowed.");
        return;
    }

    // Happy path: only reaches here if all checks passed
    book.setAvailable(false);
    System.out.println("Borrowed successfully!");
}
```

---

## 9. Git Commit Best Practices

> Good commit messages make your project history readable. Anyone (including future you) can understand what changed and why.

### Commit Message Format

```
<type>: <short description>

[Optional longer explanation]
```

### Commit Types

| Type | When to Use | Example |
|---|---|---|
| `feat` | Adding a new feature | `feat: add borrow book functionality` |
| `fix` | Fixing a bug | `fix: prevent null pointer in searchBookByTitle` |
| `refactor` | Improving code without changing behavior | `refactor: extract validation into helper method` |
| `docs` | Adding or updating documentation | `docs: add README and requirements` |
| `style` | Formatting changes only (no logic change) | `style: fix indentation in Library.java` |
| `test` | Adding or updating tests | `test: add unit test for borrowBook method` |
| `chore` | Maintenance tasks (build scripts, etc.) | `chore: add .gitignore for bin/ folder` |

### ✅ Good Commit Messages

```
feat: add User class with borrowBook and returnBook methods

fix: null check added in Library.borrowBook to prevent crash

docs: create beginner-friendly README with setup instructions

refactor: rename 'show' to 'displayBookInfo' for clarity
```

### ❌ Bad Commit Messages

```
update
fixed stuff
changes
wip
aaa
```

### General Git Tips for Beginners

- **Commit often** — small, focused commits are better than one huge commit
- **One concern per commit** — don't mix a bug fix and a new feature in the same commit
- **Never commit broken code** — make sure the code compiles before committing
- **Use `.gitignore`** — exclude the `bin/` folder (compiled files) from commits

#### Example `.gitignore` for this project

```
# Compiled Java class files
bin/
*.class

# OS files
.DS_Store
Thumbs.db

# IDE files
.idea/
*.iml
.vscode/
```

---

*Back to [README.md](../README.md)*  
*See also: [requirements.md](requirements.md)*
