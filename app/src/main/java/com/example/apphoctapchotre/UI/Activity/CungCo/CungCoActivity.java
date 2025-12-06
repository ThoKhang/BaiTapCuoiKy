package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private String maNguoiDung;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadTienDo();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc1);

        bindUI();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        maNguoiDung = prefs.getString("MA_NGUOI_DUNG", null);

        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(CungCoViewModel.class);

        observeTienDo();
        loadTienDo();

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
                if ("MH002".equals(item.getMaMonHoc())) {
                    tvTenMonHocTV.setText(item.getTenMonHoc());
                    txtTienDoTV.setText("Tổng số bài đã học: "
                            + item.getSoDaHoc() + "/" + item.getTongSoBai());
                }

                if ("MH001".equals(item.getMaMonHoc())) {
                    txtTenMonHocT.setText(item.getTenMonHoc());
                    txtTienDoT.setText("Tổng số bài đã học: "
                            + item.getSoDaHoc() + "/" + item.getTongSoBai());
                }
            }
        });
    }

    private void loadTienDo() {
        viewModel.loadTienDo(maNguoiDung);
    }

    private void openMon(String maMon) {
        Intent intent = new Intent(this, CungCoActivity2.class);
        intent.putExtra("maMonHoc", maMon);
        intent.putExtra("maNguoiDung", maNguoiDung);
        launcher.launch(intent);
    }
}
