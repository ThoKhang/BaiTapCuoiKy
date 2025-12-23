package com.example.apphoctapchotre.UI.Activity.TroChoi.TrumTinhNham;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.TroChoi.HoanThienCauTu.GioiThieuHoanThienCauTu;

public class TrumTinhNham extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trum_tinh_nham);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        ibtnBack=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, GioiThieuTrumTinhNham.class);
            startActivity(intent);
        });
        ibtnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
