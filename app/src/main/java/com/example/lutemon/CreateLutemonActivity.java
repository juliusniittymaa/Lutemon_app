package com.example.lutemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateLutemonActivity extends AppCompatActivity {

    private EditText nameInput;
    private String selectedColor = "gray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lutemon);

        nameInput = findViewById(R.id.editTextName);

        findViewById(R.id.btnRed).setOnClickListener(v -> selectedColor = "red");
        findViewById(R.id.btnBlue).setOnClickListener(v -> selectedColor = "blue");
        findViewById(R.id.btnGreen).setOnClickListener(v -> selectedColor = "green");
        findViewById(R.id.btnPink).setOnClickListener(v -> selectedColor = "pink");
        findViewById(R.id.btnYellow).setOnClickListener(v -> selectedColor = "yellow");
        findViewById(R.id.btnGray).setOnClickListener(v -> selectedColor = "gray");

        Button createBtn = findViewById(R.id.btnCreate);
        createBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                nameInput.setError("Enter a name");
                return;
            }

            Lutemon lutemon = new Lutemon(name, selectedColor, 10, 100);
            LutemonStorage.getInstance().addLutemon(lutemon);

            setResult(Activity.RESULT_OK);
            finish();
        });
    }
}