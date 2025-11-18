package com.example.apphoctapchotre;

import android.os.Bundle;
import android.widget.ImageView; // Import ImageView cho nút quay lại

import androidx.appcompat.app.AppCompatActivity;

public class TracNghiem extends AppCompatActivity {
    private ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        backButton = findViewById(R.id.quayLai);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
    }

}