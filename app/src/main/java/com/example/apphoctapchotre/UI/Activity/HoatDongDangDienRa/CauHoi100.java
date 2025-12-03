package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class CauHoi100 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_100cauhoi);

        // Nút Back: quay lại màn hình trước
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}
