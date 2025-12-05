package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ProgressBar;

import com.example.apphoctapchotre.UI.Adapter.CungCo.CungCoAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.apphoctapchotre.UI.ViewModel.CungCoViewModel2;

import com.example.apphoctapchotre.R;

public class CungCoActivity2 extends AppCompatActivity {

    private ListView listBaiKiemTra;
    private CungCoViewModel2 vm;
    private String maNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc2);

        listBaiKiemTra = findViewById(R.id.listBaiKiemTra);
        TextView btnBack = findViewById(R.id.back);

        // Lấy thông tin từ Intent
        String maMon = getIntent().getStringExtra("maMonHoc");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung"); // Lấy từ Intent hoặc SharedPreferences

        // Nếu không có từ Intent, dùng giá trị mặc định
        if (maNguoiDung == null) {
            maNguoiDung = "ND002"; // Thay bằng người dùng thực tế
        }

        // Khởi tạo ViewModel
        vm = new ViewModelProvider(this).get(CungCoViewModel2.class);
        vm.setMaNguoiDung(maNguoiDung);

        // ====================================================
        // CHỌN 1 TRONG 2 CÁCH:
        // ====================================================

        // CÁCH 1: Hiển thị BÀI ĐÃ LÀM (hiện "✓ Đã hoàn thành")
        vm.loadCungCoDaLam(maMon);

        // CÁCH 2: Hiển thị BÀI CHƯA LÀM (hiện điểm)
        // vm.loadDanhSach(maMon);

        // Observe dữ liệu từ ViewModel
        vm.getDanhSachDaLam().observe(this, list -> {
            if (list != null && !list.isEmpty()) {
                CungCoAdapter adapter = new CungCoAdapter(this, list);
                listBaiKiemTra.setAdapter(adapter);
            }
        });


        // Xử lý nút back
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}