package service;

import model.Book;
import model.Reservation;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import utility.BookPersistence;
import utility.LibraryDataPersistence;
import java.util.Set;

/**
 * Manages the library's book collection and reservation queues.
 *
 * LAYER: service — all business logic lives here.
 *   - Accepts model objects (Book, User, Reservation) as inputs/outputs.
 *   - Knows nothing about the console menu (that belongs to Main).
 *
 * STEP 11: Uses HashMap<String, Book> for O(1) title-based lookup.
 * STEP 12: Adds reservation queue with auto-assign on return.
 * STEP 13: Renamed from Library → LibraryService, moved to service layer.
 */
public class LibraryService {
    public void addOrUpdateUser(User user) {
        if (user != null) {
            users.put(user.getUserName().toLowerCase(), user);
        }
    }

    // ─── Fields ────────────────────────────────────────────────────────────────

    /** Key: lowercase title → Value: Book object. O(1) lookup. */
    private final HashMap<String, Book> books;
    private final Map<String, Queue<Reservation>> reservationQueues;
    private final HashMap<String, User> users; // For admin view and persistence

    // ─── Constructor ───────────────────────────────────────────────────────────

    public LibraryService() {
        this.books             = new HashMap<>();
        this.reservationQueues = new HashMap<>();
        this.users             = new HashMap<>();
        // Load books from file
        List<Book> loadedBooks = BookPersistence.loadBooks();
        for (Book b : loadedBooks) {
            books.put(b.getTitle().toLowerCase(), b);
        }
        // Load reservations and borrowed books
        Map<String, Queue<Reservation>> loadedRes = LibraryDataPersistence.loadReservations(books, users);
        reservationQueues.putAll(loadedRes);
        LibraryDataPersistence.loadBorrowedBooks(users.values(), books);
    }

    // ─── Add ───────────────────────────────────────────────────────────────────

    /**
     * Adds a Book to the library. Rejects null books.
     *
     * @param book The Book to add
     */
    public void addBook(Book book) {
        if (book == null) {
            System.out.println("  [Error] Cannot add a null book.");
            return;
        }
        books.put(book.getTitle().toLowerCase(), book);
        BookPersistence.saveBooks(books.values());
        saveAllData();
        System.out.println("  Book added successfully: \"" + book.getTitle() + "\"");
    }

    // ─── Search ────────────────────────────────────────────────────────────────

