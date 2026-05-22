package model; 

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents a reservation — one User waiting for one unavailable Book.      
 * LAYER: model — pure data + timestamp, no business logic.
 */
public class Reservation {

    private User user;
    private Book book;
    private long timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Reservation(User user, Book book) {
        this(user, book, System.currentTimeMillis());
    }

    public Reservation(User user, Book book, long timestamp) {
        this.user = user;
        this.book = book;
        this.timestamp = timestamp;
    }

    public User getUser() { return user; }
    public Book getBook() { return book; }
    public long getTimestamp() { return timestamp; }

    public String getReservedAt() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()).format(FORMATTER);
    }

    @Override
    public String toString() {
        return "User: " + user.getUserName()
                + " | Book: " + book.getTitle()
                + " | Reserved at: " + getReservedAt();
    }
}
