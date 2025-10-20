package com.example.apphoctapchotre;

import android.content.Context;
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
    private TextView textQuenMatKhau;  // Làm nút "Gửi lại OTP?"
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_otp);  // Tên layout XML

        // Lấy view từ layout
        eTextOTPDangNhap = findViewById(R.id.eTextOTPDangNhap);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        textQuenMatKhau = findViewById(R.id.textQuenMatKhau);

        // Nhận email từ Intent
        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thay text "Quên mật khẩu?" thành "Gửi lại OTP?" (nếu cần resend, thêm code sau)
        textQuenMatKhau.setText("Gửi lại OTP?");  // Tùy chọn, nếu có API resend

        // Sự kiện nhấn nút Xác thực OTP
        btnDangNhapVaoTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy OTP từ EditText
                String otp = eTextOTPDangNhap.getText().toString().trim();

                // Validate: Phải 6 số
                if (otp.isEmpty()) {
                    Toast.makeText(DangNhapOTP.this, "Vui lòng nhập OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (otp.length() != 6 || !otp.matches("\\d+")) {
                    Toast.makeText(DangNhapOTP.this, "OTP phải là 6 chữ số!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gọi API verify OTP
                Map<String, String> request = new HashMap<>();
                request.put("email", email);
                request.put("otp", otp);

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<ResponseBody> call = apiService.verifyOTP(request);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Đọc body message từ backend (động, ưu tiên backend)
                            String successMsg = "";
                            try {
                                if (response.body() != null) {
                                    successMsg = response.body().string();  // Message từ ResponseEntity.ok()
                                    if (successMsg.isEmpty()) {
                                        successMsg = "Xác thực OTP thành công! Chào mừng đến trang chủ.";  // Fallback nếu rỗng
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("OTP_MSG", "Lỗi đọc body: " + e.getMessage());
                                successMsg = "Xác thực OTP thành công! Chào mừng đến trang chủ.";  // Fallback nếu lỗi
                            }
                            Toast.makeText(DangNhapOTP.this, successMsg, Toast.LENGTH_SHORT).show();

                            // Sửa: Lưu trạng thái đăng nhập trước khi chuyển
                            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userEmail", email);
                            editor.apply();

                            // Chuyển sang màn hình TrangChu1 (thêm flags để clear stack cũ)
                            Intent intent = new Intent(DangNhapOTP.this, TrangChu1.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Clear back stack
                            startActivity(intent);
                            finish();
                        } else {
                            // Lỗi từ server: Đọc error body từ backend
                            String errorMsg = "";
                            try {
                                if (response.errorBody() != null) {
                                    errorMsg = response.errorBody().string();  // Message từ ResponseEntity.badRequest()
                                    if (errorMsg.isEmpty()) {
                                        errorMsg = "OTP sai hoặc hết hạn! Vui lòng thử lại.";  // Fallback nếu rỗng
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("OTP_ERROR", "Lỗi đọc error body: " + e.getMessage());
                                errorMsg = "OTP sai hoặc hết hạn! Vui lòng thử lại.";  // Fallback nếu lỗi
                            }
                            Toast.makeText(DangNhapOTP.this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Lỗi kết nối
                        Toast.makeText(DangNhapOTP.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("OTP_FAILURE", "Lỗi kết nối verify OTP: " + t.getMessage());
                    }
                });
            }
        });

        // Sự kiện nhấn nút Back (quay lại đăng nhập)
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapOTP.this, GiaoDienDangNhap.class);
                startActivity(intent);
                finish();
            }
        });

        // Sự kiện click "Gửi lại OTP?" (nếu cần resend)
        textQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi API send-otp (nếu có)
                Map<String, String> request = new HashMap<>();
                request.put("email", email);
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<ResponseBody> call = apiService.sendOTP(request);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DangNhapOTP.this, "OTP mới đã gửi đến email!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DangNhapOTP.this, "Lỗi gửi OTP! Thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(DangNhapOTP.this, "Lỗi kết nối khi gửi OTP!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}