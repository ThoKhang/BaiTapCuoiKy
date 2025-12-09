package com.example.apphoctapchotre.UI.Activity.Account.DangNhap;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.MainActivity;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.GioiThieu.OnboardingActivity;
import com.example.apphoctapchotre.UI.ViewModel.DangNhapOtpViewModel;

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

    private DangNhapOtpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_otp);

        eTextOTPDangNhap = findViewById(R.id.eTextOTPDangNhap);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        textGuiLaiMa = findViewById(R.id.textQuenMatKhau);

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(DangNhapOtpViewModel.class);
        observeViewModel();

        ibtnBack.setOnClickListener(v -> finish());

        textGuiLaiMa.setOnClickListener(v -> {
            if (TEST_MODE) {
                Toast.makeText(this, "Đã gửi lại mã OTP (test mode): 123456", Toast.LENGTH_LONG).show();
            } else {
                viewModel.sendOtp(email);
            }
        });

        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOTPDangNhap.getText().toString().trim();

            if (TEST_MODE) {
                if (otp.equals("123456")) {
                    Toast.makeText(this, "Đăng nhập thành công (test mode)!", Toast.LENGTH_SHORT).show();

                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("isLoggedIn", true)
                            .putString("userEmail", email)
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

            if (otp.isEmpty() || otp.length() != 6 || !otp.matches("\\d+")) {
                Toast.makeText(this, "OTP phải là 6 chữ số!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.verifyOtpAndGetUser(email, otp);
        });
    }

    private void observeViewModel() {
        viewModel.message.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(DangNhapOTP.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.loginSuccess.observe(this, result -> {
            if (result == null) return;

            String token = result.getToken();
            String maNguoiDung = result.getNguoiDung().getMaNguoiDung();

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("userEmail", email)
                    .putString("MA_NGUOI_DUNG", maNguoiDung)
                    .putString("TOKEN", token)   // ⭐ LƯU TOKEN Ở ĐÂY
                    .apply();

            Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
