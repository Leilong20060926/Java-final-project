package story.extension;

import java.util.Scanner;

public class StoryPrinter {

    // Set delay between characters (milliseconds)
    private static int textDelay = 35;

    // Set the scanner to capture user input
    private static Scanner scanner = new Scanner(System.in);

    public static void setTextDelay(int ms) {
        textDelay = ms;
    }

    // Print slow text with delay, but allow the user to skip using Enter
    public static void printSlowWithSkip(String text) {
        // Start a thread to detect Enter key press
        Thread skipThread = new Thread(() -> {
            if(scanner.hasNextLine()) scanner.nextLine();  // Wait for Enter key press
        });

        // Start the thread
        skipThread.start();

        // Print the text with delay, check if the user pressed Enter
        for (char c : text.toCharArray()) {
            // Check if the user pressed Enter to skip
            if (skipThread.isAlive()) {
                System.out.print(c);
                try {
                    Thread.sleep(textDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                // If Enter is pressed, break out of the loop and print the rest instantly
                System.out.print(text.substring(text.indexOf(c)));
                break;
            }
        }
        System.out.println();
    }


    // Print slow text without newline

    public static void printSlowRaw(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
