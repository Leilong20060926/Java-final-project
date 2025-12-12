package game;

import java.util.*;

/**
 * CloverPitAdvanced
 *
 * 進階版 CloverPit-like console prototype
 *
 * - 回合制 (maxRounds 可調)
 * - 每回合有 debt (債務) 要求，若玩家結算時 money < debt 則死亡（除非保險）
 * - Slot machine (三軸) 符號: CHERRY, BAR, SEVEN
 * - Item 介面：被動/主動、消耗/非消耗皆可
 * - 範例 Items: LuckyCharm (提升 next spin 的 SEVEN 機率),
 *                 Insurance (被動保命一次),
 *                 CoinMultiplier (疊加獎金),
 *                 ReSpin (主動：重新拉一次),
 *                 DebtReducer (主動：降低本回合 debt)
 * - 行動次數限制 (actionsPerRound)
 *
 * 可擴充：更多道具、事件、商店、GUI
 */
public class CloverPit {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }

    /* ---------------- Game ---------------- */
    static class Game {
        final Scanner scanner = new Scanner(System.in);
        final Random rng = new Random();

        Player player;
        int maxRounds = 5;
        int currentRound = 1;
        int baseDebt = 30;         // 第1回合債務
        int actionsPerRound = 3;   // 每回合最多行動次數（避免一直 spin）
        int spinCost = 5;          // 每次拉霸消耗金錢
        Symbol[] symbols = Symbol.values();

        // Judgement variables
        public int clear4 = 0;
        public int perfect4 = 0;
        public int achievement4 = 0;

        public int[] play() {
            welcome();
            player = new Player(60); // 初始金錢
            // 預設道具（示範）
            player.addItem(new LuckyCharm());      // 提升下一次 spin 中 SEVEN 機率（消耗）
            player.addItem(new Insurance());       // 保險（被動，遇不到債務時保命一次）
            player.addItem(new CoinMultiplier());  // 堆疊型，增加 payout
            player.addItem(new ReSpin());          // 立刻重轉一次的道具
            player.addItem(new DebtReducer());     // 主動降低本回合債務

            loop();

            return new int[]{player.isAlive() ? 1 : 0, perfect4, achievement4};
        }

        void welcome() {
            System.out.println("=== CloverPit Advanced (Console) ===");
            System.out.println("玩法重點：每回合你必須掙到足夠金錢還債 (debt)。");
            System.out.println("你有道具可以改變拉霸機率、提升獎金或保命。祝你好運。\n");
        }

        void loop() {
            while (currentRound <= maxRounds) {
                System.out.println("\n--- 回合 " + currentRound + " ---");
                int debt = computeDebt(currentRound);
                System.out.println("本回合債務 (required): " + debt);
                int actionsLeft = actionsPerRound;
                boolean roundOver = false;

                // 本回合臨時可用修正（例如被 DebtReducer 改過）
                int tempDebtReduction = 0;

                while (!roundOver && player.isAlive()) {
                    System.out.println("\n金錢: " + player.money + " | hp: " + player.hp + " | 行動剩餘: " + actionsLeft);
                    System.out.println("道具: " + player.itemSummary());
                    System.out.println("1) Spin (cost " + spinCost + ")  2) Use Item  3) Inspect Status  4) End Round");
                    System.out.print("選擇 (1-4): ");
                    String input = scanner.nextLine().trim();

                    switch (input) {
                        case "1":
                            if (actionsLeft <= 0) {
                                System.out.println("行動次數已用完。");
                                break;
                            }
                            if (player.money < spinCost) {
                                System.out.println("金錢不足，無法拉霸。");
                                break;
                            }
                            player.money -= spinCost;
                            SpinResult result = spinOnce(player);
                            int payout = computePayout(result);
                            payout = player.applyPayoutModifiers(payout);
                            System.out.println("拉霸結果: " + result + " -> 獲得 " + payout + " 元");
                            player.money += payout;
                            actionsLeft--;
                            // 如果有使用 LuckyCharm 的標記，使用後清除（設計在 LuckyCharm 自身）
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
                            System.out.println("請輸入 1-4。");
                    }

                    // 早期結束條件：若已達成債務
                    if (player.money >= (debt - tempDebtReduction)) {
                        System.out.println("\n已達到本回合債務要求 (扣除臨時折抵)!");
                        roundOver = true;
                    }
                } // 回合內

                // 回合結算
                if (player.money < computeDebt(currentRound)) {
                    // 若有 Insurance，且尚未在本回合已使用
                    if (player.hasUsableInsurance()) {
                        System.out.println("\n你未達成債務，但保險自動啟動，避免死亡一次。保險被消耗。");
                        player.consumeInsurance();
                        // 將金錢拉到債務線 (象徵貸款)
                        player.money = computeDebt(currentRound);
                    } else {
                        System.out.println("\n你未能還清債務，陷阱打開，你掉進深淵。");
                        player.hp = 0;
                    }
                } else {
                    System.out.println("回合 " + currentRound + " 成功還債！");
                }

                if (!player.isAlive()) {
                    System.out.println("\n=== Game Over ===");
                    break;
                }

                // 每回合結束時，可能觸發簡單事件（示範）
                maybeTriggerEvent();

                currentRound++;
            } // rounds

            if (player.isAlive()) {
                System.out.println("\n=== 恭喜，你存活到關卡結束！ ===");
                System.out.println("最終金錢: " + player.money + " | hp: " + player.hp);
            }
            System.out.println("\n感謝遊玩 CloverPit Advanced.");
        }

        int computeDebt(int round) {
            // 稍微更有彈性的債務升幅（可微調）
            return baseDebt + (round - 1) * 25; // 30,55,80,105,130 (示例)
        }

        /* ---------- spin/slot 機制 ---------- */

        // spin 會考慮玩家的 nextSpinFlags (例如 LuckyCharm)
        SpinResult spinOnce(Player player) {
            // 基本權重（higher means more likely）
            // base: CHERRY 60, BAR 30, SEVEN 10
            int wCherry = 60, wBar = 30, wSeven = 10;

            // 檢查玩家是否有 nextSpin 增益 (LuckyCharm) 或其他 effect
            if (player.nextSpinHasLuckyCharm) {
                // 如果 LuckyCharm 啟動，明顯提高 SEVEN 機率
                wSeven = 50; wCherry = 30; wBar = 20;
                System.out.println("[LuckyCharm] 下一次拉霸 SEVEN 機率大幅提升！");
            }

            // 若玩家有被動道具可調整權重（hook）
            WeightModifier wm = player.getPassiveWeightModifier();
            if (wm != null) {
                int[] arr = wm.modifyWeights(wCherry, wBar, wSeven);
                wCherry = arr[0]; wBar = arr[1]; wSeven = arr[2];
            }

            // 依權重產生每軸結果
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
            // 基礎 payout 規則（可調）
            if (r.a == Symbol.SEVEN && r.b == Symbol.SEVEN && r.c == Symbol.SEVEN) {
                System.out.println("三個 7！大中獎！");
                return 150;
            } else if (r.a == Symbol.BAR && r.b == Symbol.BAR && r.c == Symbol.BAR) {
                System.out.println("三個 BAR！中獎！");
                return 80;
            } else if (r.a == Symbol.CHERRY && r.b == Symbol.CHERRY && r.c == Symbol.CHERRY) {
                System.out.println("三個 CHERRY！小中獎！");
                return 40;
            } else if (r.a == r.b || r.b == r.c || r.a == r.c) {
                System.out.println("兩個相同符號！小獎");
                return 15;
            } else {
                System.out.println("什麼也沒中……");
                return 0;
            }
        }

        /* ---------- item 使用 ---------- */

        void useItemMenu() {
            if (player.items.isEmpty()) {
                System.out.println("無可用道具。");
                return;
            }
            System.out.println("\n可用道具：");
            for (int i = 0; i < player.items.size(); i++) {
                Item it = player.items.get(i);
                System.out.printf("%d) %s - %s\n", i + 1, it.name(), it.description());
            }
            System.out.println("輸入道具編號使用，或輸入 0 返回：");
            String s = scanner.nextLine().trim();
            int pick;
            try { pick = Integer.parseInt(s); }
            catch (NumberFormatException e) { System.out.println("輸入錯誤。"); return; }
            if (pick == 0) return;
            if (pick < 1 || pick > player.items.size()) { System.out.println("編號錯誤。"); return; }
            Item chosen = player.items.get(pick - 1);
            boolean used = chosen.use(player);
            if (used && chosen.consumable()) {
                player.items.remove(chosen);
                System.out.println(chosen.name() + " 已消耗並從庫存移除。");
            } else if (used) {
                System.out.println(chosen.name() + " 已觸發 (非消耗型)。");
            } else {
                System.out.println(chosen.name() + " 無效果或無法使用。");
            }
        }

        /* ---------- Events (示例) ---------- */

        void maybeTriggerEvent() {
            // 簡單隨機事件：小機率獲得 10 元、或被扣 10 元（示範）
            int roll = rng.nextInt(100);
            if (roll < 10) {
                System.out.println("[事件] 你找到了一枚古幣，獲得 10 元！");
                player.money += 10;
            } else if (roll >= 95) {
                System.out.println("[事件] 你遭遇不幸，被偷走 10 元...");
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
            if (items.isEmpty()) return "無";
            StringBuilder sb = new StringBuilder();
            for (Item i : items) sb.append(i.name()).append(", ");
            return sb.substring(0, sb.length() - 2);
        }

        // payout multiplier 使用 coinMultiplierStacks
        int applyPayoutModifiers(int payout) {
            int res = payout;
            if (coinMultiplierStacks > 0 && res > 0) {
                // 每層 +40% 獎金（可調整）
                for (int i = 0; i < coinMultiplierStacks; i++) {
                    res += res * 40 / 100;
                }
                System.out.println("[CoinMultiplier] 獎金被提升至: " + res);
            }
            return res;
        }

        // 被動權重修正 hook（某些道具可能回傳 modifier）
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

        // Insurance 檢查與消耗
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
                    System.out.println("[Insurance] 保險被消耗。");
                    return;
                }
            }
        }

        // nextSpin flag consumption：用於 LuckyCharm 等
        void consumeNextSpinFlagsIfAny() {
            if (nextSpinHasLuckyCharm) {
                nextSpinHasLuckyCharm = false; // 清除 lucky 標記（LuckyCharm 被消耗時會自己處理）
            }
        }

        void debugStatus() {
            System.out.println("\n-- 玩家狀態 --");
            System.out.println("hp: " + hp);
            System.out.println("money: " + money);
            System.out.println("CoinMultiplier stacks: " + coinMultiplierStacks);
            System.out.println("NextSpinLucky: " + nextSpinHasLuckyCharm);
            System.out.println("道具: " + itemSummary());
            System.out.println("----------------\n");
        }
    }

    /* ---------- Item 介面與實作 ---------- */
    interface Item {
        String name();
        String description();
        /**
         * 使用道具（主動觸發）
         * @param player 傳入玩家以修改狀態
         * @return true 若道具觸發/生效（若為消耗型，Game 會移除該道具）
         */
        boolean use(Player player);
        default boolean consumable() { return true; } // 預設為消耗型
    }

    // 有些道具會提供被動權重修改
    interface WeightModifierProvider {
        WeightModifier getWeightModifier();
    }

    // 權重 modifier 物件（可回傳調整後的權重）
    static class WeightModifier {
        // modify weights: input (wCherry, wBar, wSeven) -> return adjusted {wCherry, wBar, wSeven}
        int[] modifyWeights(int wCherry, int wBar, int wSeven) {
            return new int[] {wCherry, wBar, wSeven};
        }
    }

    /* ---------- 範例道具：LuckyCharm（影響下一次 spin 的 SEVEN 機率） ---------- */
    static class LuckyCharm implements Item {
        boolean consumed = false;
        @Override public String name() { return "LuckyCharm"; }
        @Override public String description() { return "消耗型：下一次拉霸 SEVEN 機率大幅提升。"; }
        @Override
        public boolean use(Player player) {
            if (consumed) return false;
            consumed = true;
            player.nextSpinHasLuckyCharm = true;
            System.out.println("[使用 LuckyCharm] 下一次拉霸 SEVEN 機率將大幅提升。");
            return true;
        }
    }

    /* ---------- Insurance（被動保命一次） ---------- */
    static class Insurance implements Item {
        @Override public String name() { return "Insurance"; }
        @Override public String description() { return "被動保險：若回合結算未達債務，會自動消耗保險避免死亡。"; }
        @Override public boolean use(Player player) {
            System.out.println("Insurance 為被動道具，無法主動使用（在回合結算時自動觸發）。");
            return false;
        }
        @Override public boolean consumable() { return true; } // 在觸發時會被消耗
    }

    /* ---------- CoinMultiplier（疊加型獎金乘數） ---------- */
    static class CoinMultiplier implements Item {
        @Override public String name() { return "CoinMultiplier"; }
        @Override public String description() { return "主動：增加 1 層獎金乘數（每層+40% 獎金）。消耗型。"; }
        @Override public boolean use(Player player) {
            player.coinMultiplierStacks++;
            System.out.println("[CoinMultiplier] 增加一層乘數，目前層數：" + player.coinMultiplierStacks);
            return true;
        }
    }

    /* ---------- ReSpin（主動重轉一次） ---------- */
    static class ReSpin implements Item {
        @Override public String name() { return "ReSpin"; }
        @Override public String description() { return "主動：立刻執行一次免費 re-spin（非消耗時可改為無限）"; }
        @Override public boolean use(Player player) {
            // 由於本 prototype 的 spin 必須在 Game.spinOnce 執行，
            // 這裡只作為標記型道具，實際使用方式為：玩家按 use 之後 Game 可檢查並立刻呼叫 spinOnce，再移除此道具。
            System.out.println("[ReSpin] 系統會在下一次拉霸機會內執行一個免費重轉（實現簡化：直接給玩家 10 元代表重轉小獎）。");
            // 為避免耦合過深，這個 item 在 use 時給予玩家小額補償（示範）
            player.money += 10;
            return true;
        }
    }

    /* ---------- DebtReducer（降低本回合 debt） ---------- */
    static class DebtReducer implements Item {
        @Override public String name() { return "DebtReducer"; }
        @Override public String description() { return "主動：當回合使用可降低本回合債務 20 元（一次性）。"; }
        @Override public boolean use(Player player) {
            // 我們在 prototype 中無法直接改 Game 的 debt 計算（耦合），
            // 因此採取使玩家直接獲得等價金額（讓需求看似降低）
            System.out.println("[DebtReducer] 你立即獲得 20 元（等效於降低本回合債務）。");
            player.money += 20;
            return true;
        }
    }

    /* ---------- Symbol、SpinResult ---------- */
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
