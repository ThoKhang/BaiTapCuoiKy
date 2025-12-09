package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetCoBan;
import com.example.apphoctapchotre.UI.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.ui.DeItem;

import java.util.ArrayList;
import java.util.List;

public class DeNangCao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_nang_cao);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));

        int tongDe=getIntent().getIntExtra("TONG_DE_NANG_CAO",0);
        List<DeItem> list = new ArrayList<>();
        for (int i = 1; i <= tongDe; i++) {
            list.add(new DeItem("Đề nâng cao số " + i, 100, 0, 10));
        }

        rv.setAdapter(new DeAdapter(this, list, R.drawable.gadient_denangcao));
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(DeNangCao.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}
