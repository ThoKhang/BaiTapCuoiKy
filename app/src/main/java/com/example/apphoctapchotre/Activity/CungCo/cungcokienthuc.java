package com.example.apphoctapchotre.Activity.CungCo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class cungcokienthuc extends AppCompatActivity {

    private TextView tvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cungcokienthuc);

        // Ánh xạ nút Back (đúng ID bạn cung cấp)
        tvBack = findViewById(R.id.back);

        // Kiểm tra null để tránh lỗi
        if (tvBack != null) {
            tvBack.setOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();  // đảm bảo Activity đóng đúng
    }
}
