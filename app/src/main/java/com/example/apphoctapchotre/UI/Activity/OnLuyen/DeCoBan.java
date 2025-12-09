package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetCoBan;
import com.example.apphoctapchotre.UI.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.ui.DeItem;

import java.util.ArrayList;
import java.util.List;

public class DeCoBan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_co_ban);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));

        int tongDe=getIntent().getIntExtra("TONG_DE_CO_BAN",0);
        List<DeItem> list = new ArrayList<>();
        for (int i = 1; i <= tongDe; i++) {
            list.add(new DeItem("Đề cơ bản số " + i, 50, 0, 10));
        }

        DeAdapter adapter = new DeAdapter(this, list, R.drawable.gadient_decoban);
        rv.setAdapter(adapter);
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(DeCoBan.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}