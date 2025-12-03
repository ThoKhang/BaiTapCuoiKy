package com.example.apphoctapchotre.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.R;

public class FragmentGioiThieu4 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gioi_thieu4, container, false); // Đảm bảo tên layout đúng
        Button btnBatDau = view.findViewById(R.id.btnBatDau);
        btnBatDau.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GiaoDienTong.class);
            startActivity(intent);
            getActivity().finish(); // Đóng OnboardingActivity
        });

        return view;
    }
}