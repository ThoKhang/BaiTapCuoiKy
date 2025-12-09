package com.example.apphoctapchotre.UI.Activity.Account.DangNhap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau.QuenMatKhauOTP;
import com.example.apphoctapchotre.UI.ViewModel.GiaoDienDangNhapViewModel;

public class GiaoDienDangNhap extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau;
    private Button btnDangNhap;
    private TextView textQuenMatKhau, textDangKyNgay;

    private GiaoDienDangNhapViewModel viewModel;
    private String currentEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_dang_nhap);
        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        textQuenMatKhau = findViewById(R.id.textQuenMatKhau);
        textDangKyNgay = findViewById(R.id.textDangKyNgay);
        viewModel = new ViewModelProvider(this).get(GiaoDienDangNhapViewModel.class);
        // Observe toast messages
        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(GiaoDienDangNhap.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        // Observe login success to navigate to OTP screen
        viewModel.loginSuccess.observe(this, success -> {
            if (success != null && success) {
                openOtpScreen(currentEmail);
            }
        });
        //quenmatkhau
        textQuenMatKhau.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            Intent intent = new Intent(GiaoDienDangNhap.this, QuenMatKhauOTP.class);
            if (!email.isEmpty()) {
                intent.putExtra("EMAIL", email);
            }
            startActivity(intent);
        });
        // Register
        textDangKyNgay.setOnClickListener(v ->
                startActivity(new Intent(GiaoDienDangNhap.this, DangKy.class))
        );
        // Login
        btnDangNhap.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String password = eTextMatKhau.getText().toString().trim();
            currentEmail = email;

            viewModel.login(email, password);
        });
    }
    private void openOtpScreen(String email) {
        Intent intent = new Intent(GiaoDienDangNhap.this, DangNhapOTP.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
        finish();
    }
}
