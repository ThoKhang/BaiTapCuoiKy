package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.ui.DeItem;
import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetCoBan;
import com.example.apphoctapchotre.UI.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;

import java.util.ArrayList;
import java.util.List;

public class DeTrungBinh extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_trung_binh);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<DeItem> list = new ArrayList<>();
        list.add(new DeItem("Đề trung bình số 1", 80, 9, 10));
        for (int i = 2; i <= 10; i++) {
            list.add(new DeItem("Đề trung bình số " + i, 80, 0, 10));
        }

        rv.setAdapter(new DeAdapter(this, list, R.drawable.gadient_detrungbinh));
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(DeTrungBinh.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}
