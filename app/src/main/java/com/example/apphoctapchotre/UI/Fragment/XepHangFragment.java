package com.example.apphoctapchotre.UI.Fragment;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.UI.Adapter.XepHang.XepHangAdapter;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.DATA.model.NguoiDungXepHang;
import com.example.apphoctapchotre.DATA.model.XepHangItem;
import com.example.apphoctapchotre.DATA.model.XepHangResponse;
import com.example.apphoctapchotre.UI.ViewModel.XepHangViewModel;

import java.util.ArrayList;
import java.util.List;

public class XepHangFragment extends Fragment {

    private RecyclerView recyclerXepHang;
    private XepHangAdapter xepHangAdapter;

    private TextView tvYourName, tvYourRank, tvYourScore, tvTotalPlayers;

    private XepHangViewModel xepHangViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
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

        // ViewModel
        xepHangViewModel = new ViewModelProvider(this).get(XepHangViewModel.class);

        // Observe dữ liệu xếp hạng
        xepHangViewModel.getXepHangLiveData().observe(
                getViewLifecycleOwner(),
                this::hienThiXepHang
        );

        // Observe lỗi
        xepHangViewModel.getLoiLiveData().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && isAdded()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Lần đầu vào fragment → tải dữ liệu luôn
        taiDuLieu();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Mỗi lần tab Xếp hạng được hiển thị lại → reload dữ liệu mới nhất
        taiDuLieu();
    }

    private void taiDuLieu() {
        if (!isAdded()) return;

        SharedPreferences prefs =
                requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String email = prefs.getString("userEmail", "");
        String userName = prefs.getString("userName", "");

        android.util.Log.d("XEPHANG", "email=" + email + " | userName=" + userName);

        if (email == null || email.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Không tìm thấy email người dùng, vui lòng đăng nhập lại!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 DÒNG QUAN TRỌNG: LOAD XẾP HẠNG
        xepHangViewModel.taiDuLieuXepHang(email, 20);
    }


    private void hienThiXepHang(XepHangResponse duLieu) {
        if (!isAdded() || duLieu == null) return;

        // ===== Thông tin người dùng hiện tại =====
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

        // ===== Tổng số người chơi =====
        tvTotalPlayers.setText("Tổng cộng " + duLieu.getTongSoNguoiChoi() + " người chơi");

        // ===== Danh sách top người chơi =====
        List<XepHangItem> danhSachHienThi = new ArrayList<>();
        if (duLieu.getTopNguoiDung() != null) {
            for (NguoiDungXepHang nd : duLieu.getTopNguoiDung()) {
                danhSachHienThi.add(new XepHangItem(
                        nd.getTenDangNhap(),
                        nd.getTongDiem()
                ));
            }
        }

        if (xepHangAdapter == null) {
            xepHangAdapter = new XepHangAdapter(danhSachHienThi);
            recyclerXepHang.setAdapter(xepHangAdapter);
        } else {
            // Nếu adapter đã có → chỉ cần cập nhật lại dữ liệu
            xepHangAdapter.setData(danhSachHienThi);
        }
    }
}
