package com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.UI.ViewModel.QuenMatKhauViewModel;
import com.google.android.material.button.MaterialButton;

public class QuenMatKhau extends AppCompatActivity {

    private EditText eTextMatKhau, eTextNhapLaiMatKhau;
    private MaterialButton btnDangNhap;
    private String email;

    private QuenMatKhauViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());

        email = getIntent().getStringExtra("EMAIL");
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Lỗi: Không nhận được email!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(QuenMatKhauViewModel.class);

        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(QuenMatKhau.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.resetSuccess.observe(this, success -> {
            if (success != null && success) {
                startActivity(new Intent(QuenMatKhau.this, GiaoDienDangNhap.class));
                finish();
            }
        });

        btnDangNhap.setOnClickListener(v -> {
            String newPass = eTextMatKhau.getText().toString().trim();
            String confirmPass = eTextNhapLaiMatKhau.getText().toString().trim();
            viewModel.resetPassword(email, newPass, confirmPass);
        });
    }
}
