package com.example.apphoctapchotre.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.apphoctapchotre.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.GioiThieuKHViewPager;
import com.example.apphoctapchotre.NgonNgu;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.ThongBao;
import com.google.android.material.button.MaterialButton;

public class SettingFragment extends Fragment {

    public SettingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        MaterialButton btnLogOut = view.findViewById(R.id.btnLogOut);
        CardView itemGioiThieu = view.findViewById(R.id.itemGioiThieu);
        CardView itemThongBao = view.findViewById(R.id.itemThongBao);
        CardView itemNgonNgu = view.findViewById(R.id.itemNgonNgu);
        CardView itemChiaSe = view.findViewById(R.id.itemChiaSe);

        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        btnLogOut.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),GiaoDienDangNhap.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


        itemGioiThieu.setOnClickListener(v ->
                startActivity(new Intent(getContext(), GioiThieuKHViewPager.class))
        );

        itemThongBao.setOnClickListener(v ->
                startActivity(new Intent(getContext(), ThongBao.class))
        );

        itemNgonNgu.setOnClickListener(v ->
                startActivity(new Intent(getContext(), NgonNgu.class))
        );

        itemChiaSe.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hãy thử ứng dụng này!");
            startActivity(Intent.createChooser(share, "Chia sẻ qua"));
        });
        view.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        return view;
    }
}
