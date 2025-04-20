
package com.example.lutemon;

public class Lutemon {
    public enum Location {
        HOME, TRAINING, BATTLE
    }

    private String name;
    private String color;
    private int atk;
    private int hp;
    private int exp;
    private int lvl;
    private int wins;
    private int losses;
    private int trainingSeconds;
    private Location location;

    public Lutemon(String name, String color, int atk, int hp) {
        this.name = name;
        this.color = color;
        this.atk = atk;
        this.hp = hp;
        this.exp = 0;
        this.lvl = 1;
        this.wins = 0;
        this.losses = 0;
        this.trainingSeconds = 0;
        this.location = Location.HOME;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
    public int getAtk() { return atk; }
    public int getHp() { return hp; }
    public int getLvl() { return lvl;}
    public int getWins() { return wins;}
    public int getLosses() { return losses;}
    public int getTrainingSeconds() { return trainingSeconds;}
    public Location getLocation() { return location; }

    public void addWin() { wins++;}
    public void addLoss() { losses++;}
    public void addTrainingSecond() { trainingSeconds++;}
    public void setLocation(Location location) { this.location = location; }

    //This is for leveling up when enough XP
    public void gainExp(int amount) {
        exp += amount;
        while (exp >= lvl * 10) {
            exp -= lvl * 10;
            lvl++;
            atk += 2;
            hp += 5;
        }
    }
}
