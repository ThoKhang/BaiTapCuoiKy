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
    private DangKyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);

        viewModel = new ViewModelProvider(this).get(DangKyViewModel.class);
        observeViewModel();

        btnDangKy.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = eTextNhapLaiMatKhau.getText().toString().trim();

            // Validate basic
            if (email.isEmpty() || matKhau.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhau.equals(nhapLaiMatKhau)) {
                Toast.makeText(this, "Mật khẩu không trùng!", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.dangKy(email, matKhau);
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
        viewModel.getDangKyResult().observe(this, result -> {
            if (result == null) return;

            Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();

            if (result.isSuccess()) {
                // Đăng ký OK → chuyển sang màn OTP, mang theo email
                Intent intent = new Intent(DangKy.this, DangKyOTP.class);
                intent.putExtra("EMAIL", result.getEmail());
                startActivity(intent);
                finish();
            }
        });

        // Nếu bạn có progress bar thì observe loading ở đây
        // viewModel.getLoading().observe(this, isLoading -> {...});
    }
}
