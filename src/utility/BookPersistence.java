package utility;

import model.Book;
import java.io.*;
import java.util.*;

/**
 * Handles saving and loading the book list to/from a file for persistence.
 */
public class BookPersistence {
    private static final String BOOKS_FILE = "books.csv";

    /**
     * Saves all books to a CSV file in the working directory.
     */
    public static void saveBooks(Collection<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.printf("%d,%s,%s,%b\n",
                        book.getId(),
                        escape(book.getTitle()),
                        escape(book.getAuthor()),
                        book.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("  [Error] Failed to save books: " + e.getMessage());
        }
    }

    /**
     * Loads books from the CSV file. Returns a list of Book objects.
     */
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        if (!file.exists()) return books;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 4) continue;
                int id = Integer.parseInt(parts[0]);
                String title = unescape(parts[1]);
                String author = unescape(parts[2]);
                boolean available = Boolean.parseBoolean(parts[3]);
                Book book = new Book(id, title, author);
                book.setAvailable(available);
                books.add(book);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [Error] Failed to load books: " + e.getMessage());
        }
        return books;
    }

    // Escapes commas and newlines for CSV
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }
    // Splits a CSV line, handling escaped commas
    private static String[] splitCsv(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escape) {
                sb.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else if (c == ',') {
                parts.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        parts.add(sb.toString());
        return parts.toArray(new String[0]);
    }
}
