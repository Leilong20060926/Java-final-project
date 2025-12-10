package story;

public class Opening {
    public void startOpening() {
        System.out.println("It happened on a quiet night.\r\n" + //
                        "\r\n" + //
                        "You were walking home through a narrow back alley, taking your usual shortcut.\r\n" + //
                        "The streetlights flickered. The air felt colder than normal.\r\n" + //
                        "\r\n" + //
                        "Footsteps echoed behind you.\r\n" + //
                        "\r\n" + //
                        "Before you could turn around, a masked figure appeared out of the darkness.\r\n" + //
                        "\r\n" + //
                        "\"Got you.\"\r\n" + //
                        "\r\n" + //
                        "A sharp blow struck the back of your head.\r\n" + //
                        "Everything went black instantly.\r\n" + //
                       "");
                       try {
                        Thread.sleep(10000);
                       }
                       catch (InterruptedException a){
                        a.printStackTrace();
                    
                       }
                       System.out.println("[You wake up tied to a metal chair under a flickering spotlight.] \n[Your head throbs. Your wrists ache.]\nRows of masked spectators stare at you in silence. A man in a black suit and a silver mask steps onto a small stage. \nDEALER: \"Welcome, player, to The Pit.\nHe opens his arms as if introducing a show.\nDEALER: \"Four games. Win them all, and you go free.\" \nDEALER: \"Lose once… and you stay here forever.\" \nHe points toward the first steel door.\nDEALER: \"Let’s begin.\"\n");
    }
}
