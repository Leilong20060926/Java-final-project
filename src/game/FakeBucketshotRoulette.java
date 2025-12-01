package game;

import java.util.Scanner;

import game.extension.GamePrinter;

import java.util.Random;
import java.util.ArrayList; // more flexible than arrays
import java.util.List;
import java.util.Collections;

public class FakeBucketshotRoulette {
    // Initial health
    private static final int MAX_HEALTH = 3;
    private List<String> shotgun; // shotgun (cylinder)
    private int playerHealth;
    private int dealerHealth;
    private Boolean playerTurn; // flag for player's turn: true=player, false=dealer
    private Random random;
    private Scanner scanner;
    private double dangerLevel = 0.3; // danger level; affects proportion of live shells
    private double atleastDangerLevel = 0.2; // minimum danger level

    // Constructor
    public FakeBucketshotRoulette() {
        shotgun = new ArrayList<>();
        playerHealth = MAX_HEALTH;
        dealerHealth = MAX_HEALTH;
        playerTurn = true; // player starts first
        random = new Random();
        scanner = new Scanner(System.in);

    }

    // Initialize the shotgun
    // Includes reload functionality
    // Danger level logic: starts at 0.2 and increases by 0.1 on each reload up to 1.0
    private void loadShotgun(boolean increaseDanger) {
        if (increaseDanger) {
            dangerLevel += 0.1;
            atleastDangerLevel += 0.1;
            if (atleastDangerLevel > 1.0)
                atleastDangerLevel = 1.0;
            if (dangerLevel > 1.0)
                dangerLevel = 1.0;
        }

        shotgun.clear();

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

        GamePrinter.printSlow("Shotgun loaded: total " + totalShells +
            " shells, live shells " + liveShells +
            " (current max ratio: " + (int) (dangerLevel * 100) + "%)");
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
        GamePrinter.printSlow("Remaining live shells: " + shotgun.stream().filter(shell -> shell.equals("live")).count());
    }

    // Player logic
    // includes reload behavior when live shells are spent
    private void PlayAction() {
        displayStatus();
        GamePrinter.printSlow("Your turn — choose an action");
        GamePrinter.printSlow("1. Shoot yourself");
        GamePrinter.printSlow("2. Shoot dealer");
        int choice = scanner.nextInt();
        boolean shootSelf = (choice == 1);

        String shell = shotgun.remove(0); // take the first shell
        GamePrinter.printSlow("Pulling the trigger..." + (shell.equals("live") ? " Live round!" : " Blank"));
        if (shootSelf) {
            if (shell.equals("live")) {
                playerHealth--;
                GamePrinter.printSlow("You hit yourself! -1 health");
                playerTurn = false;
            } else {
                GamePrinter.printSlow("Lucky! You keep your turn");
                playerTurn = true;
            }
        } else {
            if (shell.equals("live")) {
                dealerHealth--;
                GamePrinter.printSlow("You hit the dealer! Dealer -1 health");
                playerTurn = false;
            } else {
                GamePrinter.printSlow("Dealer avoided damage, dealer's turn");
                playerTurn = false;
            }
        }
    }

    // Dealer logic
    private void dealerAction() {
        displayStatus();

        int liveShells = (int) shotgun.stream().filter(shell -> shell.equals("live")).count();
        boolean shootSelf = (double) liveShells / shotgun.size() < 0.5;
        String shell = shotgun.remove(0);

        if (shootSelf) {
                GamePrinter.printSlow("Dealer chooses to shoot themselves. Pulling the trigger..." + (shell.equals("live") ? " Live round!" : " Blank"));
            if (shell.equals("live")) {
                dealerHealth--;
                    GamePrinter.printSlow("Dealer hit themselves! Dealer -1 health");
                playerTurn = true;
            } else {
                    GamePrinter.printSlow("Dealer continues their turn");
                playerTurn = false;
            }
        } else {
                GamePrinter.printSlow("Dealer chooses to shoot you. Pulling the trigger..." + (shell.equals("live") ? " Live round!" : " Blank"));
            if (shell.equals("live")) {
                playerHealth--;
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