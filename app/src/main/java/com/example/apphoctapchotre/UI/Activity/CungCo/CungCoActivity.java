package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.CungCoViewModel;

public class CungCoActivity extends AppCompatActivity {

    private TextView tvTenMonHocTV, txtTienDoTV;
    private TextView txtTenMonHocT, txtTienDoT;

    private LinearLayout btnTiengViet, btnToan;

    private CungCoViewModel viewModel;
    private final String maNguoiDung = "ND004"; // FIXED CHO TEST

    // Nhận kết quả từ Activity2 để reload tiến độ
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadTienDo(); // Reload giao diện
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc1);

        bindUI();

        viewModel = new ViewModelProvider(this).get(CungCoViewModel.class);

        observeTienDo();  // Quan sát 1 lần
        loadTienDo();     // Load tiến độ ban đầu

        // Click chọn môn
        btnTiengViet.setOnClickListener(v -> openMon("MH002"));
        btnToan.setOnClickListener(v -> openMon("MH001"));
    }

    private void bindUI() {
        tvTenMonHocTV = findViewById(R.id.tvTenMonHocTV);
        txtTienDoTV = findViewById(R.id.txtTienDoTV);
        txtTenMonHocT = findViewById(R.id.txtTenMonHocT);
        txtTienDoT = findViewById(R.id.txtTienDoT);

        btnTiengViet = findViewById(R.id.btnTiengViet);
        btnToan = findViewById(R.id.btnToan);

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void observeTienDo() {
        viewModel.getTienDo().observe(this, list -> {
            if (list == null) return;

            for (var item : list) {

                // Tiếng Việt
                if (item.getMaMonHoc().equals("MH002")) {
                    tvTenMonHocTV.setText(item.getTenMonHoc());
                    txtTienDoTV.setText("Tổng số bài đã học: "
                            + item.getSoDaHoc() + "/" + item.getTongSoBai());
                }

                // Toán
                if (item.getMaMonHoc().equals("MH001")) {
                    txtTenMonHocT.setText(item.getTenMonHoc());
                    txtTienDoT.setText("Tổng số bài đã học: "
                            + item.getSoDaHoc() + "/" + item.getTongSoBai());
                }
            }
        });
    }

    // Gọi API lấy tiến độ
    private void loadTienDo() {
        viewModel.loadTienDo(maNguoiDung);
    }

    // Mở Activity2 và đợi kết quả
    private void openMon(String maMon) {
        Intent intent = new Intent(this, CungCoActivity2.class);
        intent.putExtra("maMonHoc", maMon);
        intent.putExtra("maNguoiDung", maNguoiDung);
        launcher.launch(intent); // MUST HAVE
    }
}
