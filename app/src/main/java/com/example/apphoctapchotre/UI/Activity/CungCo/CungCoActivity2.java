package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private final ActivityResultLauncher<Intent> cauHoiLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    vm.loadCungCoDaLam(maMon);
                    setResult(RESULT_OK);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc2);

        listBaiKiemTra = findViewById(R.id.listBaiKiemTra);
        TextView btnBack = findViewById(R.id.back);

        maMon = getIntent().getStringExtra("maMonHoc");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung");

        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            maNguoiDung = prefs.getString("MA_NGUOI_DUNG", null);
        }

        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        vm = new ViewModelProvider(this).get(CungCoViewModel2.class);
        vm.setMaNguoiDung(maNguoiDung);

        vm.loadCungCoDaLam(maMon);

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
