package com.example.apphoctapchotre.Activity.XepHang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;  // <<< THÊM DÒNG NÀY

import com.example.apphoctapchotre.Adapter.XepHang.XepHangAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.TrangChu;
import com.example.apphoctapchotre.model.XepHangItem;

import java.util.ArrayList;
import java.util.List;

public class XepHang extends AppCompatActivity {

    // ===== KHAI BÁO BIẾN Ở ĐÂY =====
    private RecyclerView recyclerXepHang;
    private XepHangAdapter adapter;
    // ===============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xep_hang);

        // Ánh xạ và setup RecyclerView
        recyclerXepHang = findViewById(R.id.recyclerXepHang);
        recyclerXepHang.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu giống hệt hình bạn gửi
        List<XepHangItem> list = new ArrayList<>();
        list.add(new XepHangItem("USERNAME_01", 42500));
        list.add(new XepHangItem("USERNAME_02", 35100));
        list.add(new XepHangItem("USERNAME_03", 29305));
        list.add(new XepHangItem("USERNAME_04", 25260));
        list.add(new XepHangItem("USERNAME_05", 19250));
        list.add(new XepHangItem("USERNAME_06", 16890));
        list.add(new XepHangItem("USERNAME_07", 15020));
        list.add(new XepHangItem("USERNAME_08", 11000));
        list.add(new XepHangItem("USERNAME_09", 10561));

        // Gán adapter
        adapter = new XepHangAdapter(list);
        recyclerXepHang.setAdapter(adapter);

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v->{
            Intent intent = new Intent(this, TrangChu.class);
            startActivity(intent);
        });
    }
}