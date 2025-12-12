package game;

import java.util.*;

/**
 * CloverPitAdvanced
 *
 * Advanced version of CloverPit-like console prototype
 *
 * - Turn-based (maxRounds adjustable)
 * - Each round has a debt requirement. If the player's money < debt at settlement, they die (unless they have insurance)
 * - Slot machine (three reels) symbols: CHERRY, BAR, SEVEN
 * - Item interface: passive/active, consumable/non-consumable
 * - Example Items: LuckyCharm (increases SEVEN chances for next spin),
 *                 Insurance (passive, saves the player once),
 *                 CoinMultiplier (stackable bonus),
 *                 ReSpin (active: re-spin immediately),
 *                 DebtReducer (active: reduce this round's debt)
 * - Action limits (actionsPerRound)
 *
 * Expandable: more items, events, stores, GUI
 */
public class CloverPit {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public int[] play() {
        Game game = new Game();
        return game.start();
    }

    /* ---------------- Game ---------------- */
    static class Game {
        final Scanner scanner = new Scanner(System.in);
        final Random rng = new Random();

        Player player;
        int maxRounds = 5;
        int currentRound = 1;
        int baseDebt = 30;         // Debt in the 1st round
        int actionsPerRound = 3;   // Maximum actions per round (to prevent infinite spins)
        int spinCost = 5;          // Cost per spin
        Symbol[] symbols = Symbol.values();

        // Judgement variables
        public int clear4 = 0;
        public int perfect4 = 0;
        public int achievement4 = 0;

        public int[] start() {
            welcome();
            player = new Player(60); // Starting money
            // Default items (demo purposes)
            player.addItem(new LuckyCharm());      // Increases SEVEN chances in next spin (consumable)
            player.addItem(new Insurance());       // Insurance (passive, saves once if debt isn't met)
            player.addItem(new CoinMultiplier());  // Stackable, increases payout
            player.addItem(new ReSpin());          // Re-spin item
            player.addItem(new DebtReducer());     // Active, reduces this round's debt

            loop();

            return new int[]{player.isAlive() ? 1 : 0, perfect4, achievement4};
        }

        void welcome() {
            System.out.println("=== CloverPit Advanced (Console) ===");
            System.out.println("Game Objective: Each round, you must earn enough money to pay off your debt.");
            System.out.println("You have items that can modify slot machine probabilities, increase payouts, or save your life. Good luck.\n");
        }

        void loop() {
            while (currentRound <= maxRounds) {
                System.out.println("\n--- Round " + currentRound + " ---");
                int debt = computeDebt(currentRound);
                System.out.println("This round's debt (required): " + debt);
                int actionsLeft = actionsPerRound;
                boolean roundOver = false;

                // Temporary debt adjustment for the round (e.g., modified by DebtReducer)
                int tempDebtReduction = 0;

                while (!roundOver && player.isAlive()) {
                    System.out.println("\nMoney: " + player.money + " | hp: " + player.hp + " | Actions left: " + actionsLeft);
                    System.out.println("Items: " + player.itemSummary());
                    System.out.println("1) Spin (cost " + spinCost + ")  2) Use Item  3) Inspect Status  4) End Round");
                    System.out.print("Choose (1-4): ");
                    String input = scanner.nextLine().trim();

                    switch (input) {
                        case "1":
                            if (actionsLeft <= 0) {
                                System.out.println("No actions left.");
                                break;
                            }
                            if (player.money < spinCost) {
                                System.out.println("Not enough money to spin.");
                                break;
                            }
                            player.money -= spinCost;
                            SpinResult result = spinOnce(player);
                            int payout = computePayout(result);
                            payout = player.applyPayoutModifiers(payout);
                            System.out.println("Spin result: " + result + " -> You win " + payout + " coins");
                            player.money += payout;
                            actionsLeft--;
                            // If LuckyCharm was used, clear its flag (design handled in LuckyCharm itself)
                            player.consumeNextSpinFlagsIfAny();
                            break;
                        case "2":
                            useItemMenu();
                            break;
                        case "3":
                            player.debugStatus();
                            break;
                        case "4":
                            roundOver = true;
                            break;
                        default:
                            System.out.println("Please input 1-4.");
                    }

                    // Early exit condition: if debt is covered
                    if (player.money >= (debt - tempDebtReduction)) {
                        System.out.println("\nYou have met the debt requirement (after temporary reductions)!");
                        roundOver = true;
                    }
                } // End of round

                // Round settlement
                if (player.money < computeDebt(currentRound)) {
                    // If insurance is available and not used in this round
                    if (player.hasUsableInsurance()) {
                        System.out.println("\nYou haven't met the debt, but insurance activates to save you. Insurance is consumed.");
                        player.consumeInsurance();
                        // Bring the money to the debt level (symbolic of taking a loan)
                        player.money = computeDebt(currentRound);
                    } else {
                        System.out.println("\nYou failed to meet the debt. A trap opens, and you fall into the abyss.");
                        player.hp = 0;
                    }
                } else {
                    System.out.println("Round " + currentRound + " debt cleared!");
                }

                if (!player.isAlive()) {
                    System.out.println("\n=== Game Over ===");
                    break;
                }

                // Potential events at the end of the round (demo)
                maybeTriggerEvent();

                currentRound++;
            } // rounds

            if (player.isAlive()) {
                System.out.println("\n=== Congratulations, you survived to the end of the game! ===");
                System.out.println("Final money: " + player.money + " | hp: " + player.hp);
            }
            System.out.println("\nThank you for playing CloverPit Advanced.");
        }

