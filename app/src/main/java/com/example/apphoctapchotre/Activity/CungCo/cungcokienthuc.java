// File: CungCoKienThucActivity.java
// Package: com.example.apphoctapchotre (giả sử package của bạn)
// Mục đích: Activity này hiển thị danh sách các bài kiểm tra củng cố kiến thức theo môn học.
// Nó sử dụng Retrofit để gọi API lấy list BaiKiemTra, sau đó hiển thị trong ListView.
// Giả sử bạn truyền maMonHoc từ Intent (ví dụ: 1 cho Toán). Nếu không, có thể hardcode hoặc chọn từ UI.

package com.example.apphoctapchotre.Activity.CungCo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.BaiKiemTra;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class cungcokienthuc extends AppCompatActivity {
    private ListView listBaiKiemTra;
    private TextView tvBack;
    private byte maMonHoc = 1;  // Giả sử mặc định là Toán (1), bạn có thể truyền từ Intent hoặc chọn từ UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cungcokienthuc);  // Layout XML bạn cung cấp, nhưng cần thêm ListView

        // Ánh xạ các view
        tvBack = findViewById(R.id.back);
        listBaiKiemTra = findViewById(R.id.listBaiKiemTra);  // Giả sử bạn thêm ListView id=listBaiKiemTra vào layout XML

        // Xử lý nút back
        tvBack.setOnClickListener(v -> finish());

        // Lấy maMonHoc từ Intent nếu có
        if (getIntent().hasExtra("maMonHoc")) {
            maMonHoc = getIntent().getByteExtra("maMonHoc", (byte)1);
        }

    }

}