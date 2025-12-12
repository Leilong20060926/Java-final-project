package story.extension;

import java.util.Scanner;

public class StoryPrinter {

    // Set delay between characters (milliseconds)
    private static int textDelay = 35;

    // Set the scanner to capture user input
    private static Scanner scanner = new Scanner(System.in);
    
    // volatile 關鍵字確保在多個執行緒之間正確同步 'isSkipped' 的值
    private static volatile boolean isSkipped = false;

    public static void setTextDelay(int ms) {
        textDelay = ms;
    }

    // Print slow text with delay, but allow the user to skip using Enter
    public static void printSlowWithSkip(String text) {
        
        isSkipped = false; // 重置旗標
        
        // **注意：避免在 skipThread 中呼叫 scanner.hasNextLine()**
        // 啟動一個執行緒來等待使用者按下 Enter
        Thread skipThread = new Thread(() -> {
            // 這個 nextLine() 會阻塞直到使用者輸入一行（按 Enter）
            // 在您應用程式的環境中，這可以安全地等待輸入
            if (scanner.hasNextLine()) {
                scanner.nextLine();
                isSkipped = true; // 按下 Enter 後設定跳過旗標
            }
        });

        // 啟動執行緒
        skipThread.start();

        // 印出文本，檢查 'isSkipped' 旗標
        for (int i = 0; i < text.length(); i++) {
            
            // 檢查使用者是否按下 Enter 來跳過
            if (!isSkipped) { 
                System.out.print(text.charAt(i));
                try {
                    Thread.sleep(textDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                // 如果 isSkipped 為 true，跳出迴圈並印出其餘部分
                // 從目前字元 i 開始列印剩餘的字串
                System.out.print(text.substring(i));
                break;
            }
        }
        
        // 確保列印完成後，如果 skipThread 仍在運行（可能使用者在列印完成後才按下 Enter），
        // 並且尚未讀取輸入，則需要處理懸掛的輸入。
        // 然而，由於您已經讓 skipThread 成功讀取了 nextLine()，
        // 這裡通常不需要額外的處理，但為了乾淨，可以中斷它。
        if (skipThread.isAlive()) {
             skipThread.interrupt(); // 中斷等待輸入的執行緒
        }

        System.out.println(); // 加上換行符
    }

    // Print slow text without newline
    public static void printSlowRaw(String text) {
        // ... (保持不變) ...
    }
}
