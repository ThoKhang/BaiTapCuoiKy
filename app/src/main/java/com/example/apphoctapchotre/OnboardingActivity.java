package com.example.apphoctapchotre;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphoctapchotre.Adapter.GioiThieu.OnboardingAdapter;

public class OnboardingActivity extends AppCompatActivity {
    //Đây là của giới thiệu
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);  // Đảm bảo layout tồn tại

        viewPager2 = findViewById(R.id.viewPager2);
        OnboardingAdapter adapter = new OnboardingAdapter(this);
        viewPager2.setAdapter(adapter);

        // Swipe tự động hỗ trợ left/right
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setOffscreenPageLimit(1);  // Giữ 1 page trước/sau để mượt
    }

    // Bỏ method onBoQuaClicked() vì Fragment tự xử lý
}