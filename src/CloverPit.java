/**
 * 一、遊戲目標
 * 玩家必須在 5 回合內，每回合透過拉霸籌到足夠金錢並成功「清償負債」。
 * 回合結束前：
 * 若金錢 ≥ 負債 → 成功進入下一回合
 * 否則 → 繼續拉霸直到可償還或死亡。
 * 二、初始狀態
 * 初始金錢：30
 * 初始負債：50
 * 初始 HP：100
 * 拉霸費用：10 元
 * 初始道具：
 * 幸運符 / 回復藥水
 * 三、六種符號 & 出現機率
 * 符號 / 出現機率 / 說明
 * 櫻桃 CHERRY / 35% / 最普通
 * 葡萄 GRAPE / 20% / 中低值
 * 7 SEVEN / 15% / 高價值
 * 問號 MYSTERY / 15% / 特殊獎勵
 * 鑽石 DIAMOND / 10% / 高價值
 * 黃金 GOLD / 5% / 最高價值
 * 四、三個相同符號時獎金（3 of a kind）
 * 三個相同符號/獎金
 * GOLD/200
 * DIAMOND/150
 * SEVEN/100
 * GRAPE/50
 * CHERRY/20
 * 五、兩個相同符號時獎金（2 of a kind）
 * 兩個相同符號/獎金
 * GOLD/120
 * DIAMOND/60
 * SEVEN/30
 * GRAPE/15
 * CHERRY/5
 * 六、問號（?）特殊效果
 * 兩個問號 ? ? 隨機獲得（等機率 25%）：
 * 幸運符（下一次 Seven 機率上升）
 * 回復藥水（回復 30 HP）
 * 減債契約（負債 -15）
 * 一張免單券
 * 三個問號 ? ? ? 隨機獲得（等機率 25%）：
 * 超級幸運符（下一次 GOLD 機率上升）
 * 超級回復藥水（回滿 HP）
 * 超級減債契約（負債 -50）
 * 兩張免單券
 * 七、道具系統
 * 一般道具
 * 道具
 * 效果
 * 幸運符
 * 下一次拉霸提升 SEVEN 機率
 * 回復藥水
 * +30 HP
 * 減債契約
 * 當前負債 -15
 * 免單券
 * 下一次拉霸不花錢
 *
 * 高級道具（問號產生）
 * 道具
 * 效果
 * 超級幸運符
 * 下一次拉霸 GOLD 機率大幅提升
 * 超級回復藥水
 * HP 回滿
 * 超級減債契約
 * 當前負債 -50
 * 兩張免單券
 * 一次獲得 2 張免單券
 */

import java.util.*;

public class CloverPit {

    public static void main(String[] args) {
        Game game = new Game();
        GameAccessor.link(game);
        game.start();
    }

    static class Game {
        Scanner scanner = new Scanner(System.in);
        Random rng = new Random();

        Player player;
        int maxRounds = 5;
        int currentRound = 1;
        int debt = 50;
        int spinCost = 10;

        void start() {
            System.out.println("===  CloverPit  ===");
            System.out.println("目標：每回合拉霸還清負債，活到最後一回合！");

            player = new Player(30);

            // 初始道具：幸運符 + 回復藥水
            player.addItem(new LuckyCharm());
            player.addItem(new HealingPotion());

            while (currentRound <= maxRounds && player.isAlive()) {
                System.out.println("\n--- 回合 " + currentRound + " ---");
                System.out.println("本回合負債：" + debt);

                int actionsLeft = 3;
                boolean hasSpunThisRound = false;

                while (player.isAlive() && actionsLeft > 0) {
                    System.out.println("\n金錢:" + player.money + " | HP:" + player.hp + " | 行動次數剩餘:" + actionsLeft);
                    System.out.println("道具: " + player.itemSummary());
                    System.out.println("1) 拉霸  2) 使用道具  3) 查看狀態  4) 結束回合");
                    System.out.print("選擇: ");

                    String input = scanner.nextLine().trim();

                    switch (input) {
                        case "1":
                            if (!player.hasFreeSpin) {
                                if (player.money < spinCost) {
                                    System.out.println("金錢不足，無法拉霸！");
                                    break;
                                }
                                player.money -= spinCost;
                            } else {
                                System.out.println("使用免單券：本次拉霸不扣錢！");
                                player.hasFreeSpin = false;
                            }
                            hasSpunThisRound = true;
                            actionsLeft--;

                            showSpinAnimation();
                            SpinResult result = spin(player);
                            System.out.println("拉霸結果:" + result);
                            resolveSpin(result);
                            break;

                        case "2":
                            useItemMenu();
                            actionsLeft--;
                            break;

                        case "3":
                            player.showStatus();
                            break;

                        case "4":
                            if (!hasSpunThisRound) {
                                System.out.println("※ 必須至少拉霸一次才能結算回合");
                                break;
                            }
                            if (player.money >= debt) {
                                System.out.println("成功還清負債！");
                                player.money -= debt;
                                debt += 20;
                                actionsLeft = 0;
                                currentRound++;
                            } else {
                                System.out.println("金錢不足！扣 5 HP 並增加負債");
                                player.hp -= 5;
                                debt += 10;
                                if (!player.isAlive()) System.out.println("壓力過大死亡…");
                            }
                            break;

                        default:
                            System.out.println("請輸入 1-4");
                    }
                }
            }

            if (player.isAlive()) {
                System.out.println("恭喜！你活過所有回合！");
                System.out.println("最終金錢：" + player.money);
            } else System.out.println("Game Over…");
        }

