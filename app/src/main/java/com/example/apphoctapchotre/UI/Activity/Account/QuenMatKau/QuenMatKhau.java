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

    private static final boolean TEST_MODE = false;

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
        observeViewModel();

        btnDangNhap.setOnClickListener(v -> {
            String matKhauMoi = eTextMatKhau.getText().toString().trim();
            String nhapLai = eTextNhapLaiMatKhau.getText().toString().trim();

            if (matKhauMoi.isEmpty() || nhapLai.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!matKhauMoi.equals(nhapLai)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TEST_MODE) {
                Toast.makeText(this,
                        "Đặt lại mật khẩu thành công (test mode)!",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(QuenMatKhau.this, GiaoDienDangNhap.class));
                finish();
                return;
            }

            viewModel.resetPassword(email, matKhauMoi);
        });
    }

    private void observeViewModel() {
        viewModel.getMessage().observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getResetSuccess().observe(this, success -> {
            if (success == null) return;
            if (success) {
                startActivity(new Intent(QuenMatKhau.this, GiaoDienDangNhap.class));
                finish();
            }
        });

        // Nếu có loading UI thì observe viewModel.getLoading()
    }
}
