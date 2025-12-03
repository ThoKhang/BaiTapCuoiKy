package com.example.apphoctapchotre.UI.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apphoctapchotre.UI.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetCoBan;
import com.example.apphoctapchotre.UI.Activity.TroChoi.Trochoi;
import com.example.apphoctapchotre.UI.Activity.LyThuyet.BangCuuChuong;
import com.example.apphoctapchotre.BannerSlide;
import com.example.apphoctapchotre.UI.Activity.Settings.CaiDat;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa.Thuthach100Cau;

import java.util.ArrayList;
import java.util.List;

public class TrangChuFragment extends Fragment {

    private ViewPager2 bannerViewPager;
    private LinearLayout dotsLayout;
    private BannerSlide bannerAdapter;
    private List<BannerSlide.BannerItem> bannerList;
    private final Handler autoSlideHandler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;

    private LinearLayout bangCuuChuong, lnLichSu, lnXepHang, lnTroChoi, thuthach1, thuthach2, lnSudoku;
    private ImageView ibtnCaiDat;
    private View btnLTCoBan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_giao_dien_trang_chu, container, false);

        // ====================== KIỂM TRA ĐĂNG NHẬP ======================
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", 0);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String userEmail = prefs.getString("userEmail", "");

        if (!isLoggedIn || userEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(requireActivity(), GiaoDienDangNhap.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            requireActivity().finish();
            return view;
        }

        // ====================== CHÀO MỪNG NGƯỜI DÙNG ======================
        TextView tvWelcome = view.findViewById(R.id.tvWelcome);
        if (tvWelcome != null) {
            tvWelcome.setText("Chào mừng: " + userEmail);
        }

        // ====================== CÁC NÚT ĐIỀU HƯỚNG ======================
        btnLTCoBan = view.findViewById(R.id.btnLTCoBan);
        btnLTCoBan.setOnClickListener(v -> startActivity(new Intent(requireActivity(), LyThuyetCoBan.class)));

        bangCuuChuong = view.findViewById(R.id.lnBangCuuChuong);
        bangCuuChuong.setOnClickListener(v -> startActivity(new Intent(requireActivity(), BangCuuChuong.class)));

        // SỬA LẠI HOÀN TOÀN 2 DÒNG NÀY:
        lnTroChoi = view.findViewById(R.id.lnTroChoi);
        lnTroChoi.setOnClickListener(v -> startActivity(new Intent(requireActivity(), Trochoi.class)));


        lnXepHang = view.findViewById(R.id.xepHang);
        lnXepHang.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(1, true); // tab Xếp Hạng (vị trí 1)
            }
        });

        lnLichSu = view.findViewById(R.id.lnLichsu);  // bạn đã làm đúng rồi, giữ nguyên hoặc sửa cho giống
        lnLichSu.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(2, true); // tab Lịch Sử (vị trí 2)
            }
        });

        ibtnCaiDat = view.findViewById(R.id.ibtnCaiDat);
        ibtnCaiDat.setOnClickListener(v -> startActivity(new Intent(requireActivity(), CaiDat.class)));


        thuthach2= view.findViewById(R.id.thuthach2);
        thuthach2.setOnClickListener(v -> startActivity(new Intent(requireActivity(), Thuthach100Cau.class)));
        // ====================== BANNER SLIDER ======================
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        // CHẶN VUỐT SANG VIEWPAGER LỚN
        bannerViewPager.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        dotsLayout = view.findViewById(R.id.dotsLayout);


        bannerList = new ArrayList<>();
        bannerList.add(new BannerSlide.BannerItem(R.drawable.bannerblue, "Củng cố kiến thức", "Vào học ngay!"));
        bannerList.add(new BannerSlide.BannerItem(R.drawable.bannerblue2, "Ôn luyện", "Học từ bài tập"));

        bannerAdapter = new BannerSlide(bannerList);
        bannerViewPager.setAdapter(bannerAdapter);

        bannerViewPager.post(this::addDotsInitial);

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                addDots(position);
                currentPage = position;
            }
        });

        // Tự động chuyển slide
        autoSlideHandler.postDelayed(autoSlideRunnable, 5000);

        return view;
    }

    private void addDotsInitial() {
        addDots(0);
    }

    private void addDots(int position) {
        if (dotsLayout == null || getContext() == null) return;
        dotsLayout.removeAllViews();
        ImageView[] dots = new ImageView[bannerList.size()];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageResource(R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        if (dots.length > 0) {
            dots[position].setImageResource(R.drawable.dot_active);
        }
    }

    private final Runnable autoSlideRunnable = new Runnable() {
        @Override
        public void run() {
            if (bannerAdapter != null && bannerList.size() > 1) {
                currentPage = (currentPage + 1) % bannerList.size();
                bannerViewPager.setCurrentItem(currentPage, true);
                autoSlideHandler.postDelayed(this, 5000);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }
}