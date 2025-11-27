package com.example.apphoctapchotre.Activity.Account.DangKy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class DangKy extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau, eTextNhapLaiMatKhau;
    private MaterialButton btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);

        btnDangKy.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = eTextNhapLaiMatKhau.getText().toString().trim();

            if (email.isEmpty() || matKhau.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhau.equals(nhapLaiMatKhau)) {
                Toast.makeText(this, "Mật khẩu không trùng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuẩn bị body gửi lên backend
            Map<String, String> body = new HashMap<>();
            // Backend yêu cầu tenDangNhap, tạm dùng email làm tên đăng nhập
            body.put("tenDangNhap", email);
            body.put("email", email);
            body.put("matKhau", matKhau);

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.register(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DangKy.this,
                                "Đăng ký thành công! Vui lòng kiểm tra email để lấy OTP.",
                                Toast.LENGTH_LONG).show();

                        // 👉 Chuyển sang màn Đăng ký OTP, mang theo email
                        Intent intent = new Intent(DangKy.this, DangKyOTP.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    } else {
                        String err = "Đăng ký thất bại!";
                        try {
                            if (response.errorBody() != null) {
                                err = response.errorBody().string();
                            }
                        } catch (Exception ignored) {}

                        Toast.makeText(DangKy.this,
                                "Lỗi: " + err,
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DangKy.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());
    }
}
