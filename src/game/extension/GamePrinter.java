package game.extension;

public class GamePrinter {

    // Set delay between characters (milliseconds)
    
    private static int textDelay = 20;

    public static void setTextDelay(int ms) {
        textDelay = ms;
    }

    public static void printSlow(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
