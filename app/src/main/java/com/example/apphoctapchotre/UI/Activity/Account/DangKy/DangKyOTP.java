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
    private String email;

    private DangKyOtpViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(DangKyOtpViewModel.class);
        observeViewModel();

        btnDangNhapVaoTrangChu.setOnClickListener(v -> {
            String otp = eTextOtpDangKy.getText().toString().trim();

            if (otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.xacThucOtpDangKy(email, otp);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());
    }

    private void observeViewModel() {
        viewModel.getMessage().observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getOtpSuccess().observe(this, success -> {
            if (success == null) return;
            if (success) {
                // Xác thực OK → chuyển sang màn đăng nhập
                Intent intent = new Intent(DangKyOTP.this, GiaoDienDangNhap.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();
            }
        });

        // Nếu có progress bar thì observe loading ở đây
        // viewModel.getLoading().observe(this, isLoading -> {...});
    }
}
