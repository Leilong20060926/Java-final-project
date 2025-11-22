package game;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
public class FakeBucketshotRoulette {
    // 初始生命
    private static final int MAX_HEALTH = 3;
    private List<String> shotgun; // 子彈筒
    private int playerHealth;
    private int dealerHealth;
    private Boolean playerTurn; // 玩家回合標誌true: 玩家回合, false: 莊家回合
    private Random random;
    private Scanner scanner;
    // 建構子
    public FakeBucketshotRoulette(){
        shotgun = new ArrayList<>();
        playerHealth = MAX_HEALTH;
        dealerHealth = MAX_HEALTH;
        playerTurn = true; // 玩家先手
        random = new Random();
        scanner = new Scanner(System.in);
    }
    //初始化建構子
    // 加上重新裝填子彈功能
    private void loadShotgun(boolean increaseDanger) {
        shotgun.clear();
        int totalShells = random.nextInt(7) + 2; // 2-8發子彈
        int liveShells;
        if (increaseDanger) {
            // 危險模式：實彈比例增加，例如至少一半是實彈
            liveShells = totalShells / 2 + random.nextInt(totalShells / 2 + 1);
        } else {
            liveShells = random.nextInt(totalShells) + 1; // 至少1發實彈
        }

        for (int i = 0; i < liveShells; i++) {
            shotgun.add("live");
        }
        for (int i = 0; i < totalShells - liveShells; i++) {
            shotgun.add("blank");
        }
        Collections.shuffle(shotgun);
        System.out.println("子彈筒已裝填，共有 " + totalShells + " 發子彈，其中 " + liveShells + " 發為實彈。");
    }
    private void displayStatus() {
        System.out.println("\n-- 目前狀態 --");
        System.out.println("玩家生命值: " + playerHealth);
        System.out.println("莊家生命值: " + dealerHealth);
        System.out.println("剩餘子彈數: " + shotgun.size());
        } 
    //玩家邏輯
    //加上打完填裝危險子彈
   private void PlayAction() {
    displayStatus();
    System.out.println("你的回合，選擇行動");
    System.out.println("1. 射擊自己");
    System.out.println("2. 射擊莊家");
    int choice = scanner.nextInt();
    boolean shootSelf = (choice == 1);

    // 如果子彈筒沒子彈，重新裝填
    if (shotgun.isEmpty()) {
        System.out.println("子彈打完了，重新裝填...");
        loadShotgun(true); // 重新裝填危險模式
    }

    String shell = shotgun.remove(0); // 取第一顆子彈
    System.out.println("扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
    if (shootSelf) {
        if (shell.equals("live")) {
            playerHealth--;
            System.out.println("你中了自己的子彈，生命值減少1！");
            playerTurn = false;
        } else {
            System.out.println("幸運！繼續你的回合");
            playerTurn = true;
        }
    } else {
        if (shell.equals("live")) {
            dealerHealth--;
            System.out.println("你射中了莊家，莊家生命值減少1！");
            playerTurn = true;
        } else {
            System.out.println("莊家躲過一劫，輪到莊家回合");
            playerTurn = false;
        }
    }
}
    //莊家邏輯
    //加上打完填裝危險子彈
    private void dealerAction() {
    displayStatus();

    // 如果子彈筒沒子彈，重新裝填
    if (shotgun.isEmpty()) {
        System.out.println("子彈打完了，重新裝填...");
        loadShotgun(true);
    }

    int liveShells = (int) shotgun.stream().filter(shell -> shell.equals("live")).count();
    boolean shootSelf = (double) liveShells / shotgun.size() < 0.5;
    String shell = shotgun.remove(0);

    if (shootSelf) {
        System.out.println("莊家選擇射擊自己。扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
        if (shell.equals("live")) {
            dealerHealth--;
            System.out.println("莊家中了自己的子彈，生命值減少1！");
            playerTurn = true;
        } else {
            System.out.println("莊家繼續他的回合");
            playerTurn = false;
        }
    } else {
        System.out.println("莊家選擇射擊你。扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
        if (shell.equals("live")) {
            playerHealth--;
            System.out.println("莊家射中了你，你的生命值減少1！");
            playerTurn = false;
        } else {
            System.out.println("你幸運地躲過一劫，輪到你的回合");
            playerTurn = true;
            }
        }
    }
    //遊戲結束判定
    private void isGameOver() {
        if (playerHealth <= 0) {
            System.out.println("你已經死亡，遊戲結束！");
            System.exit(0);
        } else if (dealerHealth <= 0) {
            System.out.println("莊家已經死亡，你贏了！");
            System.exit(0);
        }
    }
    //主遊戲循環迴圈
    public void play(){
        loadShotgun(false); // 初始裝填
        System.out.println("遊戲開始！玩家先手。");

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
        game.play(); // 開始遊戲
    }
}