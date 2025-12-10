package com.example.apphoctapchotre.UI.Activity.Account.DangKy;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.UI.ViewModel.DangKyOtpViewModel;
import com.google.android.material.button.MaterialButton;

public class DangKyOTP extends AppCompatActivity {

    private EditText eTextOtpDangKy;
    private MaterialButton btnDangNhapVaoTrangChu;
    private ImageButton ibtnBack;
    private String email;
    private DangKyOtpViewModel viewModel;
    private String currentOtp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky_otp);
        // Ánh xạ view
        eTextOtpDangKy = findViewById(R.id.eTextOtpDangKy);
        btnDangNhapVaoTrangChu = findViewById(R.id.btnDangNhapVaoTrangChu);
        ibtnBack = findViewById(R.id.ibtnBack);
        // Lấy email
        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không nhận được email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // ViewModel
        viewModel = new ViewModelProvider(this).get(DangKyOtpViewModel.class);
        // Observe message
        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(DangKyOTP.this, msg, Toast.LENGTH_LONG).show();
            }
        });
        // Observe verify success
        viewModel.verifySuccess.observe(this, success -> {
            if (success != null && success) {
                // Xác thực OTP ok → chuyển sang màn đăng nhập
                openLoginScreen(email);
            }
        });
        // Click xác thực OTP
        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            currentOtp = eTextOtpDangKy.getText().toString().trim();
            viewModel.verifyOtp(email, currentOtp);
        });
        // Back
        ibtnBack.setOnClickListener(v -> finish());

        // Giữ lại phần insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void openLoginScreen(String email) {
        Intent intent = new Intent(DangKyOTP.this, GiaoDienDangNhap.class);
        intent.putExtra("EMAIL", email); // nếu muốn fill sẵn email
        startActivity(intent);
        finish();
    }
}
