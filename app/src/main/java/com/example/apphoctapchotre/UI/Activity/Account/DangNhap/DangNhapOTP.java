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
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.NguoiDung;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.GioiThieu.OnboardingActivity;
import com.example.apphoctapchotre.UI.ViewModel.DangNhapOtpViewModel;

public class DangNhapOTP extends AppCompatActivity {

    private EditText eTextOTPDangNhap;
    private Button btnDangNhapVaoTrangChu;
    private ImageButton ibtnBack;
    private TextView textGuiLaiMa;
    private String email;

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

        // Observe messages
        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(DangNhapOTP.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        // Observe login success
        viewModel.loginSuccess.observe(this, this::onLoginSuccess);
        ibtnBack.setOnClickListener(v -> finish());
        textGuiLaiMa.setOnClickListener(v -> viewModel.resendOtp(email));
        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOTPDangNhap.getText().toString().trim();
            viewModel.verifyOtp(email, otp);
        });
    }
    private void onLoginSuccess(NguoiDung nguoiDung) {
        if (nguoiDung == null) return;
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("isLoggedIn", true)
                .putString("userEmail", email)
                .putString("MA_NGUOI_DUNG", nguoiDung.getMaNguoiDung())
                .apply();
        Intent intent = new Intent(DangNhapOTP.this, OnboardingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
