package com.example.apphoctapchotre.UI.Activity.TroChoi.TrumTinhNham;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.TroChoi.TrumTinhNham.NhapDapAnTrumTinhNham;

public class GioiThieuTrumTinhNham extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView quayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_trum_tinh_nham);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        quayLai=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhapDapAnTrumTinhNham.class);
            startActivity(intent);
        });
        quayLai.setOnClickListener(v -> {
            finish();
        });
    }
}

