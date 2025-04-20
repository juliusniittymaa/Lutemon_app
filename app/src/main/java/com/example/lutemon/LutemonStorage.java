package com.example.lutemon;

import java.util.ArrayList;
import java.util.List;

public class LutemonStorage {
    private static LutemonStorage instance;
    private final List<Lutemon> lutemons;

    private int totalBattles = 0;
    private int totalTrainingTime = 0;

    public int getTotalBattles() { return totalBattles; }
    public int getTotalTrainingTime() { return totalTrainingTime; }

    public void addBattles() { totalBattles++; }
    public void addTrainingTime() { totalTrainingTime++; }

    private LutemonStorage() {
        lutemons = new ArrayList<>();
    }

    //getting single instance
    public static LutemonStorage getInstance() {
        if (instance == null) {
            instance = new LutemonStorage();
        }
        return instance;
    }

    public List<Lutemon> getLutemons() {
        return lutemons;
    }

    public void addLutemon(Lutemon l) {
        lutemons.add(l);
    }
}
