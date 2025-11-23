package com.example.apphoctapchotre.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apphoctapchotre.Activity.Games.sudoku;
import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.R;

public class TroChoiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_tro_choi, container, false);

        // ==================== NÚT BACK ====================
        ImageView btnBack = view.findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        return view;
    }
}
