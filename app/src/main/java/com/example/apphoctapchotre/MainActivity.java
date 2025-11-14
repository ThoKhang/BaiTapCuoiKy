package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;  // Thêm import này

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.model.NguoiDung;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvApiResult;  // Thêm biến này

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        tvApiResult = findViewById(R.id.tv_api_result);

        // Ẩn thanh trạng thái và thanh điều hướng
        hideSystemUI();

        // Test API: Gọi API để lấy người dùng và hiển thị trên UI
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getNguoiDung().enqueue(new Callback<List<NguoiDung>>() {
            @Override
            public void onResponse(Call<List<NguoiDung>> call, Response<List<NguoiDung>> response) {
                if (response.isSuccessful()) {
                    List<NguoiDung> nguoiDungList = response.body();
                    if (nguoiDungList != null && !nguoiDungList.isEmpty()) {
                        StringBuilder sb = new StringBuilder("Danh sách Người dùng:\n");
                        for (NguoiDung nd : nguoiDungList) {
                            sb.append("- ").append(nd.getTenDangNhap()).append("\n");  // Append tên đăng nhập
                        }
                        // Cập nhật UI trên main thread (an toàn vì callback chạy trên main)
                        tvApiResult.setText(sb.toString());
                        Log.d("API_RESULT", "Hiển thị thành công: " + nguoiDungList.size() + " người dùng");
                    } else {
                        tvApiResult.setText("Không có dữ liệu người dùng.");
                    }
                } else {
                    tvApiResult.setText("Lỗi API: " + response.code());
                    Log.e("API_ERROR", "Lỗi khi lấy dữ liệu: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NguoiDung>> call, Throwable t) {
                tvApiResult.setText("Lỗi kết nối: " + t.getMessage());
                Log.e("API_ERROR", "Không thể kết nối: " + t.getMessage());
            }
        });

        // Sau 3 giây chuyển sang giao diện đăng nhập
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, GiaoDienDangNhap.class);
            startActivity(intent);
            finish(); // Đóng Splash để không quay lại được
        }, 3000);
    }

    private void hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            // Android 11 (API 30) trở lên
            getWindow().setDecorFitsSystemWindows(false);
            if (getWindow().getInsetsController() != null) {
                getWindow().getInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                getWindow().getInsetsController().setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            // Android 10 trở xuống
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}