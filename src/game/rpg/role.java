package game.rpg;

public class role{
    private String name;
    private int HP;
    private int ATK;
    private int DEF;

    public role(String name, int HP, int ATK, int DEF) {
        this.name = name;
        this.HP = HP;
        this.ATK = ATK;
        this.DEF = DEF;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getHP(){
        return HP;
    }
    public void setHP(int HP){
        this.HP = HP;
    }
    public int getATK(){
        return ATK;
    }
    public void setATK(int ATK){
        this.ATK = ATK;
    }
    public int getDEF(){
        return DEF;
    }
    public void setDEF(int DEF){
        this.DEF = DEF;
    }

}