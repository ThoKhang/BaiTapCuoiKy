package com.example.apphoctapchotre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.apphoctapchotre.Adapter.KhoaHocGioiThieuAdapter;
import com.example.apphoctapchotre.model.KhoaHocGioiThieu;

import java.util.ArrayList;
import java.util.List;

public class GioiThieuActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachKhoaHoc;
    private KhoaHocGioiThieuAdapter adapter;
    private List<KhoaHocGioiThieu> danhSachKhoaHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioithieu_khoahoc);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        rvDanhSachKhoaHoc = findViewById(R.id.rvCourses);
        rvDanhSachKhoaHoc.setLayoutManager(new LinearLayoutManager(this));

        danhSachKhoaHoc = new ArrayList<>();

        danhSachKhoaHoc.add(new KhoaHocGioiThieu("Toán lớp 1", 4.5f, R.drawable.thuthachoc));
        danhSachKhoaHoc.add(new KhoaHocGioiThieu("Toán lớp 2", 4.4f, R.drawable.thuthachoc));
        danhSachKhoaHoc.add(new KhoaHocGioiThieu("Toán lớp 3", 4.4f, R.drawable.thuthachoc));
        danhSachKhoaHoc.add(new KhoaHocGioiThieu("Toán lớp 4", 4.6f, R.drawable.thuthachoc));
        danhSachKhoaHoc.add(new KhoaHocGioiThieu("Toán lớp 5", 4.6f, R.drawable.thuthachoc));

        adapter = new KhoaHocGioiThieuAdapter(danhSachKhoaHoc);
        rvDanhSachKhoaHoc.setAdapter(adapter);
    }
}