        // 動畫
        void showSpinAnimation() {
            try {
                System.out.print("轉動中");
                for (int i = 0; i < 6; i++) {
                    Thread.sleep(500);
                    System.out.print(".");
                }
                System.out.println();
            } catch (InterruptedException ignored) {}
        }

        // 六符號 + 超級幸運
        Symbol randomSymbol(Player p) {
            int cherry = 35;
            int grape = 20;
            int seven = 15;
            int mystery = 15;
            int diamond = 10;
            int gold = 5;

            if (p.superLucky) gold = 40;
            if (p.nextSpinLucky) seven = 40;

            p.superLucky = false;
            p.nextSpinLucky = false;

            int total = cherry + grape + seven + mystery + diamond + gold;
            int r = rng.nextInt(total);

            if (r < cherry) return Symbol.CHERRY;
            r -= cherry;
            if (r < grape) return Symbol.GRAPE;
            r -= grape;
            if (r < seven) return Symbol.SEVEN;
            r -= seven;
            if (r < mystery) return Symbol.MYSTERY;
            r -= mystery;
            if (r < diamond) return Symbol.DIAMOND;
            return Symbol.GOLD;
        }

        // 執行一次拉霸
        SpinResult spin(Player p) {
            return new SpinResult(randomSymbol(p), randomSymbol(p), randomSymbol(p));
        }

        // 結果處理
        void resolveSpin(SpinResult r) {
            // 三個相同
            if (r.isTriple()) {
                switch (r.a) {
                    case GOLD -> prize(200);
                    case DIAMOND -> prize(150);
                    case SEVEN -> prize(100);
                    case GRAPE -> prize(50);
                    case CHERRY -> prize(20);
                    case MYSTERY -> giveMysteryReward(true);
                }
                return;
            }

            // 兩個相同
            if (r.isDouble()) {
                switch (r.getDoubleSymbol()) {
                    case GOLD -> prize(120);
                    case DIAMOND -> prize(60);
                    case SEVEN -> prize(30);
                    case GRAPE -> prize(15);
                    case CHERRY -> prize(5);
                    case MYSTERY -> giveMysteryReward(false);
                }
                return;
            }

            System.out.println("未中獎！HP -10");
            player.hp -= 10;
        }

        void prize(int amount) {
            System.out.println("獲得 " + amount + " 元！");
            player.money += amount;
        }

        // 問號獎勵
        void giveMysteryReward(boolean big) {
            System.out.println(big ? "★★★ 三問號大獎！★★★" : "★ 兩問號獎勵！ ★");
            int pick = rng.nextInt(4);

            if (!big) {
                switch (pick) {
                    case 0 -> player.addItem(new LuckyCharm());
                    case 1 -> player.addItem(new HealingPotion());
                    case 2 -> player.addItem(new DebtReducer());
                    case 3 -> player.addItem(new FreeSpinTicket());
                }
            } else {
                switch (pick) {
                    case 0 -> player.addItem(new SuperLuckyCharm());
                    case 1 -> player.addItem(new SuperHealingPotion());
                    case 2 -> player.addItem(new SuperDebtReducer());
                    case 3 -> { player.addItem(new FreeSpinTicket()); player.addItem(new FreeSpinTicket()); }
                }
            }
        }

