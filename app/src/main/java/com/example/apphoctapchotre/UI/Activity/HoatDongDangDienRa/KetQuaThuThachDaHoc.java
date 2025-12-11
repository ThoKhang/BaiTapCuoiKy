package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.google.android.material.button.MaterialButton;

public class KetQuaThuThachDaHoc extends AppCompatActivity {

    private TextView tvScore, tvResultMessage, tvTime, tvCorrect, Thoat;
    private MaterialButton btnThamGia, btnChiaSe;

    private int soCauDung, soCauSai, tongCau, totalScore;
    private long thoiGian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_tt_dh); // Đặt đúng tên file XML của bạn

        mappingUI();
        getDataFromIntent();
        hienThiKetQua();
        setupButtonActions();


        Thoat = findViewById(R.id.Thoat);

        Thoat.setOnClickListener(v -> {
            Intent intent = new Intent(KetQuaThuThachDaHoc.this, GiaoDienTong.class);
            intent.putExtra("OPEN_HOME", true);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void mappingUI() {
        tvScore = findViewById(R.id.tv_score);
        tvResultMessage = findViewById(R.id.tv_result_message);
        tvTime = findViewById(R.id.tv_time);
        tvCorrect = findViewById(R.id.tv_correct);

        btnThamGia = findViewById(R.id.btnThamGia);
        btnChiaSe = findViewById(R.id.btnXepHang);
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();

        soCauDung = intent.getIntExtra("SO_CAU_DUNG", 0);
        soCauSai = intent.getIntExtra("SO_CAU_SAI", 0);
        tongCau = intent.getIntExtra("TONG_CAU", 0);
        thoiGian = intent.getLongExtra("THOI_GIAN", 0);
        totalScore = intent.getIntExtra("TOTAL_SCORE", 0);
    }

    private void hienThiKetQua() {

        // Hiển thị Điểm dưới dạng X/Y
        tvScore.setText(totalScore + "/" + (tongCau * 5));

        // Hiển thị số câu đúng
        tvCorrect.setText(soCauDung + "/" + tongCau);

        // Hiển thị thời gian (mm:ss)
        tvTime.setText(formatTime(thoiGian));

        // Hiển thị thông điệp kết quả
        if (soCauSai == 0 && soCauDung > 0) {
            tvResultMessage.setText("Xuất sắc!");
        } else if (soCauDung >= tongCau / 2) {
            tvResultMessage.setText("Chúc mừng!");
        } else {
            tvResultMessage.setText("Cố gắng hơn nhé!");
        }
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        int remainingSec = seconds % 60;
        return String.format("%02d phút %02d giây", minutes, remainingSec);
    }

    private void setupButtonActions() {

        // Nút Thử lại — quay lại màn thử thách
        btnThamGia.setOnClickListener(v -> {
            Intent intent = new Intent(KetQuaThuThachDaHoc.this, CauHoiThuThach.class);
            startActivity(intent);
            finish();
        });

        // Nút Chia sẻ — chia sẻ điểm
        btnChiaSe.setOnClickListener(v -> {
            String shareText = "Mình vừa đạt " + totalScore + " điểm trong bài thử thách! Bạn có làm được tốt hơn không?";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
        });
    }
}
