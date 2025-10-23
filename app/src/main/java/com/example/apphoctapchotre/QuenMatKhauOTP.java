package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauOTP extends AppCompatActivity {

    private EditText eTextEmail, eTextOTP;
    private TextView textOTP;
    private com.google.android.material.button.MaterialButton btnTiepTuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau_otp);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextOTP = findViewById(R.id.eTextOTP);
        textOTP = findViewById(R.id.textOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textDangKyNgay = findViewById(R.id.textDangKyNgay);
        textDangKyNgay.setOnClickListener(v -> {
            Intent intent = new Intent(QuenMatKhauOTP.this, DangKy.class);
            startActivity(intent);
        });
        // Nếu từ màn đăng nhập truyền email qua, thì hiển thị luôn
        Intent intent = getIntent();
        String emailFromLogin = intent.getStringExtra("EMAIL");
        if (emailFromLogin != null) {
            eTextEmail.setText(emailFromLogin);
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // 1️⃣ Khi nhấn "Gửi OTP về email!"
        textOTP.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> request = new HashMap<>();
            request.put("email", email);

            Call<ResponseBody> call = apiService.forgotPassword(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhauOTP.this, "Đã gửi OTP đến email của bạn!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuenMatKhauOTP.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhauOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FORGOT_OTP", "Error: " + t.getMessage());
                }
            });
        });

        // 2️⃣ Khi nhấn "Tiếp tục"
        btnTiepTuc.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String otp = eTextOTP.getText().toString().trim();

            if (email.isEmpty() || otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API verify-otp để kiểm tra OTP
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("otp", otp);

            Call<ResponseBody> call = apiService.verifyOTP(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhauOTP.this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn QuenMatKhau (nhập mật khẩu mới)
                        Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                        intent.putExtra("EMAIL", email);

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(QuenMatKhauOTP.this, "OTP không đúng hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhauOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VERIFY_OTP", "Error: " + t.getMessage());
                }
            });
        });
    }
}
