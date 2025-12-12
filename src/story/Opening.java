package story;

import story.extension.StoryPrinter;

public class Opening {
    public void startOpening() {
        StoryPrinter.printSlowWithSkip("It happened on a quiet night.\n" +
                        "\n" +
                        "You were walking home through a narrow back alley, taking your usual shortcut.\n" +
                        "\n" +
                        "The streetlights flickered. The air felt colder than normal.\n" +
                        "\n" +
                        "Footsteps echoed behind you.\n" +
                        "\n" +
                        "Before you could turn around, a masked figure appeared out of the darkness.\n" +
                        "\n" +
                        "\"Got you.\"\n" +
                        "\n" +
                        "A sharp blow struck the back of your head.\n" +
                        "\n" +
                        "Everything went black instantly.\n");
        StoryPrinter.printSlowWithSkip("[You wake up tied to a metal chair under a flickering spotlight.] \n" +
                        "\n" +
                        "[Your head throbs. Your wrists ache.]\n" +
                        "\n" +
                        "Rows of masked spectators stare at you in silence. \n" +
                        "\n" +
                        "A man in a black suit and a silver mask steps onto a small stage. \n" +
                        "\n" +
                        "DEALER: \"Welcome, player, to The Pit.\n" +
                        "\n" +
                        "He opens his arms as if introducing a show.\n" +
                        "\n" +
                        "DEALER: \"Four games. Win them all, and you go free.\" \n" +
                        "\n" +
                        "DEALER: \"Lose once… and you stay here forever.\" \n" +
                        "\n" +
                        "He points toward the first steel door.\n" +
                        "\n" +
                        "DEALER: \"Let’s begin.\"\n");
    }

    public void bucketshotOpening() {
        StoryPrinter.printSlowWithSkip("[You sit at a metal table. A shotgun lies between you and the Dealer.]\n" + 
                        "\n" + 
                        "The Dealer loads a mix of live and blank shells into the gun, spins the barrel, and sets it down.\n" + 
                        "\n" + 
                        "DEALER: \"Eight health each. When your health hits zero, you're dead.\"\n" + 
                        "DEALER: \"On your turn, shoot yourself or shoot me. Blanks let you keep going.\"\n" + 
                        "\n" + 
                        "He gestures to the gun.\n" + 
                        "\n" + 
                        "DEALER: \"Bullets run out, danger rises, gun reloads.\"\n" + 
                        "DEALER: \"Use any items you find.\"\n" +
                        "\n" + 
                        "The audience leans in.\n" + 
                        "\n" + 
                        "DEALER: \"Your turn, player. Let’s see how you gamble with your life.\"\n");
    }
    
    public void bucketshotClear() {
        StoryPrinter.printSlowWithSkip("The final shot hits the Dealer’s mask. The Dealer steadies himself, then laughs softly.\n" + 
                        "\n" + 
                        "DEALER: \"Impressive. Most don’t survive this table.\"\n" + 
                        "\n" + 
                        "He points toward the metal door unlocking behind him.\n" + 
                        "\n" + 
                        "DEALER: \"Game one is cleared. Move on.\"\n");
    }

    public void bucketshotAchievement() {
        StoryPrinter.printSlowWithSkip("[Achievement Unlocked] \n" +
                        "Cheers!:\n" + 
                        "Used the Beer item to skip the first bullet. A clever move.\n");
    }

    public void minesweeperOpening() {
        StoryPrinter.printSlowWithSkip("[You are shoved into a glass-floor chamber. Blue sparks crackle below.]\n" + 
                        "\n" + 
                        "A digital screen lights up:\n" + 
                        "\n" + 
                        "SYSTEM: \"FIND THE SAFE PATH. ERRORS ARE FATAL.\"\n" + 
                        "\n" + 
                        "DEALER (over speakers): \"Every step is a choice.\"\n" + 
                        "DEALER: \"Choose well, and you live.\"\n" + 
                        "DEALER: \"Choose poorly… and the floor will decide your fate.\"\n" + 
                        "\n" + 
                        "A timer begins.\n" );
    }
    
    public void minesweeperNormalClear() {
        StoryPrinter.printSlowWithSkip("The electricity shuts down. The glass door slides open.\n" + 
                        "\n" + 
                        "DEALER: \"Impressive. You can think under pressure.\"\n" + 
                        "DEALER: \"Most players don't make it this far.\"\n");
    }

