import model.Book;
import model.User;
import service.LibraryService;
import utility.InputHelper;
import java.util.Locale;

import java.util.Scanner;

/**
 * Entry point for the Library Management System.
 *
 * LAYER: (entry point) -- wires layers together and runs the menu loop.
 *   Business logic  -> service.LibraryService
 *   Data objects    -> model.Book, model.User, model.Reservation
 *   Input helpers   -> utility.InputHelper
 *
 * STEP 13: Updated to use layered package imports.
 */
public class Main {

    private static final int ADD_BOOK           = 1;
    private static final int VIEW_BOOKS         = 2;
    private static final int SEARCH_BOOK        = 3;
    private static final int BORROW_BOOK        = 4;
    private static final int RETURN_BOOK        = 5;
    private static final int RESERVE_BOOK       = 6;
    private static final int VIEW_BORROWED      = 7;
    private static final int VIEW_RESERVATIONS  = 8;
    private static final int ADMIN_VIEW_BORROWED = 9;
    private static final int ADMIN_VIEW_RESERVATIONS = 10;
    private static final int EXIT               = 11;

    public static void main(String[] args) {

        Scanner        scanner = new Scanner(System.in);
        LibraryService library = new LibraryService();

        System.out.print("\n  Enter your name to get started: ");
        String userName = scanner.nextLine().trim();
        if (userName.isEmpty()) userName = "Guest";
        boolean isAdmin = userName.equalsIgnoreCase("admin");
        if (isAdmin) {
            System.out.print("  Enter admin password: ");
            String password = scanner.nextLine();
            if (!"abc@123".equals(password)) {
                System.out.println("  [Error] Incorrect password. Exiting.");
                return;
            }
        }
        User user = new User(1, userName);
        library.addOrUpdateUser(user);

        System.out.println("\n  Welcome, " + user.getUserName() + (isAdmin ? " (Admin)" : "") + "!");

        boolean running = true;
        while (running) {
            printMenu(isAdmin);
            int choice = InputHelper.readInt(scanner);

            switch (choice) {
                case ADD_BOOK:
                    handleAddBook(scanner, library);
                    break;
                case VIEW_BOOKS:
                    System.out.println("\n  ===== All Books =====");
                    library.displayAllBooks();
                    break;
                case SEARCH_BOOK:
                    handleSearchBook(scanner, library);
                    break;
                case BORROW_BOOK:
                    if (isAdmin) {
                        System.out.println("  [Error] Admin cannot borrow books.");
                        break;
                    }
                    handleBorrowBook(scanner, library, user);
                    break;
                case RETURN_BOOK:
                    if (isAdmin) {
                        System.out.println("  [Error] Admin cannot return books.");
                        break;
                    }
                    handleReturnBook(scanner, library, user);
                    break;
                case RESERVE_BOOK:
                    if (isAdmin) {
                        System.out.println("  [Error] Admin cannot reserve books.");
                        break;
                    }
                    handleReserveBook(scanner, library, user);
                    break;
                case VIEW_BORROWED:
                    if (isAdmin) {
                        System.out.println("  [Error] Admin cannot view personal borrowed books.");
                        break;
                    }
                    System.out.println("\n  ===== Your Borrowed Books =====");
                    user.displayBorrowedBooks();
                    break;
                case VIEW_RESERVATIONS:
                    if (isAdmin) {
                        System.out.println("  [Error] Admin cannot view personal reservations.");
                        break;
                    }
                    handleViewReservations(scanner, library);
                    break;
                case ADMIN_VIEW_BORROWED:
                    if (isAdmin) {
                        System.out.println("\n  ===== All Borrowed Books (Admin) =====");
                        library.displayAllBorrowedBooks();
                    } else {
                        System.out.println("  [Error] Only admin can access this option.");
                    }
                    break;
                case ADMIN_VIEW_RESERVATIONS:
                    if (isAdmin) {
                        System.out.println("\n  ===== All Reservations (Admin) =====");
                        library.displayAllReservations();
                    } else {
                        System.out.println("  [Error] Only admin can access this option.");
                    }
                    break;
                case EXIT:
                    System.out.println("\n  Thank you. Goodbye!\n");
                    running = false;
                    break;
                default:
                    System.out.println("  [Error] Enter 1-" + EXIT + ".");
            }
        }
        scanner.close();
    }

    private static void printMenu(boolean isAdmin) {
        System.out.println("\n  +==========================================+");
        System.out.println("  |      Library Management System           |");
        System.out.println("  +==========================================+");
        System.out.println("  |  1. Add Book                             |");
        System.out.println("  |  2. View All Books                       |");
        System.out.println("  |  3. Search Book by Title                 |");
        if (!isAdmin) {
            System.out.println("  |  4. Borrow Book                          |");
            System.out.println("  |  5. Return Book                          |");
            System.out.println("  |  6. Reserve Book                         |");
            System.out.println("  |  7. View My Borrowed Books               |");
            System.out.println("  |  8. View Reservations for a Book         |");
            System.out.println("  | 11. Exit                                 |");
        } else {
            System.out.println("  |  9. View All Borrowed Books (Admin)      |");
            System.out.println("  | 10. View All Reservations (Admin)        |");
            System.out.println("  | 11. Exit                                 |");
        }
        System.out.println("  +==========================================+");
        System.out.print("  Enter your choice: ");
    }

    private static void handleAddBook(Scanner scanner, LibraryService library) {
        System.out.println("\n  ===== Add New Book =====");
        System.out.print("  Enter Book ID   : ");
        int id = InputHelper.readInt(scanner);
        if (id == -1) {
            System.out.println("  [Error] Invalid ID -- please enter a number.");
            return;
        }
        String title  = InputHelper.readLine(scanner, "  Enter Title     : ");
        String author = InputHelper.readLine(scanner, "  Enter Author    : ");
        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("  [Error] Title and Author cannot be empty.");
            return;
        }
        library.addBook(new Book(id, title, author));
    }

    private static void handleSearchBook(Scanner scanner, LibraryService library) {
        System.out.println("\n  ===== Search Book =====");
        String title = InputHelper.readLine(scanner, "  Enter title to search: ");
        Book found = library.searchBookByTitle(title);
        if (found == null) {
            System.out.println("  No book found with title: \"" + title + "\"");
        } else {
            System.out.println("  Book found:");
            found.displayBookInfo();
        }
    }

    private static void handleBorrowBook(Scanner scanner, LibraryService library, User user) {
        System.out.println("\n  ===== Borrow Book =====");
        String title = InputHelper.readLine(scanner, "  Enter title to borrow: ");
        Book book = library.borrowBook(title);
        if (book != null) {
            user.borrowBook(book);
            library.saveAllData();
        }
    }

    private static void handleReturnBook(Scanner scanner, LibraryService library, User user) {
        System.out.println("\n  ===== Return Book =====");
        String title = InputHelper.readLine(scanner, "  Enter title to return: ");
        Book book = library.searchBookByTitle(title);
        if (book == null) {
            System.out.println("  [Error] Book not found: \"" + title + "\"");
            return;
        }
        user.returnBook(book);
        library.returnBook(title);
        library.saveAllData();
    }

    private static void handleReserveBook(Scanner scanner, LibraryService library, User user) {
        System.out.println("\n  ===== Reserve Book =====");
        String title = InputHelper.readLine(scanner, "  Enter title to reserve: ");
        library.reserveBook(title, user);
    }

    private static void handleViewReservations(Scanner scanner, LibraryService library) {
        System.out.println("\n  ===== View Reservations =====");
        String title = InputHelper.readLine(scanner, "  Enter book title: ");
        library.displayReservationsForBook(title);
    }
}
