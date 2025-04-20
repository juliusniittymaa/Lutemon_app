package com.example.lutemon;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleArenaFragment extends Fragment {

    private TextView textBattleLog, textSelectedLutemons;
    private Button btnStartBattle, btnSelectLutemons;
    private ImageView imageLutemonA, imageLutemonB;
    private TextView textNameA, textNameB;
    private ScrollView scrollLog;

    private List<Lutemon> selectedLutemons = new ArrayList<>();

    //Inflates the layout for the battle arena view tab
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle_arena, container, false);


        textBattleLog = view.findViewById(R.id.textBattleLog);
        textSelectedLutemons = view.findViewById(R.id.textSelectedLutemons);
        btnStartBattle = view.findViewById(R.id.btnStartBattle);
        btnSelectLutemons = view.findViewById(R.id.btnSelectLutemons);
        imageLutemonA = view.findViewById(R.id.imageLutemonA);
        imageLutemonB = view.findViewById(R.id.imageLutemonB);
        textNameA = view.findViewById(R.id.textNameA);
        textNameB = view.findViewById(R.id.textNameB);
        scrollLog = view.findViewById(R.id.scrollViewBattleLog);

        btnSelectLutemons.setOnClickListener(v -> selectLutemonsFromHome());
        btnStartBattle.setOnClickListener(v -> startBattle());

        updateSelectedDisplay();

        return view;
    }

    private void selectLutemonsFromHome() {
        List<Lutemon> homeLutemons = new ArrayList<>();
        for (Lutemon l : LutemonStorage.getInstance().getLutemons()) {
            if (l.getLocation() == Lutemon.Location.HOME) {
                homeLutemons.add(l);
            }
        }

        if (homeLutemons.size() < 2) {
            Toast.makeText(getContext(), "You need at least 2 Lutemons at home to battle.", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] names = new String[homeLutemons.size()];
        boolean[] checkedItems = new boolean[homeLutemons.size()];

        for (int i = 0; i < homeLutemons.size(); i++) {
            names[i] = homeLutemons.get(i).getName() + " (" + homeLutemons.get(i).getColor() + ")";
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Select 2 Lutemons for Battle")
                .setMultiChoiceItems(names, checkedItems, (dialog, which, isChecked) -> {
                    int selectedCount = 0;
                    for (boolean checked : checkedItems) {
                        if (checked) selectedCount++;
                    }

                    if (selectedCount > 2) {
                        checkedItems[which] = false;
                        ((androidx.appcompat.app.AlertDialog) dialog).getListView().setItemChecked(which, false);
                        Toast.makeText(getContext(), "Only 2 Lutemons can be selected.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Confirm", (dialog, which) -> {

                    for (Lutemon l : selectedLutemons) {
                        l.setLocation(Lutemon.Location.HOME);
                    }

                    selectedLutemons.clear();

                    for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i]) {
                            Lutemon selected = homeLutemons.get(i);
                            selected.setLocation(Lutemon.Location.BATTLE);
                            selectedLutemons.add(selected);
                        }
                    }

                    if (selectedLutemons.size() == 2) {
                        String summary = selectedLutemons.get(0).getName() + " & " + selectedLutemons.get(1).getName();
                        textSelectedLutemons.setText("Selected: " + summary);
                        updateSelectedDisplay();
                    } else {
                        textSelectedLutemons.setText("Selected: None");
                        updateSelectedDisplay();
                        Toast.makeText(getContext(), "Select exactly 2 Lutemons.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateSelectedDisplay() {
        if (selectedLutemons.size() == 2) {
            Lutemon A = selectedLutemons.get(0);
            Lutemon B = selectedLutemons.get(1);

            textNameA.setText(A.getName());
            textNameB.setText(B.getName());

            imageLutemonA.setImageResource(getImageForColor(A.getColor()));
            imageLutemonB.setImageResource(getImageForColor(B.getColor()));
        } else {
            textNameA.setText("Lutemon A");
            textNameB.setText("Lutemon B");
            imageLutemonA.setImageResource(R.drawable.default_lutemon);
            imageLutemonB.setImageResource(R.drawable.default_lutemon);
        }
    }

    private int getImageForColor(String color) {
        switch (color.toLowerCase()) {
            case "red": return R.drawable.red_lutemon;
            case "blue": return R.drawable.blue_lutemon;
            case "green": return R.drawable.green_lutemon;
            case "pink": return R.drawable.pink_lutemon;
            case "gray": return R.drawable.gray_lutemon;
            case "yellow": return R.drawable.yellow_lutemon;
            default: return R.drawable.default_lutemon;
        }
    }

    //Automatic battle
    private void startBattle() {
        if (selectedLutemons.size() != 2) {
            Toast.makeText(getContext(), "Please select 2 Lutemons first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Lutemon attacker = selectedLutemons.get(0);
        Lutemon defender = selectedLutemons.get(1);
        int attackerHP = attacker.getHp();
        int defenderHP = defender.getHp();
        Random rand = new Random();

        List<String> battleSteps = new ArrayList<>();
        battleSteps.add("Battle between " + attacker.getName() + " and " + defender.getName() + " begins!\n");


        while (true) {
            int atk = attacker.getAtk();
            int min = Math.max(1, atk / 2);
            int max = atk * 2;
            int damage = rand.nextInt(max - min + 1) + min;
            defenderHP -= damage;

            battleSteps.add(attacker.getName() + " hits " + defender.getName() + " for " + damage + " damage.");
            battleSteps.add(defender.getName() + " has " + Math.max(defenderHP, 0) + " HP left.\n");

            if (defenderHP <= 0) {
                battleSteps.add(defender.getName() + " has been defeated!");
                attacker.gainExp(10);

                attacker.addWin();
                defender.addLoss();
                LutemonStorage.getInstance().addBattles();

                battleSteps.add(attacker.getName() + " gains EXP and returns home stronger.");
                attacker.setLocation(Lutemon.Location.HOME);
                defender.setLocation(Lutemon.Location.HOME);
                break;
            }


            Lutemon temp = attacker;
            attacker = defender;
            defender = temp;

            int tempHP = attackerHP;
            attackerHP = defenderHP;
            defenderHP = tempHP;
        }


        textBattleLog.setText("");
        Handler handler = new Handler();
        for (int i = 0; i < battleSteps.size(); i++) {
            int finalI = i;
            handler.postDelayed(() -> {
                textBattleLog.append(battleSteps.get(finalI) + "\n");
                scrollLog.post(() -> scrollLog.fullScroll(View.FOCUS_DOWN));
            }, i * 1000);
        }


        handler.postDelayed(() -> {
            selectedLutemons.clear();
            updateSelectedDisplay();
            textSelectedLutemons.setText("Selected: None");
        }, battleSteps.size() * 1000 + 500);
    }
}