        int computeDebt(int round) {
            // Flexible debt increase (adjustable)
            return baseDebt + (round - 1) * 25; // 30,55,80,105,130 (example)
        }

        /* ---------- spin/slot mechanics ---------- */

        // Spin takes into account the player's nextSpinFlags (e.g., LuckyCharm)
        SpinResult spinOnce(Player player) {
            // Basic weights (higher means more likely)
            // base: CHERRY 60, BAR 30, SEVEN 10
            int wCherry = 60, wBar = 30, wSeven = 10;

            // Check if the player has nextSpin boosts (LuckyCharm) or other effects
            if (player.nextSpinHasLuckyCharm) {
                // If LuckyCharm is active, significantly increase SEVEN chances
                wSeven = 50; wCherry = 30; wBar = 20;
                System.out.println("[LuckyCharm] SEVEN chances greatly increased for the next spin!");
            }

            // If the player has passive items that adjust the weights (hook)
            WeightModifier wm = player.getPassiveWeightModifier();
            if (wm != null) {
                int[] arr = wm.modifyWeights(wCherry, wBar, wSeven);
                wCherry = arr[0]; wBar = arr[1]; wSeven = arr[2];
            }

            // Generate results for each reel based on weights
            Symbol a = weightedRandomSymbol(wCherry, wBar, wSeven);
            Symbol b = weightedRandomSymbol(wCherry, wBar, wSeven);
            Symbol c = weightedRandomSymbol(wCherry, wBar, wSeven);
            return new SpinResult(a, b, c);
        }

        Symbol weightedRandomSymbol(int wCherry, int wBar, int wSeven) {
            int total = wCherry + wBar + wSeven;
            int pick = rng.nextInt(total);
            if (pick < wCherry) return Symbol.CHERRY;
            pick -= wCherry;
            if (pick < wBar) return Symbol.BAR;
            return Symbol.SEVEN;
        }

        int computePayout(SpinResult r) {
            // Basic payout rules (adjustable)
            if (r.a == Symbol.SEVEN && r.b == Symbol.SEVEN && r.c == Symbol.SEVEN) {
                System.out.println("Three SEVENS! Big win!");
                return 150;
            } else if (r.a == Symbol.BAR && r.b == Symbol.BAR && r.c == Symbol.BAR) {
                System.out.println("Three BARs! Win!");
                return 80;
            } else if (r.a == Symbol.CHERRY && r.b == Symbol.CHERRY && r.c == Symbol.CHERRY) {
                System.out.println("Three CHERRIES! Small win!");
                return 40;
            } else if (r.a == r.b || r.b == r.c || r.a == r.c) {
                System.out.println("Two matching symbols! Small win");
                return 15;
            } else {
                System.out.println("No win...");
                return 0;
            }
        }

