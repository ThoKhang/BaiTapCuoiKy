package com.example.apphoctapchotre.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.TrangChu;

public class FragmentGioiThieu2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gioi_thieu2, container, false); // Đảm bảo tên layout đúng

        // Xử lý nút "Bỏ qua": Skip thẳng đến TrangChu (MainActivity)
        TextView textBoQua = view.findViewById(R.id.textBoQua);
        textBoQua.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TrangChu.class); // Thay MainActivity bằng TrangChu của bạn
            startActivity(intent);
            getActivity().finish(); // Đóng OnboardingActivity
        });
        return view;
    }
}