    /**
     * Searches for a book by title (case-insensitive, O(1)).
     *
     * @param title The title to search for
     * @return The matching Book, or null if not found
     */
    public Book searchBookByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }
        return books.get(title.trim().toLowerCase());
    }

    // ─── Display ───────────────────────────────────────────────────────────────

    /** Prints a formatted table of all books with a total count header. */
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("  No books in the library yet.");
            return;
        }
        System.out.println("  Total books in library: " + getTotalBooks());
        System.out.println("  " + "-".repeat(85));
        System.out.printf("  %-5s | %-30s | %-25s | %s%n", "ID", "Title", "Author", "Status");
        System.out.println("  " + "-".repeat(85));
        for (Book book : books.values()) {
            book.displayBookInfo();
        }
        System.out.println("  " + "-".repeat(85));
    }

    // ─── Borrow ────────────────────────────────────────────────────────────────

    /**
     * Marks a book as borrowed.
     * Returns the Book on success, or null if not found / already borrowed.
     *
     * @param title The title of the book to borrow
     * @return The borrowed Book, or null
     */
    public Book borrowBook(String title) {
        Book book = searchBookByTitle(title);
        if (book == null) {
            System.out.println("  [Error] Book not found: \"" + title + "\"");
            return null;
        }
        if (!book.isAvailable()) {
            System.out.println("  [Error] \"" + book.getTitle() + "\" is already borrowed.");
            return null;
        }
        book.setAvailable(false);
        BookPersistence.saveBooks(books.values());
        saveAllData();
        return book;
    }

    // ─── Return ────────────────────────────────────────────────────────────────

    /**
     * Marks a book as available after it is returned.
     * If a reservation queue exists for this book, auto-assigns to the next user.
     *
     * @param title The title of the book being returned
     * @return The returned Book, or null if not found
     */
    public Book returnBook(String title) {
        Book book = searchBookByTitle(title);
        if (book == null) {
            System.out.println("  [Error] Book not found: \"" + title + "\"");
            return null;
        }
        Queue<Reservation> queue = reservationQueues.get(title.trim().toLowerCase());
        if (queue != null && !queue.isEmpty()) {
            // Auto-assign to the next user in the reservation queue
            Reservation next     = queue.poll();
            User        nextUser = next.getUser();
            book.setAvailable(false);   // stays borrowed — assigned to next user
            nextUser.borrowBook(book);
            System.out.println("  Auto-assigned \"" + book.getTitle()
                    + "\" to " + nextUser.getUserName() + " from the reservation queue.");
        } else {
            book.setAvailable(true);    // no one waiting — simply put back on shelf
        }
        BookPersistence.saveBooks(books.values());
        saveAllData();
        return book;
    }

    // ─── Reserve ───────────────────────────────────────────────────────────────

    /**
     * Adds a User to the reservation queue for an unavailable book.
     * If the book is currently available, suggests borrowing it directly.
     *
     * @param title The title of the book to reserve
     * @param user  The user making the reservation
     */
    public void reserveBook(String title, User user) {
        Book book = searchBookByTitle(title);
        if (book == null) {
            System.out.println("  [Error] Book not found: \"" + title + "\"");
            return;
        }
        if (book.isAvailable()) {
            System.out.println("  \"" + book.getTitle()
                    + "\" is currently available — borrow it directly (option 4).");
            return;
        }
        String key = title.trim().toLowerCase();
        reservationQueues.computeIfAbsent(key, k -> new LinkedList<>())
                         .add(new Reservation(user, book));
        users.putIfAbsent(user.getUserName().toLowerCase(), user);
        saveAllData();
        int position = reservationQueues.get(key).size();
        System.out.println("  Reservation confirmed! You are #" + position
                + " in the queue for \"" + book.getTitle() + "\".");
    }

    // ─── Display Reservations ──────────────────────────────────────────────────

    /**
     * Prints the full reservation queue for a given book.
     *
     * @param title The book title to inspect
     */
    public void displayReservationsForBook(String title) {
        String             key   = title.trim().toLowerCase();
        Queue<Reservation> queue = reservationQueues.get(key);
        if (queue == null || queue.isEmpty()) {
            System.out.println("  No reservations for \"" + title + "\".");
            return;
        }
        System.out.println("  Reservation queue for \"" + title
                + " (" + queue.size() + " waiting):");
        int pos = 1;
        for (Reservation r : queue) {
            System.out.println("    " + pos++ + ". User: [hidden] | Reserved at: " + r.getReservedAt());
        }
    }

    // Admin: View all reservations with user info
    public void displayAllReservations() {
        if (reservationQueues.isEmpty()) {
            System.out.println("  No reservations in the library.");
            return;
        }
        for (String title : reservationQueues.keySet()) {
            Queue<Reservation> queue = reservationQueues.get(title);
            if (queue == null || queue.isEmpty()) continue;
            System.out.println("  Book: " + title);
            int pos = 1;
            for (Reservation r : queue) {
                System.out.println("    " + pos++ + ". User: " + r.getUser().getUserName() + " | Reserved at: " + r.getReservedAt());
            }
        }
    }

    // Admin: View all borrowed books with user info
    public void displayAllBorrowedBooks() {
        boolean found = false;
        for (User user : users.values()) {
            if (user.getBorrowedBooks().isEmpty()) continue;
            found = true;
            System.out.println("  User: " + user.getUserName());
            for (Book book : user.getBorrowedBooks()) {
                System.out.println("    - [" + book.getId() + "] " + book.getTitle() + " by " + book.getAuthor());
            }
        }
        if (!found) System.out.println("  No borrowed books in the library.");
    }

    // Save all reservations and borrowed books
    private void saveAllData() {
        LibraryDataPersistence.saveReservations(reservationQueues);
        LibraryDataPersistence.saveBorrowedBooks(users.values());
    }

    // ─── Utility ───────────────────────────────────────────────────────────────

    /** @return Total number of books in the library */
    public int getTotalBooks() {
        return books.size();
    }

    /** @return All Book objects (for external iteration) */
    public Collection<Book> getAllBooks() {
        return books.values();
    }
}
