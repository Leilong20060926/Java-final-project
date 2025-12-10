package game;

import java.util.Scanner;
import game.extension.GamePrinter;
import game.extension.Item;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FakeBucketshotRoulette {
    // Initial health
    private static final int MAX_HEALTH = 8;
    private static final int MAX_ITEMS = 8; // Max inventory size
    private List<String> shotgun; // shotgun (cylinder)
    private int playerHealth;
    private int dealerHealth;
    private Boolean playerTurn; // flag for player's turn: true=player, false=dealer
    private Random random;
    private Scanner scanner;
    private double dangerLevel = 0.3; // danger level; affects proportion of live shells
    private double atleastDangerLevel = 0.2; // minimum danger level

    // Items & Status Effects
    private List<Item> playerItems;
    private int currentDamage = 1; // Default damage (Saw changes this to 2)
    private boolean dealerHandcuffed = false; // Is the dealer skipped?

    // Constructor
    public FakeBucketshotRoulette() {
        shotgun = new ArrayList<>();
        playerItems = new ArrayList<>(); // Initialize the item list
        playerHealth = MAX_HEALTH;
        dealerHealth = MAX_HEALTH;
        playerTurn = true; // player starts first
        random = new Random();
        scanner = new Scanner(System.in);
    }

    // Initialize the shotgun
    private void loadShotgun(boolean increaseDanger) {
        if (increaseDanger) {
            dangerLevel = Math.min(1.0, dangerLevel + 0.1);
            atleastDangerLevel = Math.min(1.0, atleastDangerLevel + 0.1);
        }

        shotgun.clear();
        grantItems(); // Grant items on reload

        int totalShells = random.nextInt(7) + 2;
        int maxLive = (int) Math.floor(totalShells * dangerLevel);
        int minLive = (int) Math.floor(totalShells * atleastDangerLevel);
        int liveShells = 1;

        if (maxLive > 1) {
            liveShells = random.nextInt(maxLive - minLive + 1) + minLive;
        }

        for (int i = 0; i < liveShells; i++)
            shotgun.add("live");
        for (int i = 0; i < totalShells - liveShells; i++)
            shotgun.add("blank");

        Collections.shuffle(shotgun);

        GamePrinter.printSlow("\n=== The Dealer reloads the shotgun ===");
        GamePrinter.printSlow("Shotgun loaded: total " + totalShells +
                " shells, live shells " + liveShells +
                " (current max ratio: " + (int) (dangerLevel * 100) + "%)");

        // Reset per-round statuses
        currentDamage = 1;
        dealerHandcuffed = false;
    }

    private void grantItems() {
        int itemsToGive = random.nextInt(3) + 2; // Give 2-4 items per reload
        for (int i = 0; i < itemsToGive; i++) {
            if (playerItems.size() >= MAX_ITEMS)
                break;

            // Pick a random item
            Item[] allItems = Item.values();
            playerItems.add(allItems[random.nextInt(allItems.length)]);
        }
        GamePrinter.printSlow("You received some items...");
    }

    private void displayStatus() {
        // check whether reload is needed first

        boolean noLive = shotgun.stream().noneMatch(shell -> shell.equals("live"));

        boolean empty = shotgun.isEmpty();

        if (noLive || empty) {
            GamePrinter.printSlow("All live shells are spent, reloading...");
            loadShotgun(true);
        }
        GamePrinter.printSlow("\n-- Current Status --");
        GamePrinter.printSlow("Player Health: " + playerHealth);
        GamePrinter.printSlow("Dealer Health: " + dealerHealth);
        GamePrinter.printSlow("Remaining shells: " + shotgun.size());
        GamePrinter
                .printSlow("Remaining live shells: " + shotgun.stream().filter(shell -> shell.equals("live")).count());
        if (dealerHandcuffed)
            GamePrinter.printSlow("[STATUS] Dealer is HANDCUFFED (Will skip turn)");
    }

    // Player logic
    private void PlayAction() {
        // Use a loop to allow item usage before shooting
        while (true) {
            displayStatus();
            GamePrinter.printSlow("Your turn — choose an action");
            GamePrinter.printSlow("1. Shoot yourself");
            GamePrinter.printSlow("2. Shoot dealer");
            GamePrinter.printSlow("3. Use Item (Inventory: " + playerItems.size() + ")"); // Added Item Menu

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // consume invalid input
            }

            if (choice == 3) {
                // Item Logic integrated here
                if (playerItems.isEmpty()) {
                    GamePrinter.printSlow("Inventory is empty!");
                    continue;
                }

                System.out.println("Select item (0 to cancel):");
                for (int i = 0; i < playerItems.size(); i++) {
                    System.out.println((i + 1) + ". " + playerItems.get(i).getName());
                }
                // Read as token so non-integer input won't throw InputMismatchException
                String selection = scanner.next().trim();
                if (selection.equals("0")) {
                    continue; // cancel
                }

                int itemIdx = -1;
                try {
                    itemIdx = Integer.parseInt(selection) - 1;
                } catch (NumberFormatException nfe) {
                    // allow selection by enum name or item name or single-letter shortcut
                    // (case-insensitive)
                    String selUpper = selection.toUpperCase();
                    for (int i = 0; i < playerItems.size(); i++) {
                        Item it = playerItems.get(i);
                        String enumName = it.name(); // e.g., MAGNIFYING_GLASS
                        String displayName = it.getName(); // e.g., Magnifying Glass
                        if (enumName.equalsIgnoreCase(selection) || displayName.equalsIgnoreCase(selection)) {
                            itemIdx = i;
                            break;
                        }
                        // match by first letter(s) of display name or enum name (e.g., 'M' ->
                        // Magnifying Glass)
                        if (!selection.isEmpty()) {
                            if (enumName.startsWith(selUpper) || displayName.toUpperCase().startsWith(selUpper)) {
                                itemIdx = i;
                                break;
                            }
                        }
                    }
                }

                if (itemIdx >= 0 && itemIdx < playerItems.size()) {
                    Item item = playerItems.remove(itemIdx);
                    GamePrinter.printSlow("Used: " + item.getName());

                    // Simple switch for item effects
                    switch (item) {
                        case BEER:
                            if (!shotgun.isEmpty())
                                GamePrinter.printSlow("Ejected: " + shotgun.remove(0));
                            break;
                        case CIGARETTES:
                            if (playerHealth < MAX_HEALTH)
                                playerHealth++;
                            GamePrinter.printSlow("Health +1");
                            break;
                        case MAGNIFYING_GLASS:
                            if (!shotgun.isEmpty())
                                GamePrinter.printSlow("Next shell is: " + shotgun.get(0));
                            break;
                        case HANDCUFFS:
                            dealerHandcuffed = true;
                            GamePrinter.printSlow("Dealer cuffed.");
                            break;
                        case SAW:
                            currentDamage = 2;
                            GamePrinter.printSlow("Saw used. Damage x2.");
                            break;
                    }
                }
                continue; // Go back to menu after using item
            }

            // SHOOTING LOGIC
            boolean shootSelf = (choice == 1);
            boolean validShoot = (choice == 1 || choice == 2);

            if (!validShoot) {
                GamePrinter.printSlow("Invalid input.");
                continue;
            }

            if (shotgun.isEmpty())
                return; // Safety check

            String shell = shotgun.remove(0); // take the first shell
            GamePrinter.printSlow("Pulling the trigger..." + (shell.equals("live") ? " Live round!" : " Blank"));

            if (shootSelf) {
                if (shell.equals("live")) {
                    playerHealth -= currentDamage; // Apply currentDamage
                    GamePrinter.printSlow("You hit yourself! -" + currentDamage + " health");
                    currentDamage = 1; // Reset damage
                    playerTurn = false;
                    return; // End turn
                } else {
                    GamePrinter.printSlow("Lucky! You keep your turn");
                    playerTurn = true;
                    return; // Keep turn
                }
            } else {
                if (shell.equals("live")) {
                    dealerHealth -= currentDamage; // Apply currentDamage
                    GamePrinter.printSlow("You hit the dealer! Dealer -" + currentDamage + " health");
                } else {
                    GamePrinter.printSlow("Dealer avoided damage (Blank)");
                }
                currentDamage = 1; // Reset damage
                playerTurn = false;
                return; // End turn
            }
        }
    }

    // Dealer logic
    private void dealerAction() {
        if (dealerHandcuffed) {
            GamePrinter.printSlow("Dealer struggles with handcuffs... Turn Skipped!");
            dealerHandcuffed = false;
            playerTurn = true;
            return;
        }

        displayStatus();
        GamePrinter.printSlow(">>> Dealer's Turn");

        // Simple Dealer AI (No items used to keep logic simple as requested)
        int liveShells = (int) shotgun.stream().filter(shell -> shell.equals("live")).count();
        boolean shootSelf = (double) liveShells / shotgun.size() < 0.5;

        if (shotgun.isEmpty())
            return;

        String shell = shotgun.remove(0);

        if (shootSelf) {
            GamePrinter.printSlow("Dealer chooses to shoot themselves. Pulling the trigger..."
                    + (shell.equals("live") ? " Live round!" : " Blank"));
            if (shell.equals("live")) {
                dealerHealth--; // Dealer takes normal damage (1)
                GamePrinter.printSlow("Dealer hit themselves! Dealer -1 health");
                playerTurn = true;
            } else {
                GamePrinter.printSlow("Dealer continues their turn");
                playerTurn = false;
            }
        } else {
            GamePrinter.printSlow("Dealer chooses to shoot you. Pulling the trigger..."
                    + (shell.equals("live") ? " Live round!" : " Blank"));
            if (shell.equals("live")) {
                playerHealth--; // Dealer deals normal damage (1)
                GamePrinter.printSlow("Dealer hit you! -1 health");
                playerTurn = true;
            } else {
                GamePrinter.printSlow("You were lucky, it's your turn");
                playerTurn = true;
            }
        }
    }

    // Game over check
    private void isGameOver() {
        if (playerHealth <= 0) {
            GamePrinter.printSlow("You have died. Game over!");
            System.exit(0);
        } else if (dealerHealth <= 0) {
            GamePrinter.printSlow("Dealer has died. You win!");
            System.exit(0);
        }
    }

    // Main game loop
    public void play() {
        loadShotgun(false); // initial load
        GamePrinter.printSlow("Game start! Player goes first.");

        while (true) {
            if (playerTurn) {
                PlayAction();
            } else {
                dealerAction();
            }
            isGameOver();
        }
    }

    public static void main(String[] args) {
        FakeBucketshotRoulette game = new FakeBucketshotRoulette();
        game.play(); // start game
    }
}