    public void minesweeperPerfectClear() {
        StoryPrinter.printSlowWithSkip("[You carefully step around each hidden mine, your heart pounding with every move. Your hands shake as you mark the correct spots on the floor, calculating each step with precision.]\n" + 
                        "\n" + 
                        "You step onto the final safe tile. The floor stops vibrating.\n" + 
                        "\n" + 
                        "The deadly hum of the electricity below fades away, and the glass floor retracts. The danger is gone.\n" + 
                        "\n" + 
                        "The door to the next arena unlocks with a satisfying *click*.\n" + 
                        "\n" + 
                        "DEALER (from the speaker): \"Impressive. You survived the impossible.\"\n" + 
                        "\n" + 
                        "You hear the faint sounds of the audience murmuring in disbelief. \n" + 
                        "\n" + 
                        "DEALER: \"Onward. The next challenge awaits.\"\n" );
    }

    public void minesweeperAchievement() {
        StoryPrinter.printSlowWithSkip("[Achievement Unlocked]\n" + 
                        "Speed Run: \n" + 
                        "You cleared the Minesweeper room faster than anyone thought possible.\n");
    }

    public void escapeOpening() {
        StoryPrinter.printSlowWithSkip("[The arena door opens. You walk toward it, but your eyes catch the glint of a keyring at the Dealer's waist. A few keys dangle from the chain, swinging slightly with every step he takes.]\n" + 
                        "\n" + 
                        "The temptation is too great. You notice that the Dealer’s attention is focused on the crowd, not on you. \n" + 
                        "\n" + 
                        "You can feel the weight of the decision—this could be your chance to escape, or it could bring deadly consequences. You glance at the Dealer, and your fingers twitch toward the keyring...\n" + 
                        "\n" + 
                        "[Option: Steal the keyring?]\n" + 
                        "[A) Steal the keyring]\n" + 
                        "[B) Don’t steal the keyring]\n" );
    }

    public void noSteal() {
        StoryPrinter.printSlowWithSkip("[You glance at the keyring, but something holds you back. You take a deep breath and decide against it.]\n" + 
                        "\n" + 
                        "You choose to continue on without the keyring, focusing on the next challenge ahead. The Dealer keeps walking, unaware of your internal struggle.\n" + 
                        "\n" + 
                        "DEALER: \"Brave choice, player. Maybe not as smart, but brave.\"\n" + 
                        "\n" + 
                        "The arena door ahead beckons. The games continue, and your fate will be decided by the next trial.\n" + 
                        "\n" + 
                        "You walk forward, prepared for whatever comes next.\n" );
    }

    public void SecondSteal() {
        StoryPrinter.printSlowWithSkip("[You quickly snatch the keyring from the Dealer's belt, but you feel his gaze shift just as your hand brushes against the metal.]\n" + 
                        "\n" + 
                        "The Dealer’s eyes flicker toward you for a moment. A sudden chill runs down your spine.\n" + 
                        "\n" + 
                        "You freeze for a moment, realizing he might have noticed. His mask remains expressionless, but something feels off.\n" + 
                        "\n" + 
                        "[Option: Steal the keyring anyway?]\n" + 
                        "[A)  Steal the keyring]\n" + 
                        "[B) Don’t steal the keyring]\n" );
    }

    public void SecondStealNoSteal() {
        StoryPrinter.printSlowWithSkip("You decide to let it go. The keyring slips back into the Dealer’s belt as you walk forward, determined to face the next challenge.\n" + 
                        "\n" + 
                        "DEALER (smiling lightly): \"Wise. Some things are better left untouched.\"\n" );
    }
    
