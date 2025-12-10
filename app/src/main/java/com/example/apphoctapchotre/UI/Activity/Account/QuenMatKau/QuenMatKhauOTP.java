package com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.UI.Activity.Account.DangKy.DangKy;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Account.QuenMatKau.QuenMatKhau;
import com.example.apphoctapchotre.UI.ViewModel.QuenMatKhauOtpViewModel;
import com.google.android.material.button.MaterialButton;

public class QuenMatKhauOTP extends AppCompatActivity {

    private EditText eTextEmail, eTextOTP;
    private TextView textOTP;
    private MaterialButton btnTiepTuc;

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
        textDangKyNgay.setOnClickListener(v ->
                startActivity(new Intent(this, DangKy.class))
        );

        String emailFromLogin = getIntent().getStringExtra("EMAIL");
        if (emailFromLogin != null && !emailFromLogin.isEmpty()) {
            eTextEmail.setText(emailFromLogin);
        }

        viewModel = new ViewModelProvider(this).get(QuenMatKhauOtpViewModel.class);

        viewModel.toastMessage.observe(this, msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(QuenMatKhauOTP.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.verifySuccess.observe(this, success -> {
            if (success != null && success) {
                String email = eTextEmail.getText().toString().trim();
                Intent intent = new Intent(QuenMatKhauOTP.this, QuenMatKhau.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();
            }
        });

        textOTP.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            viewModel.sendOtp(email);
        });

        btnTiepTuc.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String otp = eTextOTP.getText().toString().trim();
            viewModel.verifyOtp(email, otp);
        });
    }
}
