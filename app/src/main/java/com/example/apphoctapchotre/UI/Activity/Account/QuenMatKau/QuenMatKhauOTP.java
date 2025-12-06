package com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuenMatKhauOTP extends AppCompatActivity {

    private EditText eTextEmail, eTextOTP;
    private TextView textOTP;                // Nút "Gửi OTP về email!"
    private MaterialButton btnTiepTuc;

    // ĐỂ DÙNG SERVER THẬT -> ĐỂ false
    private static final boolean TEST_MODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau_otp);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextOTP = findViewById(R.id.eTextOTP);
        textOTP = findViewById(R.id.textOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());

        TextView textDangKyNgay = findViewById(R.id.textDangKyNgay);
        textDangKyNgay.setOnClickListener(v -> startActivity(new Intent(this, DangKy.class)));

        // Nhận email từ màn đăng nhập (nếu có)
        String emailFromLogin = getIntent().getStringExtra("EMAIL");
        if (emailFromLogin != null && !emailFromLogin.isEmpty()) {
            eTextEmail.setText(emailFromLogin);
        }

        // ====================== NÚT GỬI OTP ======================
        textOTP.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TEST MODE: chỉ giả lập, không gọi server
            if (TEST_MODE) {
                Toast.makeText(this, "Đã gửi OTP (test mode): 123456", Toast.LENGTH_LONG).show();
                return;
            }

            // Code gọi API thật
            Map<String, String> request = new HashMap<>();
            request.put("email", email);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.sendOtp(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhauOTP.this,
                                "Đã gửi OTP đến email của bạn!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuenMatKhauOTP.this,
                                "Email không tồn tại hoặc gửi OTP thất bại!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhauOTP.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        // ====================== NÚT TIẾP TỤC (XÁC THỰC OTP) ======================
        btnTiepTuc.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String otp = eTextOTP.getText().toString().trim();

            if (email.isEmpty() || otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TEST MODE: OTP cố định 123456
            if (TEST_MODE) {
                if (otp.equals("123456")) {
                    Toast.makeText(this,
                            "Xác thực OTP thành công (test mode)!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this,
                            "OTP sai! Trong test mode phải nhập: 123456",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            // Code gọi API thật
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("otp", otp);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            apiService.verifyOTP(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(QuenMatKhauOTP.this,
                                "Xác thực OTP thành công!",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(QuenMatKhauOTP.this,
                                "OTP không đúng hoặc đã hết hạn!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(QuenMatKhauOTP.this,
                            "Lỗi kết nối: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
