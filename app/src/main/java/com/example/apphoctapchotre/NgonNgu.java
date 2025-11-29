package com.example.apphoctapchotre;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class NgonNgu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngon_ngu);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        RadioButton rbViet = findViewById(R.id.rbViet);
        RadioButton rbAnh = findViewById(R.id.rbAnh);

        rbViet.setChecked(true);
        rbAnh.setChecked(false);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbViet) {
            } else if (checkedId == R.id.rbAnh) {
            }
        });
    }
}
