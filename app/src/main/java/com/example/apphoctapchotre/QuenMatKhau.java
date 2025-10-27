package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhau extends AppCompatActivity {

    private EditText eTextMatKhau, eTextNhapLaiMatKhau;
    private MaterialButton btnDangNhap;
    private ImageButton ibtnBack;
    private String email; // nhận từ QuenMatKhauOTP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        ibtnBack = findViewById(R.id.ibtnBack);
        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 🔹 Nhận email & OTP đã xác thực từ màn QuenMatKhauOTP
        email = getIntent().getStringExtra("EMAIL");

        btnDangNhap.setOnClickListener(v -> {
            String matKhauMoi = eTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = eTextNhapLaiMatKhau.getText().toString().trim();

            if (matKhauMoi.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhauMoi.equals(nhapLaiMatKhau)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔹 Gọi API reset password
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Map<String, String> request = new HashMap<>();
            request.put("email", email);

            request.put("newPassword", matKhauMoi);

            Call<ResponseBody> call = apiService.resetPassword(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhau.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(QuenMatKhau.this, GiaoDienDangNhap.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                            Toast.makeText(QuenMatKhau.this, "Lỗi: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(QuenMatKhau.this, "Không thể đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhau.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
