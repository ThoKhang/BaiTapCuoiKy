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
import com.example.apphoctapchotre.UI.ViewModel.DangKyViewModel;
import com.google.android.material.button.MaterialButton;

public class DangKy extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau, eTextNhapLaiMatKhau;
    private MaterialButton btnDangKy;
    private ImageButton ibtnBack;

    private DangKyViewModel viewModel;
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        // ==== ÁNH XẠ VIEW ====
        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        ibtnBack = findViewById(R.id.ibtnBack);
        // ==== VIEWMODEL ====
        viewModel = new ViewModelProvider(this).get(DangKyViewModel.class);
        // ==== OBSERVE ====
        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(DangKy.this, msg, Toast.LENGTH_LONG).show();
            }
        });
        viewModel.registerSuccess.observe(this, success -> {
            if (success != null && success) {
                // Đăng ký OK -> chuyển sang DangKyOTP, mang theo email
                openDangKyOtpScreen(currentEmail);
            }
        });
        // ==== SỰ KIỆN CLICK ====
        btnDangKy.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = eTextNhapLaiMatKhau.getText().toString().trim();

            currentEmail = email;
            viewModel.dangKy(email, matKhau, nhapLaiMatKhau);
        });
        ibtnBack.setOnClickListener(v -> finish());
        // Giữ lại phần xử lý insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void openDangKyOtpScreen(String email) {
        Intent intent = new Intent(DangKy.this, DangKyOTP.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
        finish();
    }
}
