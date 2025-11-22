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
    private void loadShotgun() {
        shotgun.clear();
        int totalShells = random.nextInt(7)+ 2; // 2-8發子彈
        int liveShells = random.nextInt(totalShells)+1; // 至少1發實彈
        for (int i = 0; i < liveShells; i++) {
            shotgun.add("live");
        }
        for (int i=0 ; i < totalShells- liveShells; i++) {
            shotgun.add("blank");
        }
        Collections.shuffle(shotgun);//隨機打亂子彈筒
        System.out.println("子彈筒已裝填，共有 " + totalShells + " 發子彈，其中 " + liveShells + " 發為實彈。");
        
    }
    private void displayStatus() {
        System.out.println("\n-- 目前狀態 --");
        System.out.println("玩家生命值: " + playerHealth);
        System.out.println("莊家生命值: " + dealerHealth);
        System.out.println("剩餘子彈數: " + shotgun.size());
        } 
    
    private void PlayAction() {
        displayStatus();
        System.out.println("你的回合，選擇行動");
        System.out.println("1. 射擊自己");
        System.out.println("2. 射擊莊家");
        int choice = scanner.nextInt();
        boolean shootSelf = (choice == 1);
        String shell = shotgun.remove(0); // 取第一顆子彈
        System.out.println("扣下板機..."+(shell.equals("live") ? "實彈！" : "空包彈"));
        if (shootSelf) {
            if (shell.equals("live")) {
                playerHealth--;
                System.out.println("你中了自己的子彈，生命值減少1！");
                playerTurn = false;
            } else {
                System.out.println("幸運！繼續你的回合");
                playerTurn = true;
            }
        }
        else {
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
}