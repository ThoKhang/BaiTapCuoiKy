package com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan.GioiThieuLienHoanTinhToan;

public class LienHoanTinhToan extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_hoan_tinh_toan);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        ibtnBack=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, GioiThieuLienHoanTinhToan.class);
            startActivity(intent);
        });
        ibtnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
