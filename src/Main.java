import game.Minesweeper;
import story.Opening;
public class Main {
    public static void main(String[] args) {
        System.out.println("Project\n");
        Opening opening = new Opening();
        opening.startOpening();
        Minesweeper minesweeper=new Minesweeper();
        minesweeper.startGame();
    }
}
