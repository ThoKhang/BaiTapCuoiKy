package com.example.apphoctapchotre.UI.Activity.Account.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau.QuenMatKhauOTP;
import com.example.apphoctapchotre.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiaoDienDangNhap extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau;
    private Button btnDangNhap;

    // <<< CHẾ ĐỘ TEST - ĐỔI THÀNH false ĐỂ DÙNG SERVER THẬT >>>
    private static final boolean TEST_MODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_dang_nhap);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        // Quên mật khẩu
        TextView textQuenMatKhau = findViewById(R.id.textQuenMatKhau);
        textQuenMatKhau.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            Intent intent = new Intent(GiaoDienDangNhap.this, QuenMatKhauOTP.class);
            if (!email.isEmpty()) {
                intent.putExtra("EMAIL", email);
            }
            startActivity(intent);
        });

        // Đăng ký
        TextView textDangKyNgay = findViewById(R.id.textDangKyNgay);
        textDangKyNgay.setOnClickListener(v -> {
            startActivity(new Intent(GiaoDienDangNhap.this, DangKy.class));
        });

        btnDangNhap.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();

            if (email.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // BODY gửi đúng theo backend
            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("matKhau", matKhau);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.login(body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {

                            String message = response.body().string();
                            Toast.makeText(GiaoDienDangNhap.this, message, Toast.LENGTH_SHORT).show();

                            // Chuyển sang OTP
                            Intent intent = new Intent(GiaoDienDangNhap.this, DangNhapOTP.class);
                            intent.putExtra("EMAIL", email);
                            startActivity(intent);
                            finish();
                        } else {
                            String message = response.errorBody() != null
                                    ? response.errorBody().string()
                                    : "Đăng nhập thất bại!";
                            Toast.makeText(GiaoDienDangNhap.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("LOGIN_ERROR", "Lỗi xử lý phản hồi: " + e.getMessage());
                        Toast.makeText(GiaoDienDangNhap.this, "Lỗi đọc phản hồi từ server!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(GiaoDienDangNhap.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}