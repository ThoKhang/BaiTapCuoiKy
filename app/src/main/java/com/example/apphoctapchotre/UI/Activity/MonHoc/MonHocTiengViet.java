package com.example.apphoctapchotre.UI.Activity.MonHoc;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.UI.Adapter.LyThuyetCoBan.LyThuyetAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.ui.LyThuyetItem;

import java.util.ArrayList;
import java.util.List;

// Trong file MonHocTiengViet.java

public class MonHocTiengViet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_tieng_viet);

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rvMHTV);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu
        List<LyThuyetItem> list = new ArrayList<>();list.add(new LyThuyetItem("Chương 1: Từ là gì", 10));
        list.add(new LyThuyetItem("Chương 2: Nguyên âm và phụ âm", 10));
        list.add(new LyThuyetItem("Chương 3: Thanh điệu", 10));
        list.add(new LyThuyetItem("Chương 4: Từ ghép và từ láy", 10));
        list.add(new LyThuyetItem("Chương 5: Câu đơn, câu ghép", 10));
        list.add(new LyThuyetItem("Chương 6: Dấu câu cơ bản", 10));
        list.add(new LyThuyetItem("Chương 7: Từ đồng nghĩa, trái nghĩa", 10));
        list.add(new LyThuyetItem("Chương 8: Viết đoạn văn", 10));
        list.add(new LyThuyetItem("Chương 9: Văn miêu tả", 10));
        list.add(new LyThuyetItem("Chương 10: Ôn tập tổng hợp", 10));

        // Tạo adapter và truyền listener
        LyThuyetAdapter adapter = new LyThuyetAdapter(this, list, position -> {
            // Khi click vào bất kỳ item nào → mở NoiDungTiengViet
            Intent intent = new Intent(MonHocTiengViet.this, NoiDungTiengViet.class);

            // Nếu muốn truyền thông tin chương (ví dụ: tiêu đề, nội dung...)
            intent.putExtra("chuong", list.get(position).getTieuDe());
            intent.putExtra("position", position); // nếu cần

            startActivity(intent);
        });

        rv.setAdapter(adapter);
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(MonHocTiengViet.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }
}