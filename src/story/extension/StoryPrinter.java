package story.extension;

public class StoryPrinter {

    private static int textDelay = 35;

    public static void setTextDelay(int ms) {
        textDelay = ms;
    }

    // Print slow text with Enter key skip for this paragraph only
    public static void printSlowWithSkip(String text) {
        final boolean[] skipThisParagraph = {false};

        Thread skipThread = new Thread(() -> {
            try {
                while (System.in.available() == 0) {
                    Thread.sleep(10);
                }
                skipThisParagraph[0] = true;  // Enter pressed
                System.in.read();             // consume Enter so next call won't be skipped
            } catch (Exception e) {
                // ignore
            }
        });

        skipThread.start();

        for (int i = 0; i < text.length(); i++) {
            if (skipThisParagraph[0]) {
                // Enter pressed → print the rest of THIS paragraph instantly
                System.out.print(text.substring(i));
                break;
            }

            System.out.print(text.charAt(i));

            try {
                Thread.sleep(textDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println();
    }

    // Slow print without newline
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

