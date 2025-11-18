package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


public class CaiDat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        ImageButton btnBack = findViewById(R.id.btnBack);
        LinearLayout itemGioiThieu = findViewById(R.id.itemGioiThieu);
        LinearLayout itemThongBao = findViewById(R.id.itemThongBao);
        LinearLayout itemNgonNgu = findViewById(R.id.itemNgonNgu);
        LinearLayout itemChiaSe = findViewById(R.id.itemChiaSe);

        btnBack.setOnClickListener(v -> onBackPressed());

        itemGioiThieu.setOnClickListener(v ->
                startActivity(new Intent(this, GioiThieuKHViewPager.class))
        );

        itemThongBao.setOnClickListener(v ->
                startActivity(new Intent(this, ThongBao.class))
        );

        itemNgonNgu.setOnClickListener(v ->
                startActivity(new Intent(this, NgonNgu.class))
        );

        itemChiaSe.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hãy thử ứng dụng này!");
            startActivity(Intent.createChooser(share, "Chia sẻ qua"));
        });
    }
}
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CaiDat extends AppCompatActivity {
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cai_dat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v->{
            Intent intent = new Intent(this, TrangChu.class);
            startActivity(intent);
        });
    }
}
