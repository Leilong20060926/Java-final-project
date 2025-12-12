package story;

import story.extension.StoryPrinter;

public class Ending {
    public void trueEnding() {
        StoryPrinter.printSlowWithSkip("DEALER: \"Impressive. You've cleared all the tests.\"\n" + 
                        "\n" + 
                        "He gestures to the exit, and the crowd goes silent.\n" + 
                        "\n" + 
                        "DEALER: \"One perfect clear… You’ve earned your freedom, player.\"\n" + 
                        "\n" + 
                        "You step toward the door, and for the first time, you feel the weight of the games lifting off your shoulders.\n" + 
                        "\n" + 
                        "You hear the sound of metal unlocking and the door creaks open.\n" + 
                        "\n" + 
                        "You walk through the door, the cold night air hitting your face.\n" + 
                        "\n" + 
                        "[TRUE ENDING]\n" + 
                        "");
    }

    public void neutralEnding() {
        StoryPrinter.printSlowWithSkip("[You step away from the slot machine, your heart still racing as you look at the money you’ve won.]\n" + 
                        "\n" +
                        "The dealer stands quietly, watching you.\n" + 
                        "\n" + 
                        "DEALER: \"You’ve made it. All four games, cleared.\"\n" + 
                        "\n" + 
                        "The door to freedom slowly opens, and you step toward it. The cool night air feels like a blessing.\n" + 
                        "\n" + 
                        "DEALER: \"But remember, surviving The Pit doesn't make you free. It just means you’ve won this round.\"\n" + 
                        "\n" + 
                        "You glance back at the arena, knowing that what you’ve conquered today may only be a small victory.\n" + 
                        "\n" + 
                        "DEALER: \"We’ll meet again. One way or another.\"\n" + 
                        "\n" + 
                        "With those words, you leave the arena and step into the unknown.\n" + 
                        "\n" + 
                        "[NEUTRAL ENDING]\n" + 
                        "");
    }

    public void dealerEnding() {
        StoryPrinter.printSlowWithSkip("The Dealer steps toward you, very slowly.\n" + 
                        "\n" + 
                        "DEALER: \"Interesting. Very interesting.\"\n" + 
                        "DEALER: \"You’re not just a survivor. You’re something more.\"\n" + 
                        "\n" + 
                        "He offers you his silver mask.\n" + 
                        "\n" + 
                        "DEALER: \"This place needs a new master. Someone worthy.\"\n" + 
                        "DEALER: \"Take the mask. Take The Pit.\"\n" + 
                        "\n" + 
                        "The audience bows their heads.\n" + 
                        "\n" + 
                        "You put on the mask.\n" + 
                        "A strange power rushes through your body.\n" + 
                        "\n" + 
                        "You are no longer the player.\n" + 
                        "You are the Dealer.\n" + 
                        "\n" + 
                        "[DEALER ENDING]\n" + 
                        "");
    }

    public void badEnding() {
        StoryPrinter.printSlowWithSkip("A fatal choice. A final breath.\n" + 
                        "\n" + 
                        "The audience applauds politely.\n" + 
                        "\n" + 
                        "DEALER: \"Unfortunate.\"\n" + 
                        "DEALER: \"But every game needs its losers.\"\n" + 
                        "\n" + 
                        "Your vision fades as the lights dim.\n" + 
                        "\n" + 
                        "The Pit claims another soul.\n" + 
                        "\n" + 
                        "[BAD ENDING]\n" + 
                        "");
    }

    public void escapeEnding() {
        StoryPrinter.printSlowWithSkip("[You make your move. Your hand slips out quickly, snatching the keyring from the Dealer's belt.]\n" + 
                        "\n" + 
                        "The keys feel cold against your fingers, but the rush of success pushes you forward. You barely believe it, but you've done it. You now hold the keys to freedom.\n" + 
                        "\n" + 
                        "The Dealer, still distracted by the audience, doesn’t notice a thing. You quietly slip the keys into your pocket and make your way toward a hidden exit.\n" + 
                        "\n" + 
                        "Suddenly, you hear a door creak open as you approach the back exit of the arena. The lock turns easily under your hand.\n" + 
                        "\n" + 
                        "The sound of the crowd fades away as you step outside. The chill of the night air fills your lungs. For the first time since you arrived, you feel free.\n" + 
                        "\n" + 
                        "The Dealer's voice echoes behind you.\n" + 
                        "\n" + 
                        "DEALER: \"So, you think you can escape? Enjoy it while you can.\"\n" + 
                        "\n" + 
                        "You walk away, knowing this is only the beginning.\n" + 
                        "\n" + 
                        "[ESCAPE ENDING: You escaped The Pit.]\n" + 
                        "");
        
    }

    public void failedEscapeEnding() {
        StoryPrinter.printSlowWithSkip("The Dealer grabs your wrist with surprising speed, a cruel smile forming on his face.\n" + 
                        "\n" + 
                        "DEALER: \"You thought you could slip by me, didn’t you?\"\r\n" + //
                        "\n" + 
                        "The keyring falls to the floor, and the Dealer’s grip tightens. The door to the next arena slams shut behind you.\n" + 
                        "\n" + 
                        "[FAILED ESCAPE ENDING]\n" + 
                        "");
    }

    public void achievementEnding() {
        StoryPrinter.printSlowWithSkip("[You step through the final door, your body weary from the trials you’ve faced. The arena behind you is silent now.]\n" + 
                        "\n" + 
                        "You wander through the empty halls, unsure of what comes next. Then, as you turn a corner, you spot something glimmering on a pedestal in the center of the room.\n" + 
                        "\n" + 
                        "A small box, its surface gleaming with an odd, metallic sheen. The logo of the Dealer is etched into the top, glowing faintly in the dim light.\n" + 
                        "\n" + 
                        "DEALER: \"Ah, I see you’ve made it this far.\"\n" + 
                        "\n" + 
                        "A surge of realization hits you— You’ve done it. You’ve completed every challenge, every twist, every trial.\n" + 
                        "\n" + 
                        "DEALER: \"You’ve completed everything... Every challenge, every game, and every choice. You’ve surpassed expectations, player.\"\n" + 
                        "\n" + 
                        "[ALL ACHIEVEMENT UNLOCKED ENDING]\n" + 
                        "");
    }
}