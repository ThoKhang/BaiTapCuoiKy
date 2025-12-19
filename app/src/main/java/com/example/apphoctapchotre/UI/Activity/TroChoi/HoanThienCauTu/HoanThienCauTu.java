package com.example.apphoctapchotre.UI.Activity.TroChoi.HoanThienCauTu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apphoctapchotre.R;

public class HoanThienCauTu extends AppCompatActivity {
    private Button btnThuNgay;
    private ImageView ibtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoan_thien_cau_tu);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        ibtnBack=findViewById(R.id.ibtnBack);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, GioiThieuHoanThienCauTu.class);
            startActivity(intent);
        });
        ibtnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