        // 道具選單
        void useItemMenu() {
            if (player.items.isEmpty()) {
                System.out.println("沒有道具！"); return;
            }

            System.out.println("可用道具：");
            for (int i = 0; i < player.items.size(); i++)
                System.out.println((i+1) + ") " + player.items.get(i).name() + " - " + player.items.get(i).description());

            System.out.print("輸入編號 (0返回)：");
            String s = scanner.nextLine().trim();
            int pick;
            try { pick = Integer.parseInt(s); } catch (Exception e) { return; }

            if (pick <= 0 || pick > player.items.size()) return;
            Item it = player.items.get(pick-1);
            if (it.use(player) && it.consumable()) player.items.remove(it);
        }
    }

    // 玩家
    static class Player {
        int money;
        int hp = 100;

        boolean nextSpinLucky = false;
        boolean superLucky = false;
        boolean hasFreeSpin = false;

        List<Item> items = new ArrayList<>();

        Player(int money) { this.money = money; }

        boolean isAlive() { return hp > 0; }
        void addItem(Item i) { items.add(i); System.out.println("獲得道具：" + i.name()); }

        String itemSummary() {
            if (items.isEmpty()) return "無";
            StringBuilder sb = new StringBuilder();
            for (Item i : items) sb.append(i.name()).append(" ");
            return sb.toString();
        }

        void showStatus() {
            System.out.println("金錢:" + money + " HP:" + hp);
            System.out.println("道具:" + itemSummary());
        }
    }

    // Item system
    interface Item {
        String name();
        String description();
        boolean use(Player p);
        default boolean consumable() { return true; }
    }

    // === 基本道具 ===
    static class LuckyCharm implements Item {
        public String name() { return "幸運符"; }
        public String description() { return "下一次拉霸提高 SEVEN 機率"; }
        public boolean use(Player p) {
            p.nextSpinLucky = true;
            System.out.println("下一次拉霸更容易出 SEVEN！");
            return true;
        }
    }

    static class HealingPotion implements Item {
        public String name() { return "回復藥水"; }
        public String description() { return "恢復 30 HP"; }
        public boolean use(Player p) {
            p.hp = Math.min(100, p.hp + 30);
            System.out.println("HP +30！");
            return true;
        }
    }

    static class DebtReducer implements Item {
        public String name() { return "減債契約"; }
        public String description() { return "立刻減少負債 15"; }
        public boolean use(Player p) {
            GameAccessor.reduceDebt(15);
            return true;
        }
    }

    static class FreeSpinTicket implements Item {
        public String name() { return "免單券"; }
        public String description() { return "下一次拉霸免費"; }
        public boolean use(Player p) {
            p.hasFreeSpin = true;
            System.out.println("下一次拉霸不扣錢！");
            return true;
        }
    }

    // === 超級道具 ===
    static class SuperLuckyCharm implements Item {
        public String name() { return "超級幸運符"; }
        public String description() { return "大幅提升黃金機率"; }
        public boolean use(Player p) {
            p.superLucky = true;
            System.out.println("下一次拉霸超高機率 GOLD！");
            return true;
        }
    }

    static class SuperHealingPotion implements Item {
        public String name() { return "超級回復藥水"; }
        public String description() { return "HP 全滿"; }
        public boolean use(Player p) {
            p.hp = 100;
            System.out.println("HP 完全恢復！");
            return true;
        }
    }

    static class SuperDebtReducer implements Item {
        public String name() { return "超級減債契約"; }
        public String description() { return "債務 -50"; }
        public boolean use(Player p) {
            GameAccessor.reduceDebt(50);
            return true;
        }
    }

    // GameAccessor
    static class GameAccessor {
        private static Game linked;
        static void link(Game g) { linked = g; }
        static void reduceDebt(int amount) {
            if (linked != null) {
                linked.debt = Math.max(0, linked.debt - amount);
                System.out.println("當前負債：" + linked.debt);
            }
        }
    }

    enum Symbol { CHERRY, GRAPE, SEVEN, MYSTERY, DIAMOND, GOLD }

    static class SpinResult {
        Symbol a, b, c;
        SpinResult(Symbol a, Symbol b, Symbol c) { this.a=a; this.b=b; this.c=c; }
        public String toString() { return "["+a+"|"+b+"|"+c+"]"; }
        boolean isTriple() { return a==b && b==c; }
        boolean isDouble() { return a==b || b==c || a==c; }
        Symbol getDoubleSymbol() {
            if (a==b || a==c) return a;
            return b;
        }
    }
}