        /* ---------- Item usage ---------- */

        void useItemMenu() {
            if (player.items.isEmpty()) {
                System.out.println("No usable items.");
                return;
            }
            System.out.println("\nAvailable items:");
            for (int i = 0; i < player.items.size(); i++) {
                Item it = player.items.get(i);
                System.out.printf("%d) %s - %s\n", i + 1, it.name(), it.description());
            }
            System.out.println("Enter item number to use, or 0 to return:");
            String s = scanner.nextLine().trim();
            int pick;
            try { pick = Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("Invalid input."); return; }
            if (pick == 0) return;
            if (pick < 1 || pick > player.items.size()) { System.out.println("Invalid number."); return; }
            Item chosen = player.items.get(pick - 1);
            boolean used = chosen.use(player);
            if (used && chosen.consumable()) {
                player.items.remove(chosen);
                System.out.println(chosen.name() + " consumed and removed from inventory.");
            } else if (used) {
                System.out.println(chosen.name() + " activated (non-consumable).");
            } else {
                System.out.println(chosen.name() + " has no effect or cannot be used.");
            }
        }

        /* ---------- Events (example) ---------- */

        void maybeTriggerEvent() {
            // Simple random event: small chance to gain 10 coins, or lose 10 coins (demo)
            int roll = rng.nextInt(100);
            if (roll < 10) {
                System.out.println("[Event] You found an ancient coin, gaining 10 coins!");
                player.money += 10;
            } else if (roll >= 95) {
                System.out.println("[Event] You encountered bad luck, losing 10 coins...");
                player.money = Math.max(0, player.money - 10);
            }
        }
    }

    /* ---------------- Player & Items ---------------- */

    static class Player {
        int hp;
        int money;
        List<Item> items = new ArrayList<>();
        // Next-spin flags & stacks
        boolean nextSpinHasLuckyCharm = false;
        int coinMultiplierStacks = 0;

        Player(int startingMoney) {
            this.hp = 100;
            this.money = startingMoney;
        }

        boolean isAlive() { return hp > 0; }

        void addItem(Item it) { items.add(it); }

        String itemSummary() {
            if (items.isEmpty()) return "None";
            StringBuilder sb = new StringBuilder();
            for (Item i : items) sb.append(i.name()).append(", ");
            return sb.substring(0, sb.length() - 2);
        }

        // payout multiplier using coinMultiplierStacks
        int applyPayoutModifiers(int payout) {
            int res = payout;
            if (coinMultiplierStacks > 0 && res > 0) {
                // Each stack adds 40% payout (adjustable)
                for (int i = 0; i < coinMultiplierStacks; i++) {
                    res += res * 40 / 100;
                }
                System.out.println("[CoinMultiplier] Payout increased to: " + res);
            }
            return res;
        }

        // Passive weight modification hook (some items may return modifiers)
        WeightModifier getPassiveWeightModifier() {
            for (Item it : items) {
                if (it instanceof WeightModifierProvider) {
                    WeightModifierProvider p = (WeightModifierProvider) it;
                    WeightModifier wm = p.getWeightModifier();
                    if (wm != null) return wm;
                }
            }
            return null;
        }

        // Insurance check and consume
        boolean hasUsableInsurance() {
            for (Item it : items) if (it instanceof Insurance) return true;
            return false;
        }
        void consumeInsurance() {
            Iterator<Item> it = items.iterator();
            while (it.hasNext()) {
                Item i = it.next();
                if (i instanceof Insurance) {
                    it.remove();
                    System.out.println("[Insurance] Insurance consumed.");
                    return;
                }
            }
        }

        // nextSpin flag consumption: for LuckyCharm and others
        void consumeNextSpinFlagsIfAny() {
            if (nextSpinHasLuckyCharm) {
                nextSpinHasLuckyCharm = false; // Clear LuckyCharm flag (handled by LuckyCharm itself)
            }
        }

