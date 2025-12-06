package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.UI.Adapter.CungCo.CungCoAdapter;
import com.example.apphoctapchotre.UI.ViewModel.CungCoViewModel2;
import com.example.apphoctapchotre.R;

public class CungCoActivity2 extends AppCompatActivity {

    private ListView listBaiKiemTra;
    private CungCoViewModel2 vm;
    private String maNguoiDung;
    private String maMon;

    private CungCoAdapter adapter;

    // Nhận kết quả từ màn làm bài
    private final ActivityResultLauncher<Intent> cauHoiLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if (result.getResultCode() == RESULT_OK) {

                    //  Reload danh sách bài củng cố
                    vm.loadCungCoDaLam(maMon);

                    // Báo cho Activity 1 rằng đã thay đổi tiến độ
                    setResult(RESULT_OK);

                    //  KHÔNG finish() → vẫn ở màn danh sách nhưng dữ liệu đã cập nhật
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc2);

        listBaiKiemTra = findViewById(R.id.listBaiKiemTra);
        TextView btnBack = findViewById(R.id.back);

        // ✔ Nhận dữ liệu từ Activity 1
        maMon = getIntent().getStringExtra("maMonHoc");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung");

        if (maNguoiDung == null) {
            maNguoiDung = "ND004";
        }

        // ViewModel
        vm = new ViewModelProvider(this).get(CungCoViewModel2.class);
        vm.setMaNguoiDung(maNguoiDung);

        // Load lần đầu
        vm.loadCungCoDaLam(maMon);

        // Quan sát danh sách
        vm.getDanhSachDaLam().observe(this, list -> {
            if (list == null) return;

            if (adapter == null) {
                adapter = new CungCoAdapter(this, list);

                adapter.setOnItemClickListener((item, position) -> {

                    Intent intent = new Intent(CungCoActivity2.this, CauHoiActivity.class);
                    intent.putExtra("maHoatDong", item.getMaHoatDong());
                    intent.putExtra("maBaiLam", item.getMaHoatDong());
                    intent.putExtra("tenHoatDong", item.getTieuDe());
                    intent.putExtra("maNguoiDung", maNguoiDung);

                    cauHoiLauncher.launch(intent);
                });

                listBaiKiemTra.setAdapter(adapter);
            } else {
                adapter.updateData(list);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
