package com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan.LienHoanTinhToan;

public class GioiThieuLienHoanTinhToan extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView quayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_lien_hoan_tinh_toan);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        quayLai=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhapDapAnLienHoanTinhToan.class);
            startActivity(intent);
        });
        quayLai.setOnClickListener(v -> {
            finish();
        });
    }
}

