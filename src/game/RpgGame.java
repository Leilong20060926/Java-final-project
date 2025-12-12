package game;
import game.rpg.role;
import java.util.Random;
import java.util.Scanner;
import game.extension.GamePrinter;

public class RpgGame{
    public int[] play (){
        // Judgement variables
        int clear3 = 0;
        int achievement3 = 0;

        Scanner sc = new Scanner(System.in);
        Random rand = new Random(); // Random number generator

        System.out.println("================================================");
        System.out.println("                RPG GAME RULES                  ");
        System.out.println("================================================");
        GamePrinter.printSlow("1. You will enter a battle against a randomly");
        GamePrinter.printSlow("   generated monster.");
        GamePrinter.printSlow("2. Each turn, you can choose one of two actions:");
        GamePrinter.printSlow("     (1) Throw Drug Potion to attack monster.");
        GamePrinter.printSlow("     (2) Drink Healing Potion to recover your HP.");
        GamePrinter.printSlow("3. Potions are limited. Once you run out, you");
        GamePrinter.printSlow("   cannot use them anymore.");
        GamePrinter.printSlow("4. After your action, the monster will counterattack");
        GamePrinter.printSlow("   if it is still alive.");
        GamePrinter.printSlow("5. The battle ends when either you or the monster");
        GamePrinter.printSlow("   reaches 0 HP.");
        System.out.println("================================================\n");

        System.out.println("============================");
        System.out.println("Welcome to the RPG Game !");
        System.out.println("============================");
        GamePrinter.printSlow("Enter your character's name: ");
        String playerName = sc.nextLine();

        role player = new role(playerName, 31, 18, 10);  // Create player character
        System.out.println("Character Created: " + player.getName() + 
                            " | HP: " + player.getHP() + " | ATK: " + player.getATK() +
                             " | DEF: " + player.getDEF());

        // Define some monsters
        String [] monsterNames = {"Creeper", "Zombie" , "Witch"}; 

        // Generate a random monster
        int monsters = rand.nextInt(monsterNames.length);
        String monsterName = monsterNames[monsters];  // Randomly selected monster name

        role monster; // Declare monster variable
        monster = switch (monsterName) {
            case "Creeper" -> new role("Creeper", 23, 17, 11);
            case "Zombie" -> new role("Zombie", 25, 15, 9);
            default -> new role("Witch", 28, 18, 13);
        };
        
        GamePrinter.printSlow(" Random Created: " + monsterName + " | HP: " + monster.getHP() + 
                            " | ATK: " + monster.getATK() +
                             " | DEF: " + monster.getDEF());

        int drugPotion = 3;  // Number of drug potions
        int healingPotion = 2; // Number of healing potions
        
        while(player.getHP() > 0 && monster.getHP() > 0){
            System.out.println("\nChoose your action: 1. Throw drug potion 2. Drinking healing potion");
            System.out.println(" (You have " + drugPotion + " drug potions and " 
                                + healingPotion + " healing potions left.) ");
            int choice = sc.nextInt();

            switch(choice){
                case 1 -> {
                    if(drugPotion <= 0){
                        System.out.println(" No drug potions left ! ");
                        continue; //skip the turn
                    }
                    drugPotion = drugPotion - 1; // Use one drug potion
                    // Calculate damage to monster
                    int drugDamage = Math.max(1, player.getATK() - monster.getDEF() + rand.nextInt(3)); 
                    monster.setHP(monster.getHP() - drugDamage);
                    System.out.println(playerName + " throw drug potion causing " +
                            drugDamage + " damage to " + monsterName);
                }
                case 2 -> {
                    if(healingPotion <= 0){
                        System.out.println(" No healing potions left ! ");
                        continue; // skip the turn
                    }
                    healingPotion = healingPotion - 1; // Use one healing potion
                    int healAmount = rand.nextInt(4) + 6; //recover 6 - 9
                    player.setHP(player.getHP() + healAmount);
                    System.out.println(playerName + " drinks healing potion and recovers " +
                            healAmount + " HP.");
                }
                default -> {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }
            }

            System.out.println("\nAfter your action:");
            System.out.println(playerName + " HP : " + player.getHP() + 
                                " | " + monsterName + " HP : " + monster.getHP());

            // Monster's turn to attack if it's still alive
            if(monster.getHP() > 0){
                int monsterDamage = Math.max(1, monster.getATK() - player.getDEF() + rand.nextInt(6)); 
                player.setHP(player.getHP() - monsterDamage);
                System.out.println(monsterName + " attacks " + playerName + 
                                    " causing " + monsterDamage + " damage.");
            }

            System.out.println("\nAfter monster's action:");
            System.out.println( playerName + " HP : " + player.getHP() + 
                                " | " + monsterName + " HP : " + monster.getHP());
        
        }
        // Determine the outcome of the battle
        if (player.getHP() <= 0 && monster.getHP() <= 0) {
            System.out.println("\nBoth " + playerName + " and " + monsterName +
                    " died! It's a draw!");
        } else if (player.getHP() <= 0) {
            System.out.println("\n" + playerName + " died! " + monsterName + " wins!");
        } else if (monster.getHP() <= 0) {
            System.out.println("\n" + monsterName + " died! " + playerName + " wins!");
            clear3++;
        }

        return new int[]{clear3, achievement3};

    }
}