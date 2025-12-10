package com.example.apphoctapchotre.UI.Activity.GiaoDienTong;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphoctapchotre.UI.Adapter.GiaoDienTong.GiaoDienTongAdapter;
import com.example.apphoctapchotre.UI.Fragment.LichSuFragment;
import com.example.apphoctapchotre.UI.Fragment.SettingFragment;
import com.example.apphoctapchotre.UI.Fragment.TrangChuFragment;
import com.example.apphoctapchotre.UI.Fragment.XepHangFragment;
import com.example.apphoctapchotre.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GiaoDienTong extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_tong);  // layout mới, lát nữa tạo

        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setItemIconTintList(null);   // Bắt buộc cho icon PNG có màu

        // Setup Adapter
        GiaoDienTongAdapter adapter = new GiaoDienTongAdapter(this);
        adapter.addFragment(new TrangChuFragment(), "Trang Chủ");
        adapter.addFragment(new XepHangFragment(), "Xếp Hạng");
        adapter.addFragment(new LichSuFragment(), "Lịch Sử");
        adapter.addFragment(new SettingFragment(), "Cài Đặt");
        viewPager2.setAdapter(adapter);

        // Đồng bộ ViewPager2 ↔ BottomNavigation
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_trang_chu) {
                viewPager2.setCurrentItem(0, true);
            } else if (itemId == R.id.menu_xep_hang) {
                viewPager2.setCurrentItem(1, true);
            } else if (itemId == R.id.menu_lich_su) {
                viewPager2.setCurrentItem(2, true);
            } else if (itemId == R.id.menu_cai_dat) {
                viewPager2.setCurrentItem(3, true);
            }

            return true;
        });

        // Mở mặc định Trang Chủ
        viewPager2.setCurrentItem(0, false);

        boolean openHome = getIntent().getBooleanExtra("OPEN_HOME", false);
        if (openHome) {
            viewPager2.setCurrentItem(0, false); // 0 = Trang Chủ
        }

    }
    public ViewPager2 getViewPager2() {
        return viewPager2;
    }

}