package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.DiemChiTiet.AdapterDiemChiTiet;
import com.example.apphoctapchotre.model.DiemChiTiet;

import java.util.ArrayList;
import java.util.List;

public class XepHang extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xep_hang);

        // ... Code khác của bạn (setup nút quay lại, ví dụ:)
        // ImageButton nutQuayLai = findViewById(R.id.nutQuayLai);
        // nutQuayLai.setOnClickListener(v -> finish());

        // Thiết lập RecyclerView cho danh sách chi tiết
        RecyclerView danhSachChiTiet = findViewById(R.id.danhSachChiTiet);
        danhSachChiTiet.setLayoutManager(new LinearLayoutManager(this));  // Cuộn dọc

        // Tạo danh sách dữ liệu mẫu (sau này thay bằng dữ liệu từ cơ sở dữ liệu)
        List<DiemChiTiet> danhSachMau = new ArrayList<>();
        danhSachMau.add(new DiemChiTiet(10, "17/11/2025 15:30", "Hoàn thành bài kiểm tra toán"));
        danhSachMau.add(new DiemChiTiet(5, "16/11/2025 10:00", "Đăng nhập hàng ngày"));
        danhSachMau.add(new DiemChiTiet(15, "15/11/2025 18:45", "Chia sẻ kết quả học tập"));

        // Tạo và set adapter
        AdapterDiemChiTiet adapter = new AdapterDiemChiTiet(danhSachMau);
        danhSachChiTiet.setAdapter(adapter);

        ImageView btnBack = findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, TrangChu.class);
            startActivity(intent);
        });
    }
}