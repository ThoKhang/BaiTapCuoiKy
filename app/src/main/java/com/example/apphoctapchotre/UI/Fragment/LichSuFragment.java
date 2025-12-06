package com.example.apphoctapchotre.UI.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.UI.Adapter.DiemChiTiet.AdapterDiemChiTiet;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.DiemChiTiet;
import com.example.apphoctapchotre.UI.ViewModel.LichSuViewModel;

import java.util.ArrayList;
import java.util.List;

public class LichSuFragment extends Fragment {

    private TextView tvTongDiem, tvDiemKiemTra, tvDiemHoatDong;
    private RecyclerView danhSachChiTiet;
    private AdapterDiemChiTiet adapter;
    private final List<DiemChiTiet> danhSachMau = new ArrayList<>();

    private LichSuViewModel lichSuViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_lich_su, container, false);

        // Ánh xạ view
        tvTongDiem = view.findViewById(R.id.tvTongDiem);
        tvDiemKiemTra = view.findViewById(R.id.tvDiemKiemTra);
        tvDiemHoatDong = view.findViewById(R.id.tvDiemHoatDong);

        danhSachChiTiet = view.findViewById(R.id.danhSachChiTiet);
        danhSachChiTiet.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new AdapterDiemChiTiet(danhSachMau);
        danhSachChiTiet.setAdapter(adapter);

        // Nút back
        ImageView btnBack = view.findViewById(R.id.ibtnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        // Khởi tạo ViewModel
        lichSuViewModel = new ViewModelProvider(this).get(LichSuViewModel.class);

        // Đăng ký observe LiveData
        observeViewModel();

        // Lấy email & gọi load dữ liệu
        String email = docEmailNguoiDung();
        lichSuViewModel.taiLichSuDiem(email);

        return view;
    }

    private String docEmailNguoiDung() {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return prefs.getString("userEmail", "");
    }

    private void observeViewModel() {
        // Tổng điểm
        lichSuViewModel.getTongDiem().observe(getViewLifecycleOwner(), tong -> {
            tvTongDiem.setText(String.valueOf(tong));
        });

        // Điểm kiểm tra
        lichSuViewModel.getDiemKiemTra().observe(getViewLifecycleOwner(), diem -> {
            tvDiemKiemTra.setText(String.valueOf(diem));
        });

        // Điểm hoạt động
        lichSuViewModel.getDiemHoatDong().observe(getViewLifecycleOwner(), diem -> {
            tvDiemHoatDong.setText(String.valueOf(diem));
        });

        // Danh sách chi tiết
        lichSuViewModel.getDanhSachChiTiet().observe(getViewLifecycleOwner(), list -> {
            danhSachMau.clear();
            if (list != null) {
                danhSachMau.addAll(list);
            }
            adapter.notifyDataSetChanged();
        });

        // Lỗi
        lichSuViewModel.getLoiLiveData().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty() && isAdded()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Nếu muốn dùng ProgressBar thì observe thêm getDangTaiLiveData()
    }
}
