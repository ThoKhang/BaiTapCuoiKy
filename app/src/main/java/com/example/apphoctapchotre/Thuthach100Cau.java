package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Adapter.XepHang.XepHangAdapter;

public class Thuthach100Cau extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuthach100cau);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnPlay = findViewById(R.id.btnPlay);
        TextView btnRank = findViewById(R.id.btnRank);

        // Nút Back: quay lại màn hình trước
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        // Nút Thử ngay → mở màn hình câu hỏi 1/100
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(Thuthach100Cau.this, CauHoi100.class);
            startActivity(intent);
        });

        // Nút Xếp hạng → mở trang xếp hạng
        btnRank.setOnClickListener(v -> {
            Intent intent = new Intent(Thuthach100Cau.this, XepHangAdapter.class);
            startActivity(intent);
        });
    }
}
