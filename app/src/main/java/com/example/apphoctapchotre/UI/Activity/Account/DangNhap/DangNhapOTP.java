package com.example.apphoctapchotre.UI.Activity.Account.DangNhap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.UI.Activity.GioiThieu.OnboardingActivity;
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
    private TextView textGuiLaiMa;
    private String email;

    private static final boolean TEST_MODE = false;

    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_otp);

        eTextOTPDangNhap = findViewById(R.id.eTextOTPDangNhap);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        textGuiLaiMa = findViewById(R.id.textQuenMatKhau);

        api = RetrofitClient.getClient().create(ApiService.class);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ibtnBack.setOnClickListener(v -> finish());

        textGuiLaiMa.setOnClickListener(v -> {
            if (TEST_MODE) {
                Toast.makeText(this, "Đã gửi lại mã OTP (test mode): 123456", Toast.LENGTH_LONG).show();
            } else {
                Map<String, String> request = new HashMap<>();
                request.put("email", email);
                api.sendOtp(request).enqueue(new Callback<ResponseBody>() {
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

        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOTPDangNhap.getText().toString().trim();

            // TEST MODE
            if (TEST_MODE) {
                if (otp.equals("123456")) {
                    Toast.makeText(this, "Đăng nhập thành công (test mode)!", Toast.LENGTH_SHORT).show();

                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("isLoggedIn", true)
                            .putString("userEmail", email)
                            // test mode nếu cần có thể hard-code MA_NGUOI_DUNG
                            .putString("MA_NGUOI_DUNG", "ND004")
                            .apply();

                    Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "OTP sai! Trong test mode phải nhập: 123456", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // MODE THẬT
            if (otp.isEmpty() || otp.length() != 6 || !otp.matches("\\d+")) {
                Toast.makeText(this, "OTP phải là 6 chữ số!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> request = new HashMap<>();
            request.put("email", email);
            request.put("otp", otp);

            api.verifyOTP(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {

                            String message = response.body().string().trim();
                            Toast.makeText(DangNhapOTP.this, message, Toast.LENGTH_SHORT).show();

                            // ✅ Sau khi OTP đúng → gọi API getByEmail để lấy MaNguoiDung
                            Map<String, String> body = new HashMap<>();
                            body.put("email", email);
                            api.getByEmail(body).enqueue(new Callback<NguoiDung>() {
                                @Override
                                public void onResponse(Call<NguoiDung> call, Response<NguoiDung> resUser) {
                                    if (!resUser.isSuccessful() || resUser.body() == null) {
                                        Toast.makeText(DangNhapOTP.this, "Không lấy được thông tin người dùng!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    NguoiDung nd = resUser.body();

                                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                    prefs.edit()
                                            .putBoolean("isLoggedIn", true)
                                            .putString("userEmail", email)
                                            .putString("MA_NGUOI_DUNG", nd.getMaNguoiDung())
                                            .apply();

                                    Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<NguoiDung> call, Throwable t) {
                                    Toast.makeText(DangNhapOTP.this, "Lỗi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            String message = response.errorBody() != null
                                    ? response.errorBody().string().trim()
                                    : "Lỗi xác thực OTP!";
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
