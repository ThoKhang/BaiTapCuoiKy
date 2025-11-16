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

public class MonHocToan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_toan);

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Vương miện (nếu cần xử lý click)
        ImageView imgVuongMieng = findViewById(R.id.imgVuongMieng);
        // imgVuongMieng.setOnClickListener(...);

        // Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rvMHT);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu: 10 chương lý thuyết
        List<LyThuyetItem> list = new ArrayList<>();
        list.add(new LyThuyetItem("Chương 1: Phép cộng cơ bản", 10));
        list.add(new LyThuyetItem("Chương 2: Phép trừ cơ bản", 10));
        list.add(new LyThuyetItem("Chương 3: Phép nhân cơ bản", 10));
        list.add(new LyThuyetItem("Chương 4: phép chia cơ bản", 10));
        list.add(new LyThuyetItem("Chương 5: Đơn vị đo chiều dài", 10));
        list.add(new LyThuyetItem("Chương 6: Đơn vị đo khối lượng", 50));
        list.add(new LyThuyetItem("Chương 7: Đơn vị đo thời gian", 10));
        list.add(new LyThuyetItem("Chương 8: Hình tam giác", 10));
        list.add(new LyThuyetItem("Chương 9: Hình tròn", 10));
        list.add(new LyThuyetItem("Chương 10: Hình vuông", 10));


        LyThuyetAdapter adapter = new LyThuyetAdapter(this, list);
        rv.setAdapter(adapter);
    }
}