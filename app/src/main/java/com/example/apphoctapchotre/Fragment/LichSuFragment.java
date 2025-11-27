package com.example.apphoctapchotre.Fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.Adapter.DiemChiTiet.AdapterDiemChiTiet;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.DiemChiTiet;
import com.example.apphoctapchotre.model.LichSuDiemResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuFragment extends Fragment {

    private TextView tvTongDiem, tvDiemKiemTra, tvDiemHoatDong;
    private RecyclerView danhSachChiTiet;
    private AdapterDiemChiTiet adapter;
    private final List<DiemChiTiet> danhSachMau = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_lich_su, container, false);

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

        // Gọi API lấy dữ liệu thật
        taiLichSuDiem();

        return view;
    }

    private void taiLichSuDiem() {
        Context context = requireContext();
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        if (email == null || email.isEmpty()) {
            Toast.makeText(context, "Không tìm thấy người dùng, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.layLichSuDiem(body).enqueue(new Callback<LichSuDiemResponse>() {
            @Override
            public void onResponse(Call<LichSuDiemResponse> call, Response<LichSuDiemResponse> response) {
                if (!isAdded()) return; // tránh crash khi fragment đã detach

                if (response.isSuccessful() && response.body() != null) {
                    LichSuDiemResponse duLieu = response.body();

                    // Set điểm
                    tvTongDiem.setText(String.valueOf(duLieu.getTongDiem()));
                    tvDiemKiemTra.setText(String.valueOf(duLieu.getDiemKiemTra()));
                    tvDiemHoatDong.setText(String.valueOf(duLieu.getDiemHoatDong()));

                    // Set danh sách chi tiết
                    danhSachMau.clear();
                    if (duLieu.getDanhSachChiTiet() != null) {
                        danhSachMau.addAll(duLieu.getDanhSachChiTiet());
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    String msg = "Lỗi tải lịch sử điểm!";
                    try {
                        if (response.errorBody() != null) {
                            msg = response.errorBody().string();
                        }
                    } catch (Exception ignored) {}
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LichSuDiemResponse> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
