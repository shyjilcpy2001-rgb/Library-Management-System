package model;

import java.util.ArrayList;

/**
 * Represents a library user who can borrow and return books.
 * LAYER: model — tracks user state (borrowed books list), validates actions.
 */
public class User {

    private int              userId;
    private String           userName;
    private ArrayList<Book>  borrowedBooks;

    public User(int userId, String userName) {
        this.userId        = userId;
        this.userName      = userName;
        this.borrowedBooks = new ArrayList<>();
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public int    getUserId()   { return userId; }
    public String getUserName() { return userName; }


    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    // ─── Borrow ────────────────────────────────────────────────────────────────

    /**
     * Adds a book to this user's borrowed list.
     * Prevents duplicate borrowing (same Book instance checked).
     */
    public void borrowBook(Book book) {
        if (book == null) {
            System.out.println("  [Error] Cannot borrow a null book.");
            return;
        }
        if (isAlreadyBorrowed(book)) {
            System.out.println("  [Error] You have already borrowed \"" + book.getTitle() + "\".");
            return;
        }
        borrowedBooks.add(book);
        System.out.println("  \"" + book.getTitle() + "\" borrowed successfully by " + userName + ".");
    }

    // ─── Return ────────────────────────────────────────────────────────────────

    /**
     * Removes a book from this user's borrowed list.
     * Fails gracefully if the user does not hold the book.
     */
    public void returnBook(Book book) {
        if (book == null) {
            System.out.println("  [Error] Cannot return a null book.");
            return;
        }
        if (!isAlreadyBorrowed(book)) {
            System.out.println("  [Error] You have not borrowed \"" + book.getTitle() + "\".");
            return;
        }
        borrowedBooks.remove(book);
        System.out.println("  \"" + book.getTitle() + "\" returned successfully by " + userName + ".");
    }

    // ─── Display ───────────────────────────────────────────────────────────────

    /** Prints all books currently borrowed by this user. */
    public void displayBorrowedBooks() {
        System.out.println("  Books borrowed by " + userName + ":");
        if (borrowedBooks.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        for (Book book : borrowedBooks) {
            System.out.println("    - [" + book.getId() + "] "
                    + book.getTitle() + " by " + book.getAuthor());
        }
    }

    // ─── Helper ────────────────────────────────────────────────────────────────

    private boolean isAlreadyBorrowed(Book book) {
        return borrowedBooks.contains(book);
    }
}
