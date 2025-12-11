package com.example.apphoctapchotre.UI.Activity.TroChoi.HoanThienCauTu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apphoctapchotre.NhapDapAnHoanThienCauTu;
import com.example.apphoctapchotre.R;

public class GioiThieuHoanThienCauTu extends AppCompatActivity {
    private Button btnThuNgay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_hoan_thien_cau_tu);

        btnThuNgay = findViewById(R.id.btnThuNgay);
        btnThuNgay.setOnClickListener(v -> {
            Intent intent = new Intent(this, NhapDapAnHoanThienCauTu.class);
            startActivity(intent);
        });
    }
}
