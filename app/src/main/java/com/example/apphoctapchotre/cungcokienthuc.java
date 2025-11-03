// File: CungCoKienThucActivity.java
// Package: com.example.apphoctapchotre (giả sử package của bạn)
// Mục đích: Activity này hiển thị danh sách các bài kiểm tra củng cố kiến thức theo môn học.
// Nó sử dụng Retrofit để gọi API lấy list BaiKiemTra, sau đó hiển thị trong ListView.
// Giả sử bạn truyền maMonHoc từ Intent (ví dụ: 1 cho Toán). Nếu không, có thể hardcode hoặc chọn từ UI.

package com.example.apphoctapchotre;

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

        loadListBaiKiemTra();
    }

    private void loadListBaiKiemTra() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getBaiKiemTraCungCo(maMonHoc).enqueue(new Callback<List<BaiKiemTra>>() {
            @Override
            public void onResponse(Call<List<BaiKiemTra>> call, Response<List<BaiKiemTra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> titles = new ArrayList<>();
                    List<BaiKiemTra> baiList = response.body();
                    for (BaiKiemTra b : baiList) {
                        titles.add(b.getTieuDe());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(cungcokienthuc.this, android.R.layout.simple_list_item_1, titles);
                    listBaiKiemTra.setAdapter(adapter);

                    // Xử lý click item để chuyển sang làm bài
                    listBaiKiemTra.setOnItemClickListener((parent, view, position, id) -> {
                        int maBaiKiemTra = baiList.get(position).getMaBaiKiemTra();
                        Intent intent = new Intent(cungcokienthuc.this, cungcokienthuc.class);
                        intent.putExtra("maBaiKiemTra", maBaiKiemTra);
                        intent.putExtra("maNguoiDung", /* Lấy từ SharedPreferences hoặc session, ví dụ */ 1);
                        startActivity(intent);
                    });
                } else {
                    Log.e("API_ERROR", "Code: " + response.code()
                            + " | Message: " + response.message());

                    Log.e("API_ERROR", "Error body: " + response.errorBody());
                    Log.e("DEBUG_PARAM", "maMonHoc gửi lên: " + maMonHoc);

                    Toast.makeText(cungcokienthuc.this, "Lỗi tải danh sách bài kiểm tra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BaiKiemTra>> call, Throwable t) {
                Toast.makeText(cungcokienthuc.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}