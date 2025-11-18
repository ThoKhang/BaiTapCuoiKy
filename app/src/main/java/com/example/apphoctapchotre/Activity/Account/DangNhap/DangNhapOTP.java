package com.example.apphoctapchotre.Activity.Account.DangNhap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.Activity.Account.QuenMatKau.QuenMatKhauOTP;
import com.example.apphoctapchotre.OnboardingActivity;
import com.example.apphoctapchotre.R;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangNhapOTP extends AppCompatActivity {

    private EditText eTextOTPDangNhap;
    private Button btnDangNhapVaoTrangChu;
    private ImageButton ibtnBack;
    private TextView textGuiLaiMa; // Nút "Gửi lại mã"
    private String email;

    // <<< CHẾ ĐỘ TEST - ĐỔI THÀNH false ĐỂ DÙNG SERVER THẬT >>>
    private static final boolean TEST_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_otp);

        eTextOTPDangNhap = findViewById(R.id.eTextOTPDangNhap);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        textGuiLaiMa = findViewById(R.id.textQuenMatKhau); // thường là "Gửi lại mã"

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Nút quay lại
        ibtnBack.setOnClickListener(v -> finish());

        // GỬI LẠI MÃ (test mode sẽ không gọi API)
        textGuiLaiMa.setOnClickListener(v -> {
            if (TEST_MODE) {
                Toast.makeText(this, "Đã gửi lại mã OTP (test mode): 123456", Toast.LENGTH_LONG).show();
            } else {
                // code gửi lại thật (giữ nguyên nếu cần)
                Map<String, String> request = new HashMap<>();
                request.put("email", email);
                RetrofitClient.getClient().create(ApiService.class)
                        .sendOTP(request)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    String msg = response.body() != null ? response.body().string() : "Không nhận được phản hồi!";
                                    Toast.makeText(DangNhapOTP.this, msg, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(DangNhapOTP.this, "Lỗi gửi lại mã!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(DangNhapOTP.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // XÁC NHẬN OTP
        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOTPDangNhap.getText().toString().trim();

            // ================== CHẾ ĐỘ TEST TĨNH ==================
            if (TEST_MODE) {
                if (otp.equals("123456")) {
                    Toast.makeText(this, "Đăng nhập thành công (test mode)!", Toast.LENGTH_SHORT).show();

                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userEmail", email);
                    editor.apply();

                    Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "OTP sai! Trong test mode phải nhập: 123456", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // =======================================================

            // Code thật (giữ nguyên phần cũ của bạn)
            if (otp.isEmpty() || otp.length() != 6 || !otp.matches("\\d+")) {
                Toast.makeText(this, "OTP phải là 6 chữ số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("otp", otp);

            RetrofitClient.getClient().create(ApiService.class)
                    .verifyOTP(request)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String message = "";
                                if (response.isSuccessful() && response.body() != null) {
                                    message = response.body().string().trim();
                                    Toast.makeText(DangNhapOTP.this, message, Toast.LENGTH_SHORT).show();

                                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("userEmail", email);
                                    editor.apply();

                                    Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    message = response.errorBody() != null ? response.errorBody().string().trim() : "Lỗi xác thực OTP!";
                                    Toast.makeText(DangNhapOTP.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(DangNhapOTP.this, "Lỗi xử lý phản hồi!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(DangNhapOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}