    public void RpgOpening() {
        StoryPrinter.printSlowWithSkip("[You enter a dark arena. The gate slams shut behind you.]\n" + 
                        "\n" + 
                        "The ground trembles beneath your feet.\n" + 
                        "Something moves in the shadows—fast, uneven, unpredictable.\n" + 
                        "\n" + 
                        "A distorted voice echoes from the speakers.\n" + 
                        "\n" + 
                        "DEALER: \"Your next opponent waits in the dark.\"\n" + 
                        "\n" + 
                        "A shape emerges under the flickering lights.\n" + 
                        "Sometimes a hissing Creeper.\n" + 
                        "Sometimes a staggering Zombie.\n" + 
                        "Sometimes a shrieking Witch.\n" + 
                        "\n" + 
                        "DEALER: \"A creature chosen just for you.\"\n" + 
                        "DEALER: \"Each one kills in its own delightful way.\"\n" + 
                        "\n" + 
                        "The monster roars, screeches, or hisses—depending on what appeared.\n" + 
                        "DEALER: \"Before you begin, take these.\"\n" + 
                        "A small vial of red liquid and a small bottle of green liquid slide toward you from the side.\n" + 
                        "DEALER: \"One heals, the other harms. Use them wisely.\"\n" + 
                        "He smiles behind his mask.\n" + 
                        "DEALER: \"Fight. Survive. Entertain us.\"\n" );
    }
    
    public void RpgClear() {
        StoryPrinter.printSlowWithSkip("The creature collapses, fading into the shadows.\n" + 
                        "The arena lights flicker, then stabilize.\n" + 
                        "\n" + 
                        "DEALER: \"Impressive.\"\n" + 
                        "DEALER: \"Not many players make it past this round.\"\n" + 
                        "\n" + 
                        "A metal door unlocks ahead.\n" + 
                        "\n" + 
                        "DEALER: \"One final game remains.\"\n" );
    }

    public void RpgAchievement() {
        StoryPrinter.printSlowWithSkip("[Achievement Unlocked] \n" + 
                        "Creeper? … Aww Man:\n" + 
                        "Survived the Creeper's wrath and lived to fight another day.\n" );
    }
    
    public void cloverPitOpening() {
        StoryPrinter.printSlowWithSkip("[You enter a room lit only by a giant slot machine.]\n" + 
                        "\n" + 
                        "The machine hums with an unnatural glow.\n" + 
                        "The masked audience encircles you like predators.\n" + 
                        "\n" + 
                        "The Dealer appears, standing beside the machine.\n" + 
                        "\n" + 
                        "DEALER: \"Strength, logic, courage… admirable traits.\"\n" + 
                        "DEALER: \"But in the end, fate decides everything.\"\n" + 
                        "\n" + 
                        "He gestures toward the lever.\n" + 
                        "\n" + 
                        "DEALER: \"One pull. If the Clover appears—you walk free.\"\n" + 
                        "DEALER: \"If not… The Pit keeps you.\"\n" + 
                        "\n" + 
                        "The machine begins vibrating as you place your hand on the lever.\n" + 
                        "\n" + 
                        "DEALER: \"Go ahead. Try your luck.\"\n" );
    }
    
    public void cloverPitNormalClear() {
        StoryPrinter.printSlowWithSkip("[The reels spin once more. The lights flash brightly as the final round slows.]\n" + 
                        "\n" + 
                        "With a soft *click*, the machine stops. The results are in.\n" + 
                        "\n" + 
                        "You check the prize. It's just enough to pay off your debt.\n" + 
                        "\n" + 
                        "A sigh of relief escapes your lips, but it's mixed with the frustration of not hitting a bigger win.\n" + 
                        "\n" + 
                        "DEALER (smiling): \"Well, you managed to escape with enough to settle your debt. Not bad.\"\n" );
    }

    public void cloverPitPerfectClear() {
        StoryPrinter.printSlowWithSkip("[The reels spin and spin, each turn taking longer than the last.]\n" + 
                        "\n" + 
                        "The machine finally slows down, and you catch your breath. The result: ** perfect matches.**\n" + 
                        "\n" + 
                        "The jackpot amount is far beyond what you imagined. It's more than enough to pay your debt, and the rest feels like an unexpected fortune.\n" + 
                        "\n" + 
                        "The audience erupts in applause, and even the Dealer is momentarily stunned.\n" + 
                        "\n" + 
                        "DEALER (in disbelief): \"Well... you’ve done it. Far more than just escaping.\"\n" );
    }

    public void cloverPitAchievement() {
        StoryPrinter.printSlowWithSkip("[Achievement Unlocked] \n" + 
                        "Coin Master: \n" + 
                        "You used the Coin Manipulator three times and still came out on top.\n" );
    }
}
