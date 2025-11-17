package com.example.apphoctapchotre;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphoctapchotre.Adapter.GTKHPagerAdapter;

public class GioiThieuKHViewPager extends AppCompatActivity {

    private ViewPager2 viewPager;

    private LinearLayout tabTiengAnhContainer, tabToanContainer, tabGiaiTriContainer;
    private View underlineTiengAnh, underlineToan, underlineGiaiTri;

    private TextView tabTiengAnh, tabToan, tabGiaiTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioithieu_viewpager);

        viewPager = findViewById(R.id.viewPagerGTKH);
        viewPager.setAdapter(new GTKHPagerAdapter(this));

        tabTiengAnhContainer = findViewById(R.id.tabTiengAnhContainer);
        tabToanContainer = findViewById(R.id.tabToanContainer);
        tabGiaiTriContainer = findViewById(R.id.tabGiaiTriContainer);

        tabTiengAnh = findViewById(R.id.tabTiengAnh);
        tabToan = findViewById(R.id.tabToan);
        tabGiaiTri = findViewById(R.id.tabGiaiTri);

        underlineTiengAnh = findViewById(R.id.underlineTiengAnh);
        underlineToan = findViewById(R.id.underlineToan);
        underlineGiaiTri = findViewById(R.id.underlineGiaiTri);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        setupTabs();
        setupViewPagerCallback();
    }

    private void setupTabs() {

        tabTiengAnhContainer.setOnClickListener(v -> viewPager.setCurrentItem(0));
        tabToanContainer.setOnClickListener(v -> viewPager.setCurrentItem(1));
        tabGiaiTriContainer.setOnClickListener(v -> viewPager.setCurrentItem(2));
    }

    private void updateTabUI(int position) {

        underlineTiengAnh.setBackgroundColor(
                position == 0 ? 0xFFFFFFFF : 0x00000000);

        underlineToan.setBackgroundColor(
                position == 1 ? 0xFFFFFFFF : 0x00000000);

        underlineGiaiTri.setBackgroundColor(
                position == 2 ? 0xFFFFFFFF : 0x00000000);

        tabTiengAnh.setTypeface(null, position == 0 ? Typeface.BOLD : Typeface.NORMAL);
        tabToan.setTypeface(null, position == 1 ? Typeface.BOLD : Typeface.NORMAL);
        tabGiaiTri.setTypeface(null, position == 2 ? Typeface.BOLD : Typeface.NORMAL);
    }

    private void setupViewPagerCallback() {

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateTabUI(position);
            }
        });
    }
}
