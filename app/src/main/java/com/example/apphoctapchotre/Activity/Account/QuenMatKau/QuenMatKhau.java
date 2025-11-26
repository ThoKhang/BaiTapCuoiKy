package com.example.apphoctapchotre.Activity.Account.QuenMatKau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
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
    private String email;

    // ĐỂ DÙNG SERVER THẬT -> ĐỂ false
    private static final boolean TEST_MODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không nhận được email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnDangNhap.setOnClickListener(v -> {
            String matKhauMoi = eTextMatKhau.getText().toString().trim();
            String nhapLai = eTextNhapLaiMatKhau.getText().toString().trim();

            if (matKhauMoi.isEmpty() || nhapLai.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhauMoi.equals(nhapLai)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TEST MODE: giả lập đặt mật khẩu thành công
            if (TEST_MODE) {
                Toast.makeText(this,
                        "Đặt lại mật khẩu thành công (test mode)!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QuenMatKhau.this, GiaoDienDangNhap.class));
                finish();
                return;
            }

            // Code gọi API thật
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("newPassword", matKhauMoi);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.resetPassword(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhau.this,
                                "Đặt lại mật khẩu thành công!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(QuenMatKhau.this, GiaoDienDangNhap.class));
                        finish();
                    } else {
                        String err = "Không thể đặt lại mật khẩu!";
                        try {
                            if (response.errorBody() != null) {
                                err = response.errorBody().string();
                            }
                        } catch (Exception ignored) {}

                        Toast.makeText(QuenMatKhau.this,
                                "Lỗi: " + err,
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhau.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
