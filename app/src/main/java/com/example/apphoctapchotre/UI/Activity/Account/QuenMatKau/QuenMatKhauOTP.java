package com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.UI.ViewModel.QuenMatKhauOtpViewModel;
import com.google.android.material.button.MaterialButton;

public class QuenMatKhauOTP extends AppCompatActivity {

    private EditText eTextEmail, eTextOTP;
    private TextView textOTP;
    private MaterialButton btnTiepTuc;

    private static final boolean TEST_MODE = false;

    private QuenMatKhauOtpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau_otp);

        eTextEmail = findViewById(R.id.eTextEmail);
        eTextOTP = findViewById(R.id.eTextOTP);
        textOTP = findViewById(R.id.textOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> finish());

        TextView textDangKyNgay = findViewById(R.id.textDangKyNgay);
        textDangKyNgay.setOnClickListener(v -> startActivity(new Intent(this, DangKy.class)));

        // Nhận email từ màn đăng nhập (nếu có)
        String emailFromLogin = getIntent().getStringExtra("EMAIL");
        if (emailFromLogin != null && !emailFromLogin.isEmpty()) {
            eTextEmail.setText(emailFromLogin);
        }

        viewModel = new ViewModelProvider(this).get(QuenMatKhauOtpViewModel.class);
        observeViewModel();

        // ===== NÚT GỬI OTP =====
        textOTP.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TEST_MODE) {
                Toast.makeText(this, "Đã gửi OTP (test mode): 123456", Toast.LENGTH_LONG).show();
                return;
            }

            viewModel.sendOtp(email);
        });

        // ===== NÚT TIẾP TỤC (XÁC THỰC OTP) =====
        btnTiepTuc.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String otp = eTextOTP.getText().toString().trim();

            if (email.isEmpty() || otp.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TEST_MODE) {
                if (otp.equals("123456")) {
                    Toast.makeText(this,
                            "Xác thực OTP thành công (test mode)!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                    intent.putExtra("EMAIL", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this,
                            "OTP sai! Trong test mode phải nhập: 123456",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            viewModel.verifyOtp(email, otp);
        });
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
                String email = eTextEmail.getText().toString().trim();
                Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();
            }
        });

        // Nếu có progress bar có thể observe viewModel.getLoading()
    }
}
