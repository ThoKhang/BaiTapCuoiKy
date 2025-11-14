package com.example.apphoctapchotre.Activity.Account.DangNhap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.Activity.Account.QuenMatKau.QuenMatKhauOTP;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.Activity.TrangChu.TrangChu1;
import com.example.apphoctapchotre.TrangChu;

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
    private TextView textQuenMatKhau;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_otp);

        eTextOTPDangNhap = findViewById(R.id.eTextOTPDangNhap);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        textQuenMatKhau = findViewById(R.id.textQuenMatKhau);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView textQuenMatKhau = findViewById(R.id.textQuenMatKhau);
        textQuenMatKhau.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapOTP.this, QuenMatKhauOTP.class);
            startActivity(intent);
        });
        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 👉 Xác thực OTP
        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOTPDangNhap.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập OTP!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (otp.length() != 6 || !otp.matches("\\d+")) {
                Toast.makeText(this, "OTP phải là 6 chữ số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("otp", otp);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.verifyOTP(request);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String message = "";

                        if (response.isSuccessful() && response.body() != null) {

                            // Đọc body *CHỈ 1 LẦN*
                            message = response.body().string().trim();
                            Log.d("OTP_RESPONSE", "Body: " + message);

                            Toast.makeText(DangNhapOTP.this, message, Toast.LENGTH_SHORT).show();

                            // Lưu trạng thái đăng nhập
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userEmail", email);
                            editor.apply();

                            // Chuyển trang
                            Intent intent = new Intent(DangNhapOTP.this, TrangChu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            // Chỉ đọc errorBody 1 lần
                            if (response.errorBody() != null) {
                                message = response.errorBody().string().trim();
                            } else {
                                message = "Lỗi xác thực OTP!";
                            }

                            Toast.makeText(DangNhapOTP.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.e("OTP_RESPONSE", "Lỗi xử lý phản hồi: " + e.getMessage());
                        Toast.makeText(DangNhapOTP.this, "Lỗi xử lý phản hồi OTP!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DangNhapOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OTP_FAILURE", "Lỗi kết nối verify OTP: " + t.getMessage());
                }
            });
        });

        // 👉 Nút quay lại
        ibtnBack.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapOTP.this, GiaoDienDangNhap.class);
            startActivity(intent);
            finish();
        });

        // 👉 Gửi lại OTP
        textQuenMatKhau.setOnClickListener(v -> {
            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<ResponseBody> call = apiService.sendOTP(request);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String msg = (response.body() != null)
                                ? response.body().string()
                                : "Không nhận được phản hồi từ máy chủ!";
                        Toast.makeText(DangNhapOTP.this, msg, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(DangNhapOTP.this, "Lỗi đọc phản hồi!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(DangNhapOTP.this, "Lỗi kết nối khi gửi OTP!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
