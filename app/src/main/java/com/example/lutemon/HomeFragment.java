package com.example.lutemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    private static final int REQUEST_CODE_CREATE = 101;
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;
    private List<Lutemon> homeLutemons;

    //Inflates the layout for home screen tab
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button createBtn = view.findViewById(R.id.btnCreateLutemon);
        createBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateLutemonActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHomeLutemons();
    }

    //Loads the lutemons that are at home
    private void loadHomeLutemons() {
        homeLutemons = LutemonStorage.getInstance().getLutemons().stream()
                .filter(l -> l.getLocation() == Lutemon.Location.HOME)
                .collect(Collectors.toList());

        adapter = new LutemonAdapter(homeLutemons, "home");
        recyclerView.setAdapter(adapter);
    }
}