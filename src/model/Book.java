package model;

/**
 * Represents a single book in the library.
 * LAYER: model — pure data, no business logic, no console I/O except display.
 */
public class Book {

    private int     id;
    private String  title;
    private String  author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id          = id;
        this.title       = title;
        this.author      = author;
        this.isAvailable = true;
    }

    // ─── Getters ───────────────────────────────────────────────────────────────

    public int     getId()        { return id; }
    public String  getTitle()     { return title; }
    public String  getAuthor()    { return author; }
    public boolean isAvailable()  { return isAvailable; }

    // ─── Setter ────────────────────────────────────────────────────────────────

    public void setAvailable(boolean available) { this.isAvailable = available; }

    // ─── Display ───────────────────────────────────────────────────────────────

    /** Prints this book's one-line summary to the console. */
    public void displayBookInfo() {
        System.out.println("  " + this);
    }

    /** Returns a formatted summary string — model owns its own representation. */
    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Borrowed";
        return String.format("ID: %-5d | Title: %-30s | Author: %-25s | Status: %s",
                id, title, author, status);
    }
}
