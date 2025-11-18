package com.example.apphoctapchotre.Fragment;   // giữ package giống 2 fragment kia

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.Adapter.DiemChiTiet.AdapterDiemChiTiet;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.DiemChiTiet;

import java.util.ArrayList;
import java.util.List;

public class LichSuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_lich_su, container, false);

        // ==================== RECYCLERVIEW ====================
        RecyclerView danhSachChiTiet = view.findViewById(R.id.danhSachChiTiet);
        danhSachChiTiet.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Dữ liệu mẫu (sau này thay bằng dữ liệu thật từ DB)
        List<DiemChiTiet> danhSachMau = new ArrayList<>();
        danhSachMau.add(new DiemChiTiet(10, "17/11/2025 15:30", "Hoàn thành bài kiểm tra toán"));
        danhSachMau.add(new DiemChiTiet(5, "16/11/2025 10:00", "Đăng nhập hàng ngày"));
        danhSachMau.add(new DiemChiTiet(15, "15/11/2025 18:45", "Chia sẻ kết quả học tập"));
        danhSachMau.add(new DiemChiTiet(15, "15/11/2025 18:45", "Chia sẻ kết quả học tập"));

        AdapterDiemChiTiet adapter = new AdapterDiemChiTiet(danhSachMau);
        danhSachChiTiet.setAdapter(adapter);

        // ==================== NÚT BACK ====================
        // Vì giờ dùng Bottom Navigation rồi nên nút back không cần chuyển về TrangChu Activity nữa
        // Chỉ cần chuyển tab về Trang Chủ là đủ (tự động nhờ ViewPager2 + BottomNav)
        ImageView btnBack = view.findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        return view;
    }
}