package com.example.apphoctapchotre.Activity.MonHoc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Adapter.LyThuyetCoBan.LyThuyetAdapter;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.LyThuyetItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonHocToan extends AppCompatActivity {

    private RecyclerView rvMHT;
    private LyThuyetAdapter adapter;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc_toan);

        // Lấy email từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        email = prefs.getString("userEmail", "");

        if (email.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
        }

        // Nút back
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        // Setup RecyclerView
        rvMHT = findViewById(R.id.rvMHT);
        rvMHT.setLayoutManager(new LinearLayoutManager(this));

        // Gọi API lấy danh sách bài học môn Toán
        loadDanhSachToan();
    }

    private void loadDanhSachToan() {
        Call<List<LyThuyetItemResponse>> call = RetrofitClient.getClient()
                .create(ApiService.class)
                .getDanhSach(email, "MH001"); 

        call.enqueue(new Callback<List<LyThuyetItemResponse>>() {
            @Override
            public void onResponse(Call<List<LyThuyetItemResponse>> call, Response<List<LyThuyetItemResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LyThuyetItemResponse> list = response.body();

                    adapter = new LyThuyetAdapter(MonHocToan.this, list, position -> {
                        LyThuyetItemResponse item = list.get(position);
                        Intent intent = new Intent(MonHocToan.this, NoiDungLyThuyet.class);

                        // Truyền đúng dữ liệu mà NoiDungLyThuyet cần
                        intent.putExtra("maHoatDong", item.getMaHoatDong());
                        intent.putExtra("email", email);

                        startActivityForResult(intent, 999);
                    });

                    rvMHT.setAdapter(adapter);
                } else {
                    Toast.makeText(MonHocToan.this, "Không tải được dữ liệu Toán!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LyThuyetItemResponse>> call, Throwable t) {
                Toast.makeText(MonHocToan.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            loadDanhSachToan(); // Refresh lại danh sách
            Toast.makeText(this, "Hoàn thành! +10 điểm", Toast.LENGTH_SHORT).show();
        }
    }
}