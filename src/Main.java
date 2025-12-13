import game.CloverPit;
import game.FakeBucketshotRoulette;
import game.Minesweeper;
import game.RpgGame;
import java.util.Scanner;
import story.Ending;
import story.Opening;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        Ending ending = new Ending();
        int achievement1=0,achievement2=0,achievement3=0,achievement4=0;
        int achievementall=0;
        int ending1=0,ending2=0,ending3=0,ending4=0,ending5=0,ending6=0,ending7=0;

        while(true){
            int game1=0,game2=0,game3=0,game4=0;
            int gameperfect2=0,gameperfect4=0;

            Opening opening = new Opening();
            opening.startOpening();

            while(game1==0){
                opening.bucketshotOpening();
                delay();
                FakeBucketshotRoulette fakebucketshotroulette=new FakeBucketshotRoulette();
                int result[]=fakebucketshotroulette.play();
                game1=result[0];
                if(game1==0){
                    ending.badEnding();
                    ending4++;
                    continueGame(0);
                } else {
                    opening.bucketshotClear();
                }
                if(achievement1==0 && result[1]==1 && game1==1){
                    achievement1=1;
                    opening.bucketshotAchievement();
                }
            }

            while(game2==0){
                opening.minesweeperOpening();
                delay();
                Minesweeper minesweeper=new Minesweeper();
                int result[]=minesweeper.play();
                game2=result[0];
                gameperfect2=result[1];
                if(game2==0){
                    ending.badEnding();
                    ending4++;
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
            int choice = sc.nextInt();
            while (choice != 1 && choice != 2) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                choice = sc.nextInt();
            }
            if(choice==2){
                opening.noSteal();
            } else {
                double rand = Math.random();
                if(rand<0.5){
                    ending.escapeEnding();
                    ending5++;
                    continueGame(1);
                    continue;
                } else {
                    opening.secondSteal();
                    int choice2 = sc.nextInt();
                    while (choice2 != 1 && choice2 != 2) {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                        choice2 = sc.nextInt();
                    }
                    if(choice2==2){
                        opening.secondStealNoSteal();
                    } else {
                        ending.failedEscapeEnding();
                        ending6++;
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
                    ending4++;
                    continueGame(0);
                } else {
                    opening.rpgClear();
                }
                if(achievement3==0 && result[1]==1 && game3==1){
                    achievement3=1;
                    opening.rpgAchievement();
                }
            }

            while(game4==0){
                opening.cloverPitOpening();
                delay();
                CloverPit cloverpit=new CloverPit();
                int result[]=cloverpit.play();
                game4=result[0];
                gameperfect4=result[1];
                if(game4==0){
                    ending.badEnding();
                    ending4++;
                    continueGame(0);
                } else if(gameperfect4==1) {
                    opening.cloverPitPerfectClear();
                } else {
                    opening.cloverPitNormalClear();
                }
                if(achievement4==0 && result[2]==1 && game4==1){
                    achievement4=1;
                    opening.cloverPitAchievement();
                }
            }

            if(achievementall==0 && achievement1==1 && achievement2==1 && achievement3==1 && achievement4==1){
                achievementall=1;
                ending.achievementEnding();
                ending7++;
                continueGame(1);
            }

            if(gameperfect2==0 && gameperfect4==0){
                ending.neutralEnding();
                ending2++;
                continueGame(1);
            } else if(gameperfect2==1 && gameperfect4==1){
                ending.dealerEnding();
                ending3++;
                continueGame(1);
            } else {
                ending.trueEnding();
                ending1++;
                continueGame(1);
            }

            if(ending1>0 && ending2>0 && ending3>0 && ending4>0 && ending5>0 && ending6>0 && ending7>0){
                ending.easterEgg();
                break;
            }
        }
    }

    public static void delay() {
        try {
            Thread.sleep(3000);
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
        int choice = sc.nextInt();
        while (choice != 0 && choice != 1) {
            if(re==0){
                System.out.println("Invalid input. Please enter 1 (Yes) or 0 (No) to retry.");
            } else {
                System.out.println("Invalid input. Please enter 1 (Yes) or 0 (No) to restart.");
            }
            choice = sc.nextInt();
        }
        if (choice == 0) {
            System.out.println("Exiting the game. Thanks for playing! Goodbye!");
            System.exit(0);
        }
    }
}
