package com.example.apphoctapchotre.UI.Activity.TroChoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.UI.Activity.Games.sudoku;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetCoBan;

public class Trochoi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tro_choi);

        // ==================== NÚT BACK ====================
        ImageView btnBack = findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> finish());
        //gamessudoku
        LinearLayout lnSudoku = findViewById(R.id.lnSudoku);
        lnSudoku.setOnClickListener(v -> {
            Intent intent = new Intent(this, sudoku.class);
            startActivity(intent);
        });
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(Trochoi.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}