        void debugStatus() {
            System.out.println("\n-- Player Status --");
            System.out.println("hp: " + hp);
            System.out.println("money: " + money);
            System.out.println("CoinMultiplier stacks: " + coinMultiplierStacks);
            System.out.println("NextSpinLucky: " + nextSpinHasLuckyCharm);
            System.out.println("Items: " + itemSummary());
            System.out.println("----------------\n");
        }
    }

    /* ---------- Item Interface & Implementation ---------- */
    interface Item {
        String name();
        String description();
        /**
         * Use item (trigger active effects)
         * @param player Pass player to modify their state
         * @return true if item triggered/was effective (if consumable, Game will remove item)
         */
        boolean use(Player player);
        default boolean consumable() { return true; } // Default to consumable
    }

    // Some items provide passive weight modifications
    interface WeightModifierProvider {
        WeightModifier getWeightModifier();
    }

    // Weight modifier object (returns adjusted weights)
    static class WeightModifier {
        // modify weights: input (wCherry, wBar, wSeven) -> return adjusted {wCherry, wBar, wSeven}
        int[] modifyWeights(int wCherry, int wBar, int wSeven) {
            return new int[] {wCherry, wBar, wSeven};
        }
    }

    /* ---------- Example Items: LuckyCharm (Increases SEVEN chances for next spin) ---------- */
    static class LuckyCharm implements Item {
        boolean consumed = false;
        @Override public String name() { return "LuckyCharm"; }
        @Override public String description() { return "Consumable: Increases SEVEN chances for next spin."; }
        @Override
        public boolean use(Player player) {
            if (consumed) return false;
            consumed = true;
            player.nextSpinHasLuckyCharm = true;
            System.out.println("[Used LuckyCharm] Next spin has increased SEVEN chances.");
            return true;
        }
    }

    /* ---------- Insurance (Passive, Saves Once) ---------- */
    static class Insurance implements Item {
        @Override public String name() { return "Insurance"; }
        @Override public String description() { return "Passive: Saves you once when debt isn't met."; }
        @Override public boolean use(Player player) {
            System.out.println("Insurance is a passive item, cannot be used directly (activates automatically during settlement).");
            return false;
        }
        @Override public boolean consumable() { return true; } // Consumed when triggered
    }

    /* ---------- CoinMultiplier (Stackable Bonus Multiplier) ---------- */
    static class CoinMultiplier implements Item {
        @Override public String name() { return "CoinMultiplier"; }
        @Override public String description() { return "Active: Adds 1 stack to the payout multiplier (+40% per stack)."; }
        @Override public boolean use(Player player) {
            player.coinMultiplierStacks++;
            System.out.println("[CoinMultiplier] One stack added. Current multiplier: " + player.coinMultiplierStacks);
            return true;
        }
    }

    /* ---------- ReSpin (Active: Re-spin Once) ---------- */
    static class ReSpin implements Item {
        @Override public String name() { return "ReSpin"; }
        @Override public String description() { return "Active: Immediately performs a re-spin (non-consumable when infinite)."; }
        @Override public boolean use(Player player) {
            System.out.println("[ReSpin] Performing a free re-spin for you.");
            player.money += 10; // Representing a small win from re-spin
            return true;
        }
    }

    /* ---------- DebtReducer (Active: Reduce Debt for the Round) ---------- */
    static class DebtReducer implements Item {
        @Override public String name() { return "DebtReducer"; }
        @Override public String description() { return "Active: Reduces this round's debt by 20 coins (one-time)."; }
        @Override public boolean use(Player player) {
            System.out.println("[DebtReducer] You immediately gain 20 coins (effectively reducing your debt).");
            player.money += 20;
            return true;
        }
    }

    /* ---------- Symbol, SpinResult ---------- */
    enum Symbol { CHERRY, BAR, SEVEN }

    static class SpinResult {
        Symbol a, b, c;
        SpinResult(Symbol a, Symbol b, Symbol c) { this.a = a; this.b = b; this.c = c; }
        @Override
        public String toString() {
            return "[" + sym(a) + " | " + sym(b) + " | " + sym(c) + "]";
        }
        private String sym(Symbol s) {
            switch (s) {
                case CHERRY: return "CHERRY";
                case BAR: return "BAR";
                default: return "7";
            }
        }
    }
}
