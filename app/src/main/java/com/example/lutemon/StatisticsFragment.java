package com.example.lutemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Comparator;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private TextView totalLutemonsText, totalBattlesText, totalTrainingText;
    private LinearLayout winsContainer, lossesContainer;

    //Inflates the layout for statistics view tab
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        totalLutemonsText = view.findViewById(R.id.textTotalLutemons);
        totalBattlesText = view.findViewById(R.id.textTotalBattles);
        totalTrainingText = view.findViewById(R.id.textTotalTraining);
        winsContainer = view.findViewById(R.id.containerMostWins);
        lossesContainer = view.findViewById(R.id.containerMostLosses);

        loadStats(inflater, container);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStats(getLayoutInflater(), (ViewGroup) requireView());
    }

    private void loadStats(LayoutInflater inflater, ViewGroup container) {
        List<Lutemon> lutemons = LutemonStorage.getInstance().getLutemons();

        totalLutemonsText.setText("Total Lutemons: " + lutemons.size());
        totalBattlesText.setText("Total battles: " + LutemonStorage.getInstance().getTotalBattles());
        totalTrainingText.setText("Total training time: " + LutemonStorage.getInstance().getTotalTrainingTime());


        winsContainer.removeAllViews();
        lossesContainer.removeAllViews();

        Lutemon mostWins = lutemons.stream().max(Comparator.comparingInt(Lutemon::getWins)).orElse(null);
        Lutemon mostLosses = lutemons.stream().max(Comparator.comparingInt(Lutemon::getLosses)).orElse(null);

        if (mostWins != null) {
            View winCard = inflater.inflate(R.layout.item_lutemon, container, false);
            populateLutemonCard(winCard, mostWins, "Wins: " + mostWins.getWins());
            winsContainer.addView(winCard);
        }

        if (mostLosses != null) {
            View lossCard = inflater.inflate(R.layout.item_lutemon, container, false);
            populateLutemonCard(lossCard, mostLosses, "Losses: " + mostLosses.getLosses());
            lossesContainer.addView(lossCard);
        }
        int seconds = LutemonStorage.getInstance().getTotalTrainingTime();
        String formatted = seconds + " sec";
        if (seconds >= 60) {
            formatted = (seconds / 60) + " min " + (seconds % 60) + " sec";
        }
        totalTrainingText.setText("Total training time: " + formatted);
    }

    //Using the item_lutemon layout to show  the stats
    private void populateLutemonCard(View cardView, Lutemon l, String suffixText) {
        TextView nameText = cardView.findViewById(R.id.textViewName);
        TextView atkText = cardView.findViewById(R.id.textViewAtk);
        TextView hpText = cardView.findViewById(R.id.textViewHp);
        TextView lvlText = cardView.findViewById(R.id.textViewLvl);
        ImageView image = cardView.findViewById(R.id.imageViewColor);
        Button moveBtn = cardView.findViewById(R.id.moveBtn);

        nameText.setText(l.getName() + " (" + suffixText + ")");
        lvlText.setText("Level: " + l.getLvl());
        atkText.setText("ATK: " + l.getAtk());
        hpText.setText("HP: " + l.getHp());
        image.setImageResource(getImageForColor(l.getColor()));
        moveBtn.setVisibility(View.GONE);
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
}