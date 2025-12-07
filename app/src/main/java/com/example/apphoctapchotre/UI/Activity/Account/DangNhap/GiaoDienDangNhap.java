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
import com.example.apphoctapchotre.UI.ViewModel.DangNhapViewModel;
import com.example.apphoctapchotre.DATA.model.KetQuaDangNhap;

public class GiaoDienDangNhap extends AppCompatActivity {

    private EditText eTextEmail, eTextMatKhau;
    private Button btnDangNhap;
    private DangNhapViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_dang_nhap);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        TextView textQuenMatKhau = findViewById(R.id.textQuenMatKhau);
        TextView textDangKyNgay = findViewById(R.id.textDangKyNgay);

        viewModel = new ViewModelProvider(this).get(DangNhapViewModel.class);

        viewModel.getLoading().observe(this, isLoading ->
                btnDangNhap.setEnabled(!Boolean.TRUE.equals(isLoading))
        );

        viewModel.getDangNhapResult().observe(this, this::xuLyDangNhap);

        textQuenMatKhau.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            Intent intent = new Intent(this, QuenMatKhauOTP.class);
            if (!email.isEmpty()) intent.putExtra("EMAIL", email);
            startActivity(intent);
        });

        textDangKyNgay.setOnClickListener(v ->
                startActivity(new Intent(this, DangKy.class))
        );

        btnDangNhap.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();

            if (email.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(this, "Nhap email va mat khau!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.dangNhap(email, matKhau);
        });
    }

    private void xuLyDangNhap(KetQuaDangNhap result) {
        if (result == null) return;

        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();

        if (result.isSuccess()) {
            Intent intent = new Intent(this, DangNhapOTP.class);
            intent.putExtra("EMAIL", result.getEmail());
            startActivity(intent);
            finish();
        }
    }
}
