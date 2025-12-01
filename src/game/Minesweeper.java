package game;

import java.util.*;

public class Minesweeper {
    private static int n,m;//n*m map
    private static int mines;//number of mines
    private static int remain=10;//experience points to be collected
    private static int[][] board;//number of mines in eight directions, -1=mine
    private static boolean[][] revealed;//1=opened 0=hasn't been opened
    private static int[] dx={-1,-1,-1,0,0,1,1,1};
    private static int[] dy={-1,0,1,-1,1,-1,0,1};//eight directions
    private static Scanner sc=new Scanner(System.in);

    //print board to player
    private static void printBoard(){
        System.out.print("\n   ");
        for(int j=0;j<m;j++) System.out.print(j + " ");//print numbers 0 to m
        System.out.println();
        for(int i=0;i<n;i++){
            System.out.printf("%2d ",i);//align the number 2 spaces to the right
            for(int j=0;j<m;j++){
                if(!revealed[i][j]) System.out.print("# ");//hasn't been opened
                else if(board[i][j]==-1) System.out.print("* ");//mine
                else System.out.print(board[i][j]+" ");//number of mines in eight directions
            }
            System.out.println();
        }
        System.out.println();
    }

    //Automatically open board[x][y]=0
    private static void dfs(int x,int y){
        if(x<0 || x>=n || y<0 || y>=m) return;//out of range
        if(revealed[x][y]) return;//opened
        revealed[x][y]=true;//(x,y) has opened
        if(board[x][y]!=0) return;//number of mines in eight directions!=0
        for(int d=0;d<8;d++) dfs(x+dx[d],y+dy[d]);//recursively check eight directions
    }

    //set level parameters
    private static int setLevel(int level){
        int experience=0;
        if(level==1){
            n=4;
            m=4;
            mines=4;
            experience=4;
        } else if(level==2){
            n=6;
            m=6;
            mines=6;
            experience=6;
        } else if(level==3){
            n=10;
            m=10;
            mines=10;
            experience=10;
        }
        return experience;
    }

    //game rules
    private static void gameRules(){
        System.out.println("Minesweeper\nGame rules:");
        System.out.println("1. Players start with only one life; stepping on a landmine ends the game.");
        System.out.println("2. Players need to collect 10 experience points, which vary depending on the level difficulty.");
        System.out.println("   (1) Level 1: 4x4 map with 4 mines; completing a level grants 4 experience points.");
        System.out.println("   (2) Level 2: 6x6 map with 6 mines; completing a level grants 6 experience points.");
        System.out.println("   (3) Level 3: 10x10 map with 10 mines; completing a level grants 10 experience points.");
    }

    public void startGame() {
        gameRules();//introduce game rules
        long startTime=System.currentTimeMillis();//timer

        while(remain>0){//continue playing the game until player collects enough experience points.
            System.out.println("Please select difficulty: (1/2/3)");
            int level=sc.nextInt();
            while(level!=1 && level!=2 && level!=3){//if not at the specified level
                System.out.println("Difficulty level not within the selection range.");
                System.out.println("Please select difficulty again: (1/2/3)");
                level=sc.nextInt();
            }

            int experience=setLevel(level);

            // initialize the board
            board=new int[n][m];
            revealed=new boolean[n][m];
            printBoard();

            //first enter the coordinates x and y
            int firstX,firstY;
            System.out.print("Please enter the coordinates x and y (e.g., 3 4): ");
            firstX=sc.nextInt();
            firstY=sc.nextInt();
            while(firstX<0 || firstX>=n || firstY<0 || firstY>=m){
                System.out.print("Coordinates out of range! Please enter again: ");
                firstX=sc.nextInt();
                firstY=sc.nextInt();
            }

            //randomly set mine locations
            Random rand=new Random();
            for(int k=0;k<mines;){
                int x=rand.nextInt(n);
                int y=rand.nextInt(m);
                if(board[x][y]==-1) continue;//duplicate position
                if(x==firstX && y==firstY) continue;//(firstX,firstY) must not contain mine.
                board[x][y]=-1;
                k++;
            }

            //calculate the number of mines in the eight directions.
            for(int i=0;i<n;i++){
                for(int j=0;j<m;j++){
                    if(board[i][j]==-1) continue;//mine
                    int cnt=0;//initialize the number of mines
                    for(int d=0;d<8;d++){//check eight directions
                        int nx=i+dx[d],ny=j+dy[d];//one of direction
                        if(nx>=0 && nx<n && ny>=0 && ny<m && board[nx][ny]==-1) cnt++;//mine
                    }
                    board[i][j]=cnt;
                }
            }

            dfs(firstX,firstY);
            printBoard();

            //game round
            while(true){
                System.out.print("Please enter the coordinates x and y (e.g., 3 4): ");
                int x=sc.nextInt();
                int y=sc.nextInt();
                if(x<0 || x>=n || y<0 || y>=m){
                    System.out.println("Coordinates out of range! Please enter again:");
                    continue;
                }

                if(board[x][y]==-1){//stepping on a mine
                    revealed[x][y]=true;
                    printBoard();
                    System.out.println("Game Over!");
                    return;
                }

                dfs(x,y);
                printBoard();

                //calculate revealed[i][j]=0
                int hidden=0;
                for(int i=0;i<n;i++){
                    for(int j=0;j<m;j++){
                        if(!revealed[i][j]) hidden++;
                    }
                }
                if(hidden==mines){
                    System.out.println("Congratulations! You have gained "+experience+" experience points.");
                    remain-=experience;
                    if(remain<=0){//enough experience
                        long endTime=System.currentTimeMillis();//timer stopped
                        long duration=(endTime-startTime)/1000;//passing time(seconds)
                        System.out.println("You have reached 10 experience points and passed all levels!");
                        System.out.println("Total time used: "+duration+" seconds");
                    } else {//not enough experience
                        System.out.println(remain+" points away from passing the game.");
                    }
                    break;
                }
            }
        }
    }
}
