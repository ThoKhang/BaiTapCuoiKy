package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        hideSystemUI();

        // =======================================
        // 🔥 KIỂM TRA KẾT NỐI BACKEND BẰNG /api/ping
        // =======================================
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.pingServer().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String result = response.body().string();   // Đọc raw text
                        Log.d("PING_STATUS", "🔥 Backend OK: " + result);
                    } catch (Exception e) {
                        Log.e("PING_STATUS", "❌ Lỗi đọc dữ liệu: " + e.getMessage());
                    }
                } else {
                    Log.e("PING_STATUS", "❌ Backend trả lỗi HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("PING_STATUS", "❌ Không thể kết nối backend: " + t.getMessage());
            }
        });


        // =======================================
        // ⏳ CHUYỂN SANG MÀN HÌNH ĐĂNG NHẬP SAU 3 GIÂY
        // =======================================
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, GiaoDienDangNhap.class));
            finish();
        }, 3000);
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
    }
}
