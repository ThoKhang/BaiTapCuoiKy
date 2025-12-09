package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class Thuthach100Cau extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuthach100cau);

        // Sửa đúng kiểu view từ XML
        ImageView btnBack = findViewById(R.id.quayLai);
        Button btnPlay = findViewById(R.id.btnPlay);
        TextView btnRank = findViewById(R.id.btnRank);

        // Back → quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Chơi ngay → sang câu hỏi 1
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Thuthach100Cau.this, CauHoi100.class);
            startActivity(intent);
        });

        btnRank.setOnClickListener(v -> {

        });
    }
}
