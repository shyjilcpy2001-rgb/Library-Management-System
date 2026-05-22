package utility;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility class for safe, reusable console input operations.
 *
 * LAYER: utility — stateless helper methods with no business logic.
 * STEP 13: Extracted from Main to give it a proper home and make
 *          input reading reusable across the application.
 *
 * Key improvement over the old inline approach:
 *   readInt() ALWAYS consumes the trailing newline after a valid integer,
 *   so callers never need an extra scanner.nextLine() call.
 */
public class InputHelper {

    // Private constructor — this class has only static utility methods;
    // it should never be instantiated.
    private InputHelper() {}

    /**
     * Safely reads an integer from the console.
     *
     * On success : reads the int, consumes the trailing newline, returns the value.
     * On bad input: clears the invalid token, returns -1 (sentinel for "invalid").
     *
     * @param scanner The active Scanner instance
     * @return The integer entered, or -1 if the input was not a valid number
     */
    public static int readInt(Scanner scanner) {
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // always consume the trailing newline
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // clear the invalid input from the buffer
            return -1;
        }
    }

    /**
     * Prints a prompt and reads a trimmed, non-empty line from the console.
     *
     * @param scanner The active Scanner instance
     * @param prompt  The text to display before reading (e.g. "  Enter title: ")
     * @return The trimmed line the user typed
     */
    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
