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

import com.example.apphoctapchotre.DATA.model.LyThuyetDaLamResponse;
import com.example.apphoctapchotre.UI.Activity.MonHoc.LyThuyetNoiDungActivity;
import com.example.apphoctapchotre.UI.Adapter.LyThuyetCoBan.LyThuyetAdapter;
import com.example.apphoctapchotre.UI.ViewModel.LyThuyetViewModel2;
import com.example.apphoctapchotre.R;

public class LyThuyetActivity2 extends AppCompatActivity {

    private ListView listBaiKiemTra;
    private LyThuyetViewModel2 vm;
    private String maNguoiDung;
    private String maMon;

    private LyThuyetAdapter adapter;

    // Launcher để nhận kết quả từ Activity chi tiết (khi hoàn thành bài học)
    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Người dùng đã bấm "Con hiểu rồi !" → Refresh lại danh sách
                    vm.loadLyThuyetDaLam(maMon);
                    setResult(RESULT_OK); // Truyền lên Activity cha nếu cần
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ly_thuyet_mon_hoc2);

        listBaiKiemTra = findViewById(R.id.listBaiKiemTra);
        TextView btnBack = findViewById(R.id.back);

        // Lấy dữ liệu từ Intent
        maMon = getIntent().getStringExtra("maMonHoc");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung");

        // Nếu không có → lấy từ SharedPreferences
        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            maNguoiDung = prefs.getString("MA_NGUOI_DUNG", null);
        }

        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy mã người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo ViewModel
        vm = new ViewModelProvider(this).get(LyThuyetViewModel2.class);
        vm.setMaNguoiDung(maNguoiDung);

        // Load danh sách bài học (đã làm + chưa làm)
        vm.loadLyThuyetDaLam(maMon);

        // Quan sát dữ liệu từ ViewModel
        vm.getDanhSachDaLam().observe(this, list -> {
            if (list == null || list.isEmpty()) {
                Toast.makeText(this, "Không có bài học nào!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (adapter == null) {
                adapter = new LyThuyetAdapter(this, list);

                // Khi click vào 1 bài học → Mở màn hình chi tiết để đọc nội dung
                adapter.setOnItemClickListener((item, position) -> {
                    Intent intent = new Intent(LyThuyetActivity2.this, LyThuyetNoiDungActivity.class);
                    intent.putExtra("maHoatDong", item.getMaHoatDong());
                    intent.putExtra("tieuDe", item.getTieuDe());
                    intent.putExtra("moTa", item.getMoTa());
                    intent.putExtra("tongDiemToiDa", item.getTongDiemToiDa());
                    intent.putExtra("maNguoiDung", maNguoiDung);

                    launcher.launch(intent); // Dùng launcher để nhận kết quả khi hoàn thành
                });

                listBaiKiemTra.setAdapter(adapter);
            } else {
                adapter.updateData(list);
                adapter.notifyDataSetChanged();
            }
        });

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }
}