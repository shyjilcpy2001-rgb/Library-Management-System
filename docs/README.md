# 📚 Library Management System

> A beginner-friendly Java console application for managing books, users, and borrowing in a library.

---

## 📖 Project Overview

The **Library Management System** is a simple Java application that runs in the terminal (console). It allows a librarian or user to **add books**, **search for books**, **borrow books**, and **return books** — all through a text-based menu.

This project is great for beginners because it covers core Java concepts like:
- **Classes and Objects** (Book, Library, User)
- **ArrayList** (storing a list of books)
- **Methods** (actions like borrowBook, returnBook)
- **Scanner** (reading user input from the keyboard)

---

## ✨ Features

| Feature | Description |
|---|---|
| Add Book | Add a new book to the library with ID, title, and author |
| View All Books | Display a list of all books and their availability |
| Search Book | Find a book by its title |
| Borrow Book | Mark a book as borrowed by a user |
| Return Book | Mark a borrowed book as available again |
| Duplicate Prevention | A user cannot borrow the same book twice |
| Input Validation | Handles missing books, already-borrowed books, and invalid input |

---

## 🗂️ Folder Structure

```
TestApp/
│
├── src/                    ← All Java source code files live here
│   ├── Book.java           ← Represents a single book
│   ├── Library.java        ← Manages the collection of books
│   ├── User.java           ← Represents a library user
│   └── Main.java           ← Entry point; shows the menu
│
├── bin/                    ← Compiled .class files go here (auto-generated)
│
├── docs/                   ← Project documentation
│   ├── requirements.md     ← What the system must do
│   └── coding-practices.md ← How the code is written
│
└── README.md               ← This file — project overview and guide
```

---

## 🧩 Class Explanation

### 1. `Book.java`

This class represents **a single book** in the library. Think of it as a "blueprint" for every book object.

**Fields (data it stores):**

| Field | Type | Description |
|---|---|---|
| `id` | `int` | A unique number to identify the book |
| `title` | `String` | The name of the book |
| `author` | `String` | Who wrote the book |
| `isAvailable` | `boolean` | `true` if the book can be borrowed, `false` if it's out |

**Methods (actions it can do):**

| Method | Description |
|---|---|
| `Book(id, title, author)` | Constructor — creates a new Book object |
| `getId()` | Returns the book's ID |
| `getTitle()` | Returns the book's title |
| `getAuthor()` | Returns the book's author |
| `isAvailable()` | Returns whether the book is available |
| `setAvailable(boolean)` | Sets the book's availability |
| `displayBookInfo()` | Prints the book's details to the console |

---

### 2. `Library.java`

This class manages the **entire collection of books**. It holds a list of `Book` objects and provides actions to work with them.

**Fields:**

| Field | Type | Description |
|---|---|---|
| `books` | `ArrayList<Book>` | A dynamic list that holds all Book objects |

**Methods:**

| Method | Description |
|---|---|
| `addBook(Book)` | Adds a new book to the library |
| `searchBookByTitle(String)` | Finds and returns a book matching the given title |
| `displayAllBooks()` | Prints info for every book in the list |
| `borrowBook(String)` | Marks a book as unavailable (borrowed) |
| `returnBook(String)` | Marks a book as available again (returned) |

---

### 3. `User.java`

This class represents **a person who uses the library**. It tracks which books they have borrowed.

**Fields:**

| Field | Type | Description |
|---|---|---|
| `userId` | `int` | A unique number to identify the user |
| `userName` | `String` | The user's name |
| `borrowedBooks` | `ArrayList<Book>` | List of books this user currently has borrowed |

**Methods:**

| Method | Description |
|---|---|
| `borrowBook(Book)` | Adds a book to the user's borrowed list |
| `returnBook(Book)` | Removes a book from the user's borrowed list |
| `displayBorrowedBooks()` | Lists all books the user currently has |

---

### 4. `Main.java`

This is the **starting point** of the application. It creates a `Library` and `User` object, then displays a menu loop where the user picks an option by typing a number.

**Menu Options:**

```
1. Add Book
2. View Books
3. Search Book
4. Borrow Book
5. Return Book
6. Exit
```

