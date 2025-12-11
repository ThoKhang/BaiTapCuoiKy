package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class event extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Sửa đúng kiểu view từ XML
        ImageView btnBack = findViewById(R.id.quayLai);
        Button btnPlay = findViewById(R.id.btnPlay);

        // Back → quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Chơi ngay → sang câu hỏi 1
        btnPlay.setOnClickListener(v -> {
            Intent intent = new Intent(event.this, CauHoiThuThach.class);
            startActivity(intent);
        });
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(event.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });

    }
}
