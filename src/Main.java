import game.CloverPit;
import game.FakeBucketshotRoulette;
import game.Minesweeper;
import game.RpgGame;
import java.util.Scanner;
import story.Ending;
import story.Opening;

public class Main {
    public static void main(String[] args) {
        Ending ending = new Ending();
        int achievement1=0,achievement2=0,achievement3=0,achievement4=0;

        while(true){
            int game1=0,game2=0,game3=0,game4=0;
            int gameperfect2=0,gameperfect4=0;

            Opening opening = new Opening();
            opening.startOpening();

            /*while(game1==0){
                opening.bucketshotOpening();
                delay();
                FakeBucketshotRoulette fakebucketshotroulette=new FakeBucketshotRoulette();
                int result[]=fakebucketshotroulette.play();
                game1=result[0];
                if(game1==0){
                    ending.badEnding();
                    continueGame(0);
                } else {
                    opening.bucketshotClear();
                }
                if(achievement1==0 && result[1]==1){
                    achievement1=1;
                    opening.bucketshotAchievement();
                }
            }*/

            while(game2==0){
                opening.minesweeperOpening();
                delay();
                Minesweeper minesweeper=new Minesweeper();
                int result[]=minesweeper.play();
                game2=result[0];
                gameperfect2=result[1];
                if(game2==0){
                    ending.badEnding();
                    continueGame(0);
                }else if(gameperfect2==1) {
                    opening.minesweeperPerfectClear();
                } else {
                    opening.minesweeperNormalClear();
                }
                if(achievement2==0 && result[2]==1){
                    achievement2=1;
                    opening.minesweeperAchievement();
                }
            }

            opening.escapeOpening();
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            while (choice != 1 && choice != 2) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                choice = scanner.nextInt();
            }
            if(choice==2){
                opening.noSteal();
            } else {
                double rand = Math.random();
                if(rand<0.5){
                    ending.escapeEnding();
                    continueGame(1);
                    continue;
                } else {
                    opening.secondSteal();
                    int choice2 = scanner.nextInt();
                    while (choice2 != 1 && choice2 != 2) {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                        choice2 = scanner.nextInt();
                    }
                    if(choice2==2){
                        opening.secondStealNoSteal();
                    } else {
                        ending.failedEscapeEnding();
                        continueGame(1);
                        continue;
                    }
                }
            }

            while(game3==0){
                opening.rpgOpening();
                delay();
                RpgGame rpggame=new RpgGame();
                int result[]=rpggame.play();
                game3=result[0];
                if(game3==0){
                    ending.badEnding();
                    continueGame(0);
                } else {
                    opening.rpgClear();
                }
                if(achievement3==0 && result[1]==1){
                    achievement3=1;
                    opening.rpgAchievement();
                }
            }

            while(game4==0){
                opening.cloverPitOpening();
                delay();
                CloverPit cloverpit=new CloverPit();
                
            }
        }
    }

    public static void delay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void continueGame(int re) {
        if(re==0){
            System.out.println("Retry? (1: Yes / 0: No)");
        } else {
            System.out.println("Restart? (1: Yes / 0: No)");
        }
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        while (choice != 0 && choice != 1) {
            if(re==0){
                System.out.println("Invalid input. Please enter 1 (Yes) or 0 (No) to retry.");
            } else {
                System.out.println("Invalid input. Please enter 1 (Yes) or 0 (No) to restart.");
            }
            choice = scanner.nextInt();
        }
        if (choice == 0) {
            System.out.println("Exiting the game. Goodbye!");
            System.exit(0);
        }
    }
}
