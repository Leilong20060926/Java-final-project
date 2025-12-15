package game.extension;
import java.util.Scanner;




public class ruleDisplay {

    public static void showIntro(Scanner scanner) {
        GamePrinter.printSlow("\n========================================");
        GamePrinter.printSlow("      FAKE BUCKETSHOT ROULETTE      ");
        GamePrinter.printSlow("========================================");
        
        System.out.println("Do you want to read the rules? (y/n)");
        String input = scanner.next();
        
        // Consume the newline character to prevent skipping later inputs
        if (scanner.hasNextLine()) scanner.nextLine(); 

        if (!input.equalsIgnoreCase("y")) {
            GamePrinter.printSlow("Good luck...");
            return;
        }

        printSectionHeader("Basic Rules");
        GamePrinter.printSlow("1. You are entering a game of roulette against the Dealer.");
        GamePrinter.printSlow("2. The shotgun is loaded with a random number of 'Live' and 'Blank' shells.");
        GamePrinter.printSlow("3. Live shells deal 1 damage; Blanks deal 0 damage.");
        GamePrinter.printSlow("4. Both sides start with the same health. The first to reach 0 dies.");
        
        waitForEnter(scanner);

        printSectionHeader("Turn Actions");
        GamePrinter.printSlow("On your turn, you can choose:");
        GamePrinter.printSlow("【Shoot Dealer】");
        GamePrinter.printSlow("   - Live: Dealer takes damage. Turn ends.");
        GamePrinter.printSlow("   - Blank: Nothing happens. Turn ends.");
        GamePrinter.printSlow("【Shoot Yourself】 (High Risk, High Reward)");
        GamePrinter.printSlow("   - Live: You take damage. Turn ends.");
        GamePrinter.printSlow("   - Blank: Nothing happens. **You keep your turn (Extra Turn)**!");
        
        waitForEnter(scanner);

        printSectionHeader("Items");
        GamePrinter.printSlow("You receive random items when the shotgun is reloaded:");
        GamePrinter.printSlow("Beer: Ejects the current shell from the shotgun (Live or Blank).");
        GamePrinter.printSlow("Cigarettes: Regains 1 health point.");
        GamePrinter.printSlow("Magnifying Glass: Lets you see if the current shell is Live or Blank.");
        GamePrinter.printSlow("Handcuffs: Skips the Dealer's next turn.");
        GamePrinter.printSlow("Saw: Saws off the barrel. The next Live shell deals 2 damage.");
        waitForEnter(scanner);

        GamePrinter.printSlow("\nRules explained. Waiver signed. Let's begin...");
        GamePrinter.printSlow("----------------------------------------\n");
    }

    // Helper: Print section headers
    private static void printSectionHeader(String title) {
        System.out.println("\n--- [" + title + "] ---");
    }

    // Helper: Pause until Enter is pressed
    private static void waitForEnter(Scanner scanner) {
        System.out.println("\n[Press Enter to continue...]");
        scanner.nextLine();
    }
}