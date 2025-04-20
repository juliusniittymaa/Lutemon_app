package com.example.lutemon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.LutemonViewHolder> {

    private List<Lutemon> lutemonList;
    //mode: home, training, battle
    private String mode;

    public LutemonAdapter(List<Lutemon> lutemonList, String mode) {
        this.lutemonList = lutemonList;
        this.mode = mode;
    }

    //Inflate the layout for each lutemon item
    @NonNull
    @Override
    public LutemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lutemon, parent, false);
        return new LutemonViewHolder(view);
    }

    //Bind the lutemons data to the UI
    @Override
    public void onBindViewHolder(@NonNull LutemonViewHolder holder, int position) {
        Lutemon l = lutemonList.get(position);
        Context context = holder.itemView.getContext();

        holder.nameText.setText(l.getName() + " (" + l.getColor() + ")");
        holder.atkText.setText("ATK: " + l.getAtk());
        holder.hpText.setText("HP: " + l.getHp());
        holder.lvlText.setText("Level: " + l.getLvl());

        switch (l.getColor().toLowerCase()) {
            case "red": holder.imageView.setImageResource(R.drawable.red_lutemon); break;
            case "blue": holder.imageView.setImageResource(R.drawable.blue_lutemon); break;
            case "green": holder.imageView.setImageResource(R.drawable.green_lutemon); break;
            case "pink": holder.imageView.setImageResource(R.drawable.pink_lutemon); break;
            case "gray": holder.imageView.setImageResource(R.drawable.gray_lutemon); break;
            case "yellow": holder.imageView.setImageResource(R.drawable.yellow_lutemon); break;
            default: holder.imageView.setImageResource(R.drawable.default_lutemon); break;
        }

        holder.moveBtn.setVisibility(View.VISIBLE);

        if (mode.equals("home")) {
            holder.moveBtn.setText("Send to Training");

            holder.moveBtn.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;

                if (!canSendToTraining()) {
                    Toast.makeText(context, "Training area is full (max 2)", Toast.LENGTH_SHORT).show();
                    return;
                }

                Lutemon lutemon = lutemonList.get(adapterPosition);
                lutemon.setLocation(Lutemon.Location.TRAINING);
                lutemonList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            });

        } else if (mode.equals("training") || mode.equals("battle")) {
            holder.moveBtn.setText("Send Home");

            holder.moveBtn.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;

                Lutemon lutemon = lutemonList.get(adapterPosition);
                lutemon.setLocation(Lutemon.Location.HOME);
                lutemonList.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            });
        } else {
            holder.moveBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lutemonList.size();
    }

    private boolean canSendToTraining() {
        int count = 0;
        for (Lutemon l : LutemonStorage.getInstance().getLutemons()) {
            if (l.getLocation() == Lutemon.Location.TRAINING) {
                count++;
            }
        }
        return count < 2;
    }

    //Holds the views
    public static class LutemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, atkText, hpText, lvlText;
        ImageView imageView;
        Button moveBtn;

        public LutemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textViewName);
            atkText = itemView.findViewById(R.id.textViewAtk);
            hpText = itemView.findViewById(R.id.textViewHp);
            imageView = itemView.findViewById(R.id.imageViewColor);
            moveBtn = itemView.findViewById(R.id.moveBtn);
            lvlText = itemView.findViewById(R.id.textViewLvl);
        }
    }
}