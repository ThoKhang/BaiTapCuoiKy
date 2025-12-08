package com.example.apphoctapchotre.UI.Activity.MonHoc;

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

import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.UI.Adapter.LyThuyetCoBan.LyThuyetAdapter;
import com.example.apphoctapchotre.UI.ViewModel.LyThuyetViewModel2;
import com.example.apphoctapchotre.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyThuyetActivity2 extends AppCompatActivity {

    private ListView listBaiKiemTra;
    private LyThuyetViewModel2 vm;
    private String maNguoiDung;
    private String maMon;

    private LyThuyetAdapter adapter;

    private ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    vm.loadLyThuyetDaLam(maMon);
                    setResult(RESULT_OK);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ly_thuyet_mon_hoc2);

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

        vm = new ViewModelProvider(this).get(LyThuyetViewModel2.class);
        vm.setMaNguoiDung(maNguoiDung);

        vm.loadLyThuyetDaLam(maMon);

        vm.getDanhSachDaLam().observe(this, list -> {
            if (list == null) return;

            if (adapter == null) {
                adapter = new LyThuyetAdapter(this, list);

                adapter.setOnItemClickListener((item, position) -> {
                    // Placeholder: Xem lý thuyết (sau này thay bằng Activity đổ MoTa)
                    Toast.makeText(this, "Xem lý thuyết: " + item.getMoTa(), Toast.LENGTH_SHORT).show();
                    // Gọi API hoàn thành (diem = tongDiemToiDa)
                    apiService.hoanThanhLyThuyet(maNguoiDung, item.getMaHoatDong(), item.getTongDiemToiDa()).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                vm.loadLyThuyetDaLam(maMon);
                                Toast.makeText(LyThuyetActivity2.this, "Hoàn thành bài học!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(LyThuyetActivity2.this, "Lỗi hoàn thành", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                listBaiKiemTra.setAdapter(adapter);
            } else {
                adapter.updateData(list);
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}