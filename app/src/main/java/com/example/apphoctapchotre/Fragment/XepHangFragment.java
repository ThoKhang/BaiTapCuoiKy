package com.example.apphoctapchotre.Fragment;   // giữ package giống 2 fragment kia

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.Adapter.XepHang.XepHangAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.XepHangItem;

import java.util.ArrayList;
import java.util.List;

public class XepHangFragment extends Fragment {

    private RecyclerView recyclerXepHang;
    private XepHangAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_xep_hang, container, false);

        // ==================== RECYCLERVIEW XẾP HẠNG ====================
        recyclerXepHang = view.findViewById(R.id.recyclerXepHang);
        recyclerXepHang.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Dữ liệu mẫu giống hệt bạn đang có
        List<XepHangItem> list = new ArrayList<>();
        list.add(new XepHangItem("USERNAME_01", 42500));
        list.add(new XepHangItem("USERNAME_02", 35100));
        list.add(new XepHangItem("USERNAME_03", 29305));
        list.add(new XepHangItem("USERNAME_04", 25260));
        list.add(new XepHangItem("USERNAME_05", 19250));
        list.add(new XepHangItem("USERNAME_06", 16890));
        list.add(new XepHangItem("USERNAME_07", 15020));
        list.add(new XepHangItem("USERNAME_08", 11000));
        list.add(new XepHangItem("USERNAME_09", 10561));

        adapter = new XepHangAdapter(list);
        recyclerXepHang.setAdapter(adapter);

        // ==================== NÚT BACK ====================
        ImageButton ibtnBack = view.findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> {
            // Vì giờ dùng Bottom Navigation nên chỉ cần chuyển về tab Trang Chủ là đẹp nhất
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        return view;
    }
}