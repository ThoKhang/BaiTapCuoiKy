package com.example.apphoctapchotre.Activity.MonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.LyThuyetCoBan.LyThuyetAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.LyThuyetItem;

import java.util.ArrayList;
import java.util.List;

public class MonHocToan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_toan);

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rvMHT);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu môn Toán
        List<LyThuyetItem> list = new ArrayList<>();
        list.add(new LyThuyetItem("Chương 1: Phép cộng cơ bản", 10));
        list.add(new LyThuyetItem("Chương 2: Phép trừ cơ bản", 10));
        list.add(new LyThuyetItem("Chương 3: Phép nhân cơ bản", 10));
        list.add(new LyThuyetItem("Chương 4: Phép chia cơ bản", 10));
        list.add(new LyThuyetItem("Chương 5: Đơn vị đo chiều dài", 10));
        list.add(new LyThuyetItem("Chương 6: Đơn vị đo khối lượng", 50));
        list.add(new LyThuyetItem("Chương 7: Đơn vị đo thời gian", 10));
        list.add(new LyThuyetItem("Chương 8: Hình tam giác", 10));
        list.add(new LyThuyetItem("Chương 9: Hình tròn", 10));
        list.add(new LyThuyetItem("Chương 10: Hình vuông", 10));

        // Tạo adapter + xử lý click
        LyThuyetAdapter adapter = new LyThuyetAdapter(this, list, position -> {
            Intent intent;

            // Cách 1: Dùng chung activity NoiDungTiengViet (đơn giản nhất)
            intent = new Intent(MonHocToan.this, NoiDungTiengViet.class);

            // Cách 2: Nếu bạn muốn tạo riêng cho Toán (khuyên làm sau này)
            // intent = new Intent(MonHocToan.this, NoiDungToan.class);

            // Truyền dữ liệu để sau này hiển thị nội dung phù hợp
            intent.putExtra("tieu_de", list.get(position).getTieuDe());
            intent.putExtra("chuong_so", position + 1); // Chương 1, 2,...
            intent.putExtra("mon_hoc", "Toán"); // Để phân biệt môn nếu dùng chung Activity

            startActivity(intent);
        });

        rv.setAdapter(adapter);
    }
}