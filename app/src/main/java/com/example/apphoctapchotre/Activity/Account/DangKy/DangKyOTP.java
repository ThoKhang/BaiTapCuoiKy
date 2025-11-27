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

public class DangKyOTP extends AppCompatActivity {

    private EditText eTextOtpDangKy;
    private MaterialButton btnDangNhapVaoTrangChu;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky_otp);

        eTextOtpDangKy = findViewById(R.id.eTextOtpDangKy);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không nhận được email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOtpDangKy.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("otp", otp);

            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.verifyOTP(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DangKyOTP.this,
                                "Xác thực OTP thành công! Bạn có thể đăng nhập.",
                                Toast.LENGTH_LONG).show();

                        // 👉 Sau khi xác thực thành công, chuyển sang màn đăng nhập
                        Intent intent = new Intent(DangKyOTP.this, GiaoDienDangNhap.class);
                        // Nếu muốn fill sẵn email ở màn đăng nhập:
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(DangKyOTP.this,
                                "OTP không đúng hoặc đã hết hạn!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DangKyOTP.this,
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
