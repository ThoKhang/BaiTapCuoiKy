package com.example.apphoctapchotre.UI.Activity.TroChoi.HoanThienCauTu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class GioiThieuHoanThienCauTu extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView quayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_hoan_thien_cau_tu);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        quayLai=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhapDapAnHoanThienCauTu.class);
            startActivity(intent);
        });
        quayLai.setOnClickListener(v -> {
            finish();
        });
    }
}
