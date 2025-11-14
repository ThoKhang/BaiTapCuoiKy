package com.example.apphoctapchotre.Activity.OnLuyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apphoctapchotre.R;

public class OnLuyen extends AppCompatActivity {
    private Button btnDeCoBan,btndeTrungBinh,btnDeNangCao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_luyen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnDeCoBan=findViewById(R.id.btnDeCoBan);
        btndeTrungBinh=findViewById(R.id.btnDeTrungBinh);
        btnDeNangCao=findViewById(R.id.btnDeNangCao);
        btnDeCoBan.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeCoBan.class);
            startActivity(intent);
        });
        btndeTrungBinh.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeTrungBinh.class);
            startActivity(intent);
        });
        btnDeNangCao.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeNangCao.class);
            startActivity(intent);
        });
    }
}