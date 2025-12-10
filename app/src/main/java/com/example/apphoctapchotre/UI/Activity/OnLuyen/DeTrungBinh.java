package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.DATA.model.ui.DeItem;
import com.example.apphoctapchotre.UI.Adapter.OnLuyen.DeAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.TienTrinhViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeTrungBinh extends AppCompatActivity {
    private TienTrinhViewModel tienTrinhViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_trung_binh);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> startActivity(new Intent(this, OnLuyen.class)));
        RecyclerView rv = findViewById(R.id.rvDe);
        rv.setLayoutManager(new LinearLayoutManager(this));
        int tongDe = getIntent().getIntExtra("TONG_DE_TRUNG_BINH", 0);
        List<DeItem> list = new ArrayList<>();
        DeAdapter adapter = new DeAdapter(this, list, R.drawable.gadient_detrungbinh);
        rv.setAdapter(adapter);
        tienTrinhViewModel = new ViewModelProvider(this).get(TienTrinhViewModel.class);
        tienTrinhViewModel.loadData(email, "Ôn TB");
        tienTrinhViewModel.listTienTrinh.observe(this, data -> {
            list.clear();
            for (int i = 1; i <= tongDe; i++) {
                String tenDeServer = "Ôn TB " + i;
                int soCauDung = 0;
                if (data != null) {
                    for (TienTrinh tt : data) {
                        if (tenDeServer.equals(tt.getTieuDe())) {
                            soCauDung = tt.getSoCauDung();
                            break;
                        }
                    }
                }
                list.add(new DeItem("Đề trung bình số " + i, 50, soCauDung, 10));
            }
            adapter.notifyDataSetChanged();
        });
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(DeTrungBinh.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}
