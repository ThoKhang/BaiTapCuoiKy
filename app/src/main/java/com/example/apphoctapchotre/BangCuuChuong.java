package com.example.apphoctapchotre;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.apphoctapchotre.Activity.CungCo.cungcokienthuc;
import com.example.apphoctapchotre.Activity.OnLuyen.OnLuyen;

public class BangCuuChuong extends AppCompatActivity {

    private TextView tvBangCuuChuong, tvCircleNumber;
    private GridLayout gridNumbers;
    private AppCompatButton selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangcuuchuong);

        tvBangCuuChuong = findViewById(R.id.tvBangCuuChuong);
        tvCircleNumber = findViewById(R.id.tvCircleNumber);
        gridNumbers = findViewById(R.id.gridNumbers);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        for (int i = 0; i < gridNumbers.getChildCount(); i++) {
            View view = gridNumbers.getChildAt(i);
            if (view instanceof AppCompatButton) {
                AppCompatButton btn = (AppCompatButton) view;

                if (i == 0) {
                    selectedButton = btn;
                }

                btn.setOnClickListener(v -> onNumberClicked(btn));
            }
        }
        ImageButton btnLuyenTap = findViewById(R.id.btnLuyenTap);

        btnLuyenTap.setOnClickListener(v -> {
            Intent intent = new Intent(BangCuuChuong.this, OnLuyen.class);
            startActivity(intent);
        });

    }

    private void onNumberClicked(AppCompatButton button) {
        String text = button.getText().toString();
        int number = Integer.parseInt(text);

        tvCircleNumber.setText(String.valueOf(number));

        StringBuilder bang = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            bang.append(i).append(" x ").append(number)
                    .append(" = ").append(i * number).append("\n");
        }
        tvBangCuuChuong.setText(bang.toString().trim());

        if (selectedButton != null) {
            selectedButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.white))
            );
        }

        button.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_blue))
        );

        selectedButton = button;
    }
}
