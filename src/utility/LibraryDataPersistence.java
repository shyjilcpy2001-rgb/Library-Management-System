package utility;

import model.Book;
import model.User;
import model.Reservation;
import java.io.*;
import java.util.*;

/**
 * Handles saving and loading reservations and borrowed books for persistence.
 */
public class LibraryDataPersistence {
    private static final String RESERVATIONS_FILE = "reservations.csv";
    private static final String BORROWED_FILE = "borrowed.csv";

    // --- Reservations ---
    public static void saveReservations(Map<String, Queue<Reservation>> reservationQueues) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Map.Entry<String, Queue<Reservation>> entry : reservationQueues.entrySet()) {
                String bookTitle = entry.getKey();
                for (Reservation r : entry.getValue()) {
                    writer.printf("%s,%s,%d\n",
                        escape(bookTitle),
                        escape(r.getUser().getUserName()),
                        r.getTimestamp()
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("  [Error] Failed to save reservations: " + e.getMessage());
        }
    }

    public static Map<String, Queue<Reservation>> loadReservations(Map<String, Book> books, Map<String, User> users) {
        Map<String, Queue<Reservation>> queues = new HashMap<>();
        File file = new File(RESERVATIONS_FILE);
        if (!file.exists()) return queues;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 3) continue;
                String bookTitle = unescape(parts[0]);
                String userName = unescape(parts[1]);
                long timestamp = Long.parseLong(parts[2]);
                Book book = books.get(bookTitle.toLowerCase());
                User user = users.computeIfAbsent(userName.toLowerCase(), n -> new User(-1, userName));
                Reservation r = new Reservation(user, book, timestamp);
                queues.computeIfAbsent(bookTitle.toLowerCase(), k -> new LinkedList<>()).add(r);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("  [Error] Failed to load reservations: " + e.getMessage());
        }
        return queues;
    }

    // --- Borrowed Books ---
    public static void saveBorrowedBooks(Collection<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BORROWED_FILE))) {
            for (User user : users) {
                for (Book book : user.getBorrowedBooks()) {
                    writer.printf("%s,%s\n", escape(user.getUserName()), escape(book.getTitle()));
                }
            }
        } catch (IOException e) {
            System.out.println("  [Error] Failed to save borrowed books: " + e.getMessage());
        }
    }

    public static void loadBorrowedBooks(Collection<User> users, Map<String, Book> books) {
        File file = new File(BORROWED_FILE);
        if (!file.exists()) return;
        Map<String, User> userMap = new HashMap<>();
        for (User u : users) userMap.put(u.getUserName().toLowerCase(), u);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = splitCsv(line);
                if (parts.length < 2) continue;
                String userName = unescape(parts[0]);
                String bookTitle = unescape(parts[1]);
                User user = userMap.get(userName.toLowerCase());
                Book book = books.get(bookTitle.toLowerCase());
                if (user != null && book != null) {
                    user.borrowBook(book);
                }
            }
        } catch (IOException e) {
            System.out.println("  [Error] Failed to load borrowed books: " + e.getMessage());
        }
    }

    // --- CSV helpers ---
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }
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
