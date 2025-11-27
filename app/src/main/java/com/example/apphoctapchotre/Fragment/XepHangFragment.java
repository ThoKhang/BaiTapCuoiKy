package com.example.apphoctapchotre.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.Adapter.XepHang.XepHangAdapter;
import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.NguoiDungXepHang;
import com.example.apphoctapchotre.model.XepHangItem;
import com.example.apphoctapchotre.model.XepHangResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XepHangFragment extends Fragment {

    private RecyclerView recyclerXepHang;
    private XepHangAdapter xepHangAdapter;

    private TextView tvYourName, tvYourRank, tvYourScore, tvTotalPlayers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_xep_hang, container, false);

        recyclerXepHang = view.findViewById(R.id.recyclerXepHang);
        recyclerXepHang.setLayoutManager(new LinearLayoutManager(requireContext()));

        tvYourName = view.findViewById(R.id.tvYourName);
        tvYourRank = view.findViewById(R.id.tvYourRank);
        tvYourScore = view.findViewById(R.id.tvYourScore);
        tvTotalPlayers = view.findViewById(R.id.tvTotalPlayers);

        ImageButton ibtnBack = view.findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                ((GiaoDienTong) getActivity()).getViewPager2().setCurrentItem(0, true);
            }
        });

        taiDuLieuXepHang();

        return view;
    }

    private void taiDuLieuXepHang() {
        // ✅ ĐỌC ĐÚNG SharedPreferences giống DangNhapOTP
        String email = "";
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            email = prefs.getString("userEmail", "");
        }

        if (email == null || email.isEmpty()) {
            Toast.makeText(requireContext(), "Không tìm thấy email người dùng, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.layXepHang(email, 20).enqueue(new Callback<XepHangResponse>() {
            @Override
            public void onResponse(Call<XepHangResponse> call, Response<XepHangResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    XepHangResponse duLieu = response.body();

                    // ======= Thông tin người dùng hiện tại =======
                    NguoiDungXepHang nguoiDungHienTai = duLieu.getNguoiDungHienTai();
                    if (nguoiDungHienTai != null) {
                        tvYourName.setText("Bạn (" + nguoiDungHienTai.getTenDangNhap() + ")");
                        tvYourRank.setText("Hạng " + nguoiDungHienTai.getHang());
                        tvYourScore.setText(String.format("%,d", nguoiDungHienTai.getTongDiem()));
                    } else {
                        tvYourName.setText("Bạn");
                        tvYourRank.setText("Hạng -");
                        tvYourScore.setText("0");
                    }

                    // ======= Tổng số người chơi =======
                    tvTotalPlayers.setText("Tổng cộng " + duLieu.getTongSoNguoiChoi() + " người chơi");

                    // ======= Danh sách top người chơi =======
                    List<XepHangItem> danhSachHienThi = new ArrayList<>();
                    if (duLieu.getTopNguoiDung() != null) {
                        for (NguoiDungXepHang nd : duLieu.getTopNguoiDung()) {
                            danhSachHienThi.add(new XepHangItem(
                                    nd.getTenDangNhap(),
                                    nd.getTongDiem()
                            ));
                        }
                    }

                    xepHangAdapter = new XepHangAdapter(danhSachHienThi);
                    recyclerXepHang.setAdapter(xepHangAdapter);

                } else {
                    Toast.makeText(requireContext(), "Không tải được bảng xếp hạng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<XepHangResponse> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
