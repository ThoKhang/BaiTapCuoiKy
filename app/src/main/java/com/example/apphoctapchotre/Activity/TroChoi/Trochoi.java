package com.example.apphoctapchotre.Activity.TroChoi;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class Trochoi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tro_choi);

        // ==================== NÚT BACK ====================
        ImageView btnBack = findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}