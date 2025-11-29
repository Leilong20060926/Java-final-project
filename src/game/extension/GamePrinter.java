package game.extension;

public class GamePrinter {
    // 設定每個字元之間的延遲時間（毫秒）
    private static int textDelay = 35;

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

    // 印出不換行的慢速文字
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
