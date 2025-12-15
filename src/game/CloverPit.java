package game;

import java.util.*;

public class CloverPit {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static void main(String[] args) {
        showRules();
        new CloverPit().start();
    }
    
    public int[] start() {
        Game game = new Game();
        game.play();
        return game.getResult();
    }

    /* =======================
       Typing Effect Utilities
       ======================= */
    static void typePrint(String text, int delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void typePrintln(String text, int delayMs) {
        typePrint(text, delayMs);
        System.out.println();
    }

    /* =======================
       Game Rules
       ======================= */
    static final String[] GAME_RULES = {
            "=== Welcome to CloverPit ===",
            "",
            "Game Objective:",
            "Survive and clear your debt across 5 rounds.",
            "If you clear all rounds with HP remaining, you win.",
            "",
            "Initial Status:",
            "- Money: 0",
            "- Debt: 20",
            "- HP: 100",
            "",
            "How a Spin Works:",
            "- Each spin shows 3 symbols.",
            "- Spinning costs NO money.",
            "- If all three symbols are different: HP -20.",
            "",
            "Slot Symbols & Chances:",
            "- Cherry (🍒): 35%",
            "- Grape (🍇): 25%",
            "- Seven (7️⃣): 16%",
            "- Question (❔): 16%",
            "- Diamond (💎): 8%",
            "",
            "Rewards (Three of a Kind):",
            "- Diamond 💎: +150",
            "- Seven 7️⃣: +100",
            "- Grape 🍇: +50",
            "- Cherry 🍒: +20",
            "",
            "Rewards (Two of a Kind):",
            "- Diamond 💎: +60",
            "- Seven 7️⃣: +30",
            "- Grape 🍇: +15",
            "- Cherry 🍒: +5",
            "",
            "Question Mark Effects:",
            "❔❔ grants ONE normal item (33% each):",
            "- Lucky Charm (Next spin: higher 7️⃣ chance)",
            "- Healing Potion (+30 HP)",
            "- Debt Contract (Debt -15)",
            "",
            "❔❔❔ grants ONE powerful item (33" +
                    "" + "% each):",
            "- Super Lucky Charm (Next spin: higher 💎 chance)",
            "- Super Healing Potion (Restore full HP)",
            "- Super Debt Contract (Debt -50)",
            "",
            "Items are one-time use.",
            "",
            "End of Round:",
            "- If money >= debt, debt is paid and next round begins.",
            "",
            "Win: Clear all 5 rounds with HP remaining.",
            "Lose: HP reaches 0."
    };

    static void showRules() {
        for (String line : GAME_RULES) {
            typePrintln(line, 25);
        }
        System.out.println();
        typePrintln("Press ENTER to start the game...", 25);
        scanner.nextLine();
    }

    /* =======================
       Game Core
       ======================= */
    static class Game {

        int round = 1;
        int maxRounds = 5;
        int debt = 20;
        Player player = new Player();

        void play() {
            typePrintln("Game Start!", 30);

            player.addItem(new LuckyCharm());
            player.addItem(new HealingPotion());

            while (round <= maxRounds && player.isAlive()) {

                typePrintln("\n--- Round " + round + " ---", 30);
                typePrintln("Current Debt: " + debt, 30);

                while (player.money < debt && player.isAlive()) {
                    showStatus();
                    typePrintln("1) Spin  2) Use Item  3) End Round", 20);
                    String input = scanner.nextLine().trim();

                    switch (input) {
                        case "1" -> spin();
                        case "2" -> useItem();
                        case "3" -> typePrintln("You cannot end the round yet.", 25);
                        default -> typePrintln("Invalid choice.", 25);
                    }
                }

                if (!player.isAlive()) break;

                typePrintln("Debt cleared!", 30);
                player.money -= debt;
                debt += 20;
                round++;
            }

            if (player.isAlive()) {
                typePrintln("YOU WIN! You survived all rounds.", 40);
            } else {
                typePrintln("GAME OVER...", 40);
            }
        }

        int[] getResult() {
            int won = player.isAlive() && round > maxRounds ? 1 : 0;
            int perfect = (won == 1 && player.hp == 100) ? 1 : 0;
            int achievement = (won == 1 && player.hp >= 80) ? 1 : 0;
            return new int[]{won, perfect, achievement};
        }

        void showStatus() {
            typePrintln("Money: " + player.money + " | HP: " + player.hp, 20);
        }

        void spin() {
            spinAnimation();

            Symbol a = Symbol.random(player);
            Symbol b = Symbol.random(player);
            Symbol c = Symbol.random(player);

            typePrintln("Result: " + a.icon + " | " + b.icon + " | " + c.icon, 35);

            handleResult(a, b, c);
        }

        void spinAnimation() {
            Symbol[] values = Symbol.values();
            for (int i = 0; i < 15; i++) {
                Symbol s1 = values[random.nextInt(values.length)];
                Symbol s2 = values[random.nextInt(values.length)];
                Symbol s3 = values[random.nextInt(values.length)];
                System.out.print("\rSpinning: " + s1.icon + " | " + s2.icon + " | " + s3.icon);
                try { Thread.sleep(100); } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.print("\r                              \r");
        }

        void handleResult(Symbol a, Symbol b, Symbol c) {
            if (a == b && b == c) {
                if (a == Symbol.QUESTION) {
                    giveItem(true);
                    return;
                }
                int reward = a.threeReward;
                player.money += reward;
                typePrintln("You gained $" + reward, 30);
            } else if (a == b || b == c || a == c) {
                if (a == Symbol.QUESTION || b == Symbol.QUESTION || c == Symbol.QUESTION) {
                    giveItem(false);
                    return;
                }
                Symbol match = (a == b || a == c) ? a : b;
                int reward = match.twoReward;
                player.money += reward;
                typePrintln("You gained $" + reward, 30);
            } else {
                player.hp -= 20;
                typePrintln("No reward. HP -20", 30);
            }
        }

        void giveItem(boolean superItem) {
            Item item;
            int r = random.nextInt(3);
            if (superItem) {
                item = switch (r) {
                    case 0 -> new SuperLuckyCharm();
                    case 1 -> new SuperHealingPotion();
                    default -> new SuperDebtContract();
                };
            } else {
                item = switch (r) {
                    case 0 -> new LuckyCharm();
                    case 1 -> new HealingPotion();
                    default -> new DebtContract();
                };
            }
            player.addItem(item);
            typePrintln("Item obtained: " + item.name(), 35);
        }

        void useItem() {
            if (player.items.isEmpty()) {
                typePrintln("No items available.", 25);
                return;
            }
            for (int i = 0; i < player.items.size(); i++) {
                System.out.println((i + 1) + ") " + player.items.get(i).name());
            }
            int choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice >= 0 && choice < player.items.size()) {
                player.items.remove(choice).use(player, this);
            }
        }
    }

    /* =======================
       Player & Items
       ======================= */
    static class Player {
        int money = 0;
        int hp = 100;
        boolean lucky7 = false;
        boolean luckyDiamond = false;
        List<Item> items = new ArrayList<>();

        boolean isAlive() { return hp > 0; }
        void addItem(Item i) { items.add(i); }
    }

    interface Item {
        String name();
        void use(Player p, Game g);
    }

    static class LuckyCharm implements Item {
        public String name() { return "Lucky Charm"; }
        public void use(Player p, Game g) { p.lucky7 = true; }
    }

    static class SuperLuckyCharm implements Item {
        public String name() { return "Super Lucky Charm"; }
        public void use(Player p, Game g) { p.luckyDiamond = true; }
    }

    static class HealingPotion implements Item {
        public String name() { return "Healing Potion"; }
        public void use(Player p, Game g) { p.hp = Math.min(100, p.hp + 30); }
    }

    static class SuperHealingPotion implements Item {
        public String name() { return "Super Healing Potion"; }
        public void use(Player p, Game g) { p.hp = 100; }
    }

    static class DebtContract implements Item {
        public String name() { return "Debt Contract"; }
        public void use(Player p, Game g) { g.debt = Math.max(0, g.debt - 15); }
    }

    static class SuperDebtContract implements Item {
        public String name() { return "Super Debt Contract"; }
        public void use(Player p, Game g) { g.debt = Math.max(0, g.debt - 50); }
    }

    /* =======================
       Slot Symbols
       ======================= */
    enum Symbol {
        CHERRY("🍒", 35, 20, 5),
        GRAPE("🍇", 25, 50, 15),
        SEVEN("7️⃣", 16, 100, 30),
        QUESTION("❔", 16, 0, 0),
        DIAMOND("💎", 8, 150, 60);

        final String icon;
        final int chance;
        final int threeReward;
        final int twoReward;

        Symbol(String icon, int chance, int three, int two) {
            this.icon = icon;
            this.chance = chance;
            this.threeReward = three;
            this.twoReward = two;
        }

        static Symbol random(Player p) {
            int roll = random.nextInt(100);
            int sum = 0;
            for (Symbol s : values()) {
                int weight = s.chance;
                if (p.lucky7 && s == SEVEN) weight += 20;
                if (p.luckyDiamond && s == DIAMOND) weight += 20;
                sum += weight;
                if (roll < sum) {
                    p.lucky7 = false;
                    p.luckyDiamond = false;
                    return s;
                }
            }
            return CHERRY;
        }
    }
}
