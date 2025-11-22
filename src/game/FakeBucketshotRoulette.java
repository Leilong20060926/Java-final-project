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
    }
