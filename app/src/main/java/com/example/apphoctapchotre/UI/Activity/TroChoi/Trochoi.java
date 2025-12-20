package com.example.apphoctapchotre.UI.Activity.TroChoi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.UI.Activity.Games.sudoku;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.TroChoi.HoanThienCauTu.HoanThienCauTu;
import com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan.LienHoanTinhToan;
import com.example.apphoctapchotre.UI.Activity.TroChoi.TrumTinhNham.TrumTinhNham;

public class Trochoi extends AppCompatActivity {
    private LinearLayout LNHoanThienCT;
    private LinearLayout LNTrumTinhNham;
    private LinearLayout LNLhTinhToan;

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
        LNHoanThienCT=findViewById(R.id.LNHoanThienCT);
        LNHoanThienCT.setOnClickListener(v -> {
            Intent intent = new Intent(this, HoanThienCauTu.class);
            startActivity(intent);
        });
        LNTrumTinhNham=findViewById(R.id.LNTrumTinhNham);
        LNTrumTinhNham.setOnClickListener(v -> {
            Intent intent = new Intent(this, TrumTinhNham.class);
            startActivity(intent);
        });

        LNLhTinhToan=findViewById(R.id.LNLhTinhToan);
        LNLhTinhToan.setOnClickListener(v -> {
            Intent intent = new Intent(this, LienHoanTinhToan.class);
            startActivity(intent);
        });
    }
}