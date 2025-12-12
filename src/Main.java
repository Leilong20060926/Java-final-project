import game.FakeBucketshotRoulette;
import game.Minesweeper;
import story.Opening;

public class Main {
    public static void main(String[] args) {
        int restart=0;
        int achievement1=0,achievement2=0,achievement3=0,achievement4=0;
        while(restart==0){
            int game1=0,game2=0,game3=0,game4=0;
            Opening opening = new Opening();
            opening.startOpening();
            while(game1==0){
                FakeBucketshotRoulette fakebucketshotroulette=new FakeBucketshotRoulette();
                int result[]=fakebucketshotroulette.play();
                
            }
            while(game2==0){
                Minesweeper minesweeper=new Minesweeper();
                int result[]=minesweeper.play();
            }
        }
    }
}
