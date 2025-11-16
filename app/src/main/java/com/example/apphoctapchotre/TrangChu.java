package com.example.apphoctapchotre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphoctapchotre.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.Activity.OnLuyen.OnLuyen;

import java.util.ArrayList;
import java.util.List;

public class TrangChu extends AppCompatActivity {

    private ViewPager2 bannerViewPager;
    private LinearLayout dotsLayout;
    private BannerSlide bannerAdapter;
    private List<BannerSlide.BannerItem> bannerList;
    private Handler autoSlideHandler = new Handler();
    private int currentPage = 0;
    private long backPressedTime = 0;
    private LinearLayout bangCuuChuong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_trang_chu);

        // ========================== KIỂM TRA ĐĂNG NHẬP ==========================
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String userEmail = prefs.getString("userEmail", "");

        if (!isLoggedIn || userEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GiaoDienDangNhap.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // ========================== CHÀO MỪNG NGƯỜI DÙNG ==========================
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        if (tvWelcome != null) {
            tvWelcome.setText("Chào mừng: " + userEmail);
        }

        // ========================== BANNER LƯỚT QUA ==========================
        bannerViewPager = findViewById(R.id.bannerViewPager);
        dotsLayout = findViewById(R.id.dotsLayout);

        bannerList = new ArrayList<>();
        bannerList.add(new BannerSlide.BannerItem(
                R.drawable.bannerblue,
                "Củng cố kiến thức",
                "Vào học ngay!"
        ));

        bannerList.add(new BannerSlide.BannerItem(
                R.drawable.bannerblue2,
                "Ôn luyện",
                "Học từ bài tập"
        ));

        // Gắn adapter cho ViewPager2
        bannerAdapter = new BannerSlide(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        // Thêm chấm tròn
        bannerViewPager.post(() -> addDots(0));

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentPage = position;
            }
        });

        // Tự động lướt banner mỗi 5 giây
        autoSlideHandler.postDelayed(autoSlideRunnable, 5000);

        // bảng cửu chương
        bangCuuChuong = findViewById(R.id.bangCuuChuong);
        bangCuuChuong.setOnClickListener(v -> {
            Intent intent = new Intent(this, BangCuuChuong.class);
            startActivity(intent);
        });
    }

    // ========================== CHẤM TRÒN INDICATOR ==========================
    private void addDots(int position) {
        dotsLayout.removeAllViews();
        ImageView[] dots = new ImageView[bannerList.size()];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.dot_inactive);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        if (dots.length > 0) {
            dots[position].setImageResource(R.drawable.dot_active);
        }
    }

    // ========================== TỰ ĐỘNG CHUYỂN SLIDE ==========================
    private final Runnable autoSlideRunnable = new Runnable() {
        @Override
        public void run() {
            if (bannerAdapter != null && bannerList.size() > 0) {
                currentPage = (currentPage + 1) % bannerList.size();
                bannerViewPager.setCurrentItem(currentPage, true);
                autoSlideHandler.postDelayed(this, 5000); // 5 giây tự động chuyển
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }

    // ========================== LOGOUT (TÙY CHỌN) ==========================
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

    // ========================== XỬ LÝ NÚT BACK ==========================
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(this, "Nhấn lần nữa để thoát ứng dụng.", Toast.LENGTH_SHORT).show();
            backPressedTime = System.currentTimeMillis();
        }
    }
}
