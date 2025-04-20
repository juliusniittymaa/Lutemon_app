package com.example.lutemon;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

public class TrainingFragment extends Fragment {
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;
    private Handler handler = new Handler();
    private List<Lutemon> trainingLutemons;

    //Inflates the layout for training view tab
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTraining);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTrainingLutemons();
        startTrainingLoop();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrainingLutemons();
    }

    //Loads the lutemons that are in training
    private void loadTrainingLutemons() {
        trainingLutemons = LutemonStorage.getInstance().getLutemons().stream()
                .filter(l -> l.getLocation() == Lutemon.Location.TRAINING)
                .collect(Collectors.toList());

        adapter = new LutemonAdapter(trainingLutemons, "training");
        recyclerView.setAdapter(adapter);
    }

    //Loop that runs every second
    //Adds training seconds for the lutemons and gives EXP
    private void startTrainingLoop() {
        handler.postDelayed(() -> {

            boolean anyTrained = false;

            for (Lutemon l : LutemonStorage.getInstance().getLutemons()) {
                if (l.getLocation() == Lutemon.Location.TRAINING) {
                    l.addTrainingSecond();
                    if (l.getTrainingSeconds() % 5 == 0) {
                        l.gainExp(1);
                    }
                    anyTrained = true;
                }
            }

            if (anyTrained) {
                LutemonStorage.getInstance().addTrainingTime();
            }

            adapter.notifyDataSetChanged();
            startTrainingLoop();
        }, 1000);
    }
}
