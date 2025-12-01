package game;
import game.rpg.role;
import java.util.Random;
import java.util.Scanner;

public class RpgGame{
    public static void main (String[]args){
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("============================");
        System.out.println("Welcome to the RPG Game !");
        System.out.println("============================");
        System.out.print("Enter your character's name: ");
        String playerName = sc.nextLine();

        role player = new role(playerName, 30, 17, 10);
        System.out.println("Character Created: " + player.getName() + 
                            " | HP: " + player.getHP() + " | ATK: " + player.getATK() +
                             " | DEF: " + player.getDEF());

        // Define some monsters
        String [] monsterNames = {"Creeper", "Zombie" , "Witch"};

        // Generate a random monster
        int monsters = rand.nextInt(monsterNames.length);
        String monsterName = monsterNames[monsters];

        role monster;
        monster = switch (monsterName) {
            case "Creeper" -> new role("Creeper", 23, 17, 11);
            case "Zombie" -> new role("Zombie", 25, 15, 9);
            default -> new role("Witch", 28, 18, 13);
        };
        
        System.out.println(" Random Created: " + monsterName + " | HP: " + monster.getHP() + 
                            " | ATK: " + monster.getATK() +
                             " | DEF: " + monster.getDEF());

        int drugPotion = 3;
        int healingPotion = 2;
        
        while(player.getHP() > 0 && monster.getHP() > 0){
            System.out.println("\nChoose your action: 1. Throw drug potion 2. Drinking healing potion");
            System.out.println(" (You have " + drugPotion + " drug potions and " 
                                + healingPotion + " healing potions left.) ");
            int choice = sc.nextInt();

            switch(choice){
                case 1 -> {
                    if(drugPotion == 0){
                        System.out.println(" No drug potions left ! ");
                        System.out.println(playerName + " has no more drug potions to throw.");
                        player.setHP(0); // Set player HP to 0 to end the game
                        break;
                    }
                    drugPotion = drugPotion - 1;
                    int drugDamage = Math.max(1, player.getATK() - monster.getDEF() + rand.nextInt(3));
                    monster.setHP(monster.getHP() - drugDamage);
                    System.out.println(playerName + " throw drug potion causing " +
                            drugDamage + " damage to " + monsterName);
                }
                case 2 -> {
                    if(healingPotion <= 0){
                        System.out.println(" No healing potions left ! Choose another action.");
                        continue;
                    }
                    healingPotion = healingPotion - 1;
                    int healAmount = rand.nextInt(3) + 4; // Heal between 4 to 6 HP
                    player.setHP(player.getHP() + healAmount);
                    System.out.println(playerName + " drinks healing potion and recovers " +
                            healAmount + " HP.");
                }
                default -> {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }
            }

            System.out.println(playerName + " HP : " + player.getHP() + 
                                " | " + monsterName + " HP : " + monster.getHP());

            if(monster.getHP() > 0){
                int monsterDamage = Math.max(1, monster.getATK() - player.getDEF() + rand.nextInt(8));
                player.setHP(player.getHP() - monsterDamage);
                System.out.println(monsterName + " attacks " + playerName + 
                                    " causing " + monsterDamage + " damage.");
            }

            System.out.println( playerName + " HP : " + player.getHP() + 
                                " | " + monsterName + " HP : " + monster.getHP());

        
    }
        if(player.getHP() <= 0){
            System.out.println("\n" + playerName + " died ! " + monsterName + " Win ! ");
        }else if(monster.getHP() <= 0){
            System.out.println("\n " + monsterName + " died ! " + playerName + " Win ! ");
        }else if(player.getHP() <= 0 && monster.getHP() <= 0){
            System.out.println("Both " + playerName + " and " + monsterName + 
                                " died ! It's a draw !");
        }
        System.out.println("\n===== GAME OVER =====");

        
}
}