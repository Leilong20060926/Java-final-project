package game;

import java.util.Scanner;

import game.extension.GamePrinter;

import java.util.Random;
import java.util.ArrayList;//比陣列更靈活
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
    private double dangerLevel = 0.2; // 危險等級，影響實彈比例
    

    // 建構子
    public FakeBucketshotRoulette(){
        shotgun = new ArrayList<>();
        playerHealth = MAX_HEALTH;
        dealerHealth = MAX_HEALTH;
        playerTurn = true; // 玩家先手
        random = new Random();
        scanner = new Scanner(System.in);
        
    }
    

    //初始化子彈筒
    // 加上重新裝填子彈功能
    //危險等級邏輯一開始0.2每次重新裝填增加0.1上限1.0
    private void loadShotgun(boolean increaseDanger) {
        if (increaseDanger) {
            dangerLevel += 0.1;
            if (dangerLevel > 1.0) dangerLevel = 1.0;
        }

        shotgun.clear();

        int totalShells = random.nextInt(7) + 2;
        int maxLive = (int) Math.floor(totalShells * dangerLevel);
        int liveShells = 1;

        if (maxLive > 1) {
            liveShells = random.nextInt(maxLive) + 1;
        }

        for (int i = 0; i < liveShells; i++) shotgun.add("live");
        for (int i = 0; i < totalShells - liveShells; i++) shotgun.add("blank");

        Collections.shuffle(shotgun);

        GamePrinter.printSlow("子彈筒已裝填：共 " + totalShells +
                " 發，其中實彈 " + liveShells +
                " 發（本次上限比例：" + (int)(dangerLevel * 100) + "%）");
    }
    private void displayStatus() {
        GamePrinter.printSlow("\n-- 目前狀態 --");
        GamePrinter.printSlow("玩家生命值: " + playerHealth);
        GamePrinter.printSlow("莊家生命值: " + dealerHealth);
        GamePrinter.printSlow("剩餘子彈數: " + shotgun.size());
        } 
    //玩家邏輯
    //加上打完填裝危險子彈
   private void PlayAction() {
    displayStatus();
    GamePrinter.printSlow("你的回合，選擇行動");
    GamePrinter.printSlow("1. 射擊自己");
    GamePrinter.printSlow("2. 射擊莊家");
    int choice = scanner.nextInt();
    boolean shootSelf = (choice == 1);

    // 如果子彈筒沒子彈，重新裝填
    if (shotgun.isEmpty()) {
        GamePrinter.printSlow("子彈打完了，重新裝填...");
        loadShotgun(true); // 重新裝填危險模式
    }

    String shell = shotgun.remove(0); // 取第一顆子彈
    GamePrinter.printSlow("扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
    if (shootSelf) {
        if (shell.equals("live")) {
            playerHealth--;
            GamePrinter.printSlow("你中了自己的子彈，生命值減少1！");
            playerTurn = false;
        } else {
            GamePrinter.printSlow("幸運！繼續你的回合");
            playerTurn = true;
        }
    } else {
        if (shell.equals("live")) {
            dealerHealth--;
            GamePrinter.printSlow("你射中了莊家，莊家生命值減少1！");
            playerTurn = true;
        } else {
            GamePrinter.printSlow("莊家躲過一劫，輪到莊家回合");
            playerTurn = false;
        }
    }
}
    //莊家邏輯
    private void dealerAction() {
    displayStatus();

    // 如果子彈筒沒子彈，重新裝填
    if (shotgun.isEmpty()) {
        GamePrinter.printSlow("子彈打完了，重新裝填...");
        loadShotgun(true);
    }

    int liveShells = (int) shotgun.stream().filter(shell -> shell.equals("live")).count();
    boolean shootSelf = (double) liveShells / shotgun.size() < 0.5;
    String shell = shotgun.remove(0);

    if (shootSelf) {
        GamePrinter.printSlow("莊家選擇射擊自己。扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
        if (shell.equals("live")) {
            dealerHealth--;
            GamePrinter.printSlow("莊家中了自己的子彈，生命值減少1！");
            playerTurn = true;
        } else {
            GamePrinter.printSlow("莊家繼續他的回合");
            playerTurn = false;
        }
    } else {
        GamePrinter.printSlow("莊家選擇射擊你。扣下板機..." + (shell.equals("live") ? "實彈！" : "空包彈"));
        if (shell.equals("live")) {
            playerHealth--;
            GamePrinter.printSlow("莊家射中了你，你的生命值減少1！");
            playerTurn = false;
        } else {
            GamePrinter.printSlow("你幸運地躲過一劫，輪到你的回合");
            playerTurn = true;
            }
        }
    }
    //遊戲結束判定
    private void isGameOver() {
        if (playerHealth <= 0) {
            GamePrinter.printSlow("你已經死亡，遊戲結束！");
            System.exit(0);
        } else if (dealerHealth <= 0) {
            GamePrinter.printSlow("莊家已經死亡，你贏了！");
            System.exit(0);
        }
    }
    //主遊戲循環迴圈
    public void play(){
        loadShotgun(false); // 初始裝填
        GamePrinter.printSlow("遊戲開始！玩家先手。");

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