package com.example.apphoctapchotre.Activity.MonHoc;

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

public class MonHocTiengViet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_tieng_viet);

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Vương miện (nếu cần xử lý click)
        ImageView imgVuongMieng = findViewById(R.id.imgVuongMieng);
        // imgVuongMieng.setOnClickListener(...);

        // Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rvMHTV);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu: 10 chương lý thuyết
        List<LyThuyetItem> list = new ArrayList<>();
        list.add(new LyThuyetItem("Chương 1: Bảng chữ cái", 10));
        list.add(new LyThuyetItem("Chương 2: Nguyên âm và phụ âm", 10));
        list.add(new LyThuyetItem("Chương 3: Thanh điệu", 10));
        list.add(new LyThuyetItem("Chương 4: Từ ghép và từ láy", 10));
        list.add(new LyThuyetItem("Chương 5: Câu đơn, câu ghép", 10));
        list.add(new LyThuyetItem("Chương 6: Dấu câu cơ bản", 50));
        list.add(new LyThuyetItem("Chương 7: Từ đồng nghĩa, trái nghĩa", 10));
        list.add(new LyThuyetItem("Chương 8: Viết đoạn văn", 10));
        list.add(new LyThuyetItem("Chương 9: Văn miêu tả", 10));
        list.add(new LyThuyetItem("Chương 10: Ôn tập tổng hợp", 10));

        // Adapter cho Lý Thuyết (không cần bgResource)
        LyThuyetAdapter adapter = new LyThuyetAdapter(this, list);
        rv.setAdapter(adapter);
    }
}