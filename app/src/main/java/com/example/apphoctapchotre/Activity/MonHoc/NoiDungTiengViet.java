package com.example.apphoctapchotre.Activity.MonHoc;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;

public class NoiDungTiengViet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noi_dung_tv);

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
    }
}
