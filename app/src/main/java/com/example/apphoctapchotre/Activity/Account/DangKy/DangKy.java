package com.example.apphoctapchotre.Activity.Account.DangKy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.NguoiDung;

public class DangKy extends AppCompatActivity {
    private EditText eTextEmail, eTextMatKhau, eTextNhapLaiMatKhau;
    private Button btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        eTextEmail = findViewById(R.id.eTextEmail);
        eTextMatKhau = findViewById(R.id.eTextMatKhau);
        eTextNhapLaiMatKhau = findViewById(R.id.eTextNhapLaiMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangKy.setOnClickListener(v -> {
            String email = eTextEmail.getText().toString().trim();
            String matKhau = eTextMatKhau.getText().toString().trim();
            String nhapLaiMatKhau = eTextNhapLaiMatKhau.getText().toString().trim();
            if (email.isEmpty()||matKhau.isEmpty()||nhapLaiMatKhau.isEmpty())
            {
                Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin !",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!matKhau.equals(nhapLaiMatKhau))
            {
                Toast.makeText(this,"Mật khẩu không trùng !",Toast.LENGTH_SHORT).show();
                return;
            }
            NguoiDung user = new NguoiDung();



        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}