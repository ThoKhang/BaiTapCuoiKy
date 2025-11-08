// File: TrangChu1.java
// Package: com.example.apphoctapchotre
// Mục đích: Activity trang chủ sau đăng nhập. Đã cập nhật để xử lý click TextView vaoHocCungCoKienThuc để chuyển sang CungCoKienThucActivity (hiển thị list củng cố kiến thức).
// Giả sử bạn đã có CungCoKienThucActivity như code trước. Nếu cần truyền maMonHoc, thêm putExtra.

package com.example.apphoctapchotre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TrangChu1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu1);  // Layout trang chủ của bạn

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String userEmail = prefs.getString("userEmail", "");

        if (!isLoggedIn || userEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GiaoDienDangNhap.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  // Clear stack cũ
            startActivity(intent);
            finish();
            return;
        }

        // Hiển thị UI trang chủ (ví dụ: TextView chào user)
        TextView tvWelcome = findViewById(R.id.tvWelcome);  // Giả sử có TextView ID này trong layout
        if (tvWelcome != null) {
            tvWelcome.setText("Chào mừng: " + userEmail);
        }

        // Thêm listener cho TextView vaoHocCungCoKienThuc để chuyển sang list củng cố
        TextView tvVaoHoc = findViewById(R.id.vaoHocCungCoKienThuc);
        if (tvVaoHoc != null) {
            tvVaoHoc.setOnClickListener(v -> {
                Intent intent = new Intent(TrangChu1.this, cungcokienthuc.class);
                // Nếu cần truyền maMonHoc (ví dụ: 1 cho Toán)
                // intent.putExtra("maMonHoc", (byte)1);
                startActivity(intent);
            });
        }

        // Nút logout (thêm nếu có Button ID: btnLogout)
        // Button btnLogout = findViewById(R.id.btnLogout);
        // btnLogout.setOnClickListener(v -> logout());
    }

    // Method logout (tùy chọn)
    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("userEmail", "");
        editor.apply();
        Intent intent = new Intent(this, GiaoDienDangNhap.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}