It uses `Scanner` to read what the user types and calls the right method based on their choice.

---

## ⚙️ How the Project Works

Here is the step-by-step flow of what happens when you run the program:

```
Program Starts (Main.java)
        │
        ▼
Creates Library object (empty book list)
Creates User object
        │
        ▼
Shows Menu in a loop
        │
        ├──► 1. Add Book     → calls library.addBook(new Book(...))
        ├──► 2. View Books   → calls library.displayAllBooks()
        ├──► 3. Search Book  → calls library.searchBookByTitle(title)
        ├──► 4. Borrow Book  → calls library.borrowBook(title) + user.borrowBook(book)
        ├──► 5. Return Book  → calls library.returnBook(title) + user.returnBook(book)
        └──► 6. Exit         → breaks loop, program ends
```

**Key relationship between classes:**

- `Main` creates and uses both `Library` and `User`.
- `Library` owns and manages `Book` objects.
- `User` holds references to `Book` objects they have borrowed.

---

## 🚀 Execution / Setup Steps

### Prerequisites

Make sure you have the following installed:

- **Java JDK 8 or higher** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- A terminal / command prompt

### Step 1 — Verify Java is installed

```bash
java -version
```

You should see output like: `java version "17.0.x"`

### Step 2 — Navigate to the project folder

```bash
cd path/to/TestApp
```

### Step 3 — Compile all Java files

This compiles all `.java` files in `src/` and puts the compiled `.class` files into `bin/`.

```bash
javac -d bin src/*.java
```

> **Note:** If the `bin/` folder doesn't exist yet, create it first:
> ```bash
> mkdir bin
> ```

### Step 4 — Run the application

```bash
java -cp bin Main
```

The menu will appear in your terminal!

---

## 💻 Example Outputs

**Starting the application:**
```
===== Library Management System =====
1. Add Book
2. View Books
3. Search Book
4. Borrow Book
5. Return Book
6. Exit
Enter your choice:
```

**Adding a book (Option 1):**
```
Enter Book ID: 101
Enter Title: The Great Gatsby
Enter Author: F. Scott Fitzgerald
Book added successfully!
```

**Viewing all books (Option 2):**
```
----- All Books -----
ID: 101 | Title: The Great Gatsby | Author: F. Scott Fitzgerald | Status: Available
ID: 102 | Title: To Kill a Mockingbird | Author: Harper Lee | Status: Borrowed
```

**Searching for a book (Option 3):**
```
Enter title to search: The Great Gatsby
Book Found!
ID: 101 | Title: The Great Gatsby | Author: F. Scott Fitzgerald | Status: Available
```

**Borrowing a book (Option 4):**
```
Enter title to borrow: The Great Gatsby
Book borrowed successfully!
```

**Trying to borrow an already-borrowed book:**
```
Enter title to borrow: The Great Gatsby
Sorry, this book is not available right now.
```

**Returning a book (Option 5):**
```
Enter title to return: The Great Gatsby
Book returned successfully!
```

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|---|---|---|
| Java | JDK 8+ | Core programming language |
| Java `ArrayList` | Built-in | Storing and managing collections of books |
| Java `Scanner` | Built-in | Reading user input from the keyboard |
| Markdown | — | Writing documentation |

---

## 🔮 Future Improvements

Here are some ideas to make this project even better:

| Idea | Description |
|---|---|
| **Reservation System** | Let users reserve a book that is currently borrowed |
| **Multiple Users** | Support login for different users with their own borrow history |
| **File Storage** | Save books and users to a `.txt` or `.csv` file so data persists after the program closes |
| **Database Integration** | Connect to a MySQL or SQLite database for real data storage |
| **Due Dates** | Track when a borrowed book is due back |
| **Fine Calculation** | Charge a fine for overdue books |
| **GUI Interface** | Build a graphical user interface using Java Swing or JavaFX |
| **Layered Architecture** | Separate code into `model`, `service`, and `utility` layers for better organization |

---

## 👤 Author

Created as a beginner Java learning project.

---

*For detailed requirements, see [docs/requirements.md](docs/requirements.md).*  
*For coding practices and guidelines, see [docs/coding-practices.md](docs/coding-practices.md).*
