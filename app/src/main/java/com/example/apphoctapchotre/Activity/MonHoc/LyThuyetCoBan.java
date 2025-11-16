package com.example.apphoctapchotre.Activity.MonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apphoctapchotre.Activity.TrangChu.TrangChu1;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.TrangChu;

public class LyThuyetCoBan extends AppCompatActivity {

    // SỬA: Dùng View thay vì Button
    private View btnTiengViet, btnToan ,btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ly_thuyet_co_ban);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // GÁN ĐÚNG KIỂU
        btnTiengViet = findViewById(R.id.btnTiengViet);
        btnToan = findViewById(R.id.btnToan);


        btnTiengViet.setOnClickListener(v -> {
            startActivity(new Intent(this, MonHocTiengViet.class));
        });

        btnToan.setOnClickListener(v -> {
            startActivity(new Intent(this, MonHocToan.class));
        });

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
    }
}