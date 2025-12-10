package game.extension;
//enum is safer than class with static final variables when typing error ide can fix it automatically
public enum Item {
    BEER("Beer", "Racks the shotgun (ejects the current shell)"),
    CIGARETTES("Cigarettes", "Restores 1 Health"),
    MAGNIFYING_GLASS("Magnifying Glass", "Check if the current shell is live or blank"),
    HANDCUFFS("Handcuffs", "Skip the Dealer's next turn"),
    SAW("Hand Saw", "Double damage for the next shot (2 dmg)");

    private final String name;
    private final String description;

    Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}