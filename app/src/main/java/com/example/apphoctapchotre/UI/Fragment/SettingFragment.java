package com.example.apphoctapchotre.UI.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.apphoctapchotre.DATA.Repository.AuthRepository;
import com.example.apphoctapchotre.UI.Activity.Account.DangNhap.GiaoDienDangNhap;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.UI.Activity.Settings.Viewpager2.GioiThieuKHViewPager;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Settings.ThongBao;
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
        CardView itemRename = view.findViewById(R.id.itemRename);
        CardView itemChiaSe = view.findViewById(R.id.itemChiaSe);

        btnBack.setOnClickListener(v -> {
            if (getActivity() instanceof GiaoDienTong) {
                GiaoDienTong act = (GiaoDienTong) getActivity();
                if (act.getViewPager2() != null) {
                    act.getViewPager2().setCurrentItem(0, true);
                }
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

        itemRename.setOnClickListener(v -> showDialogDoiTen());

        itemThongBao.setOnClickListener(v ->
                startActivity(new Intent(getContext(), ThongBao.class))
        );


        itemChiaSe.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hãy thử ứng dụng này!");
            startActivity(Intent.createChooser(share, "Chia sẻ qua"));
        });
        view.setOnTouchListener((v, event) -> {
            if (v.getParent() != null) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        return view;
    }
    private void showDialogDoiTen() {
        Context ctx = getContext();
        if (ctx == null) return;

        SharedPreferences prefs =
                ctx.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String email = prefs.getString("userEmail", null);

        if (email == null) {
            Toast.makeText(ctx, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText edt = new EditText(ctx);
        edt.setHint("Nhập tên mới");

        new AlertDialog.Builder(ctx)
                .setTitle("Đổi tên người dùng")
                .setView(edt)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String tenMoi = edt.getText().toString().trim();
                    if (tenMoi.isEmpty()) {
                        Toast.makeText(ctx,
                                "Tên không được để trống",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AuthRepository repo = new AuthRepository();
                    repo.updateTenDangNhap(email, tenMoi,
                            new AuthRepository.UpdateNameListener() {
                                @Override
                                public void onSuccess(String message) {
                                    Toast.makeText(ctx,
                                            message,
                                            Toast.LENGTH_SHORT).show();

                                    // Lưu local để hiển thị UI
                                    prefs.edit()
                                            .putString("userName", tenMoi)
                                            .apply();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(ctx,
                                            message,
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .show();
    }

}
