package com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.KetQuaLienHoanTinhToan;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.LienHoanTinhToanViewModel;

import java.util.List;

public class NhapDapAnLienHoanTinhToan extends AppCompatActivity {
    private EditText edtDapAn;
    private TextView tvGiaiThich, tvCauHoi, tv_question_number, tv_thoi_gian;
    private LinearLayout btn_huong_dan;
    private ImageView quayLai;
    private Button btnTiepTuc;
    private int soCau = 0, demCauDung = 0, demCauSai = 0;
    private int tongSoCau; // Tổng số câu trong đề
    private List<CauHoi> danhSach;
    private LienHoanTinhToanViewModel lienHoanTinhToanViewModel;
    private TienTrinh tienTrinh = new TienTrinh();
    private CountDownTimer countDownTimer;
    private final int TIME_LIMIT = 30; // 30 giây mỗi câu
    private int timeLeft = TIME_LIMIT;
    private long tongThoiGian = 0; // milliseconds
    private long batDauCau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_dap_an_lien_hoan_tinh_toan);

        edtDapAn = findViewById(R.id.edtDapAn);
        tvGiaiThich = findViewById(R.id.tvGiaiThich);
        tvCauHoi = findViewById(R.id.tvCauHoi);
        btn_huong_dan = findViewById(R.id.btn_huong_dan);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tv_question_number = findViewById(R.id.tv_question_number);
        quayLai = findViewById(R.id.quayLai);
        tv_thoi_gian = findViewById(R.id.tv_thoi_gian);

        lienHoanTinhToanViewModel = new ViewModelProvider(this).get(LienHoanTinhToanViewModel.class);

        lienHoanTinhToanViewModel.deLienHoan.observe(this, de -> {
            if (de != null) {
                danhSach = de.getDanhSachCauHoi();
                tongSoCau = danhSach.size(); // Lưu tổng số câu

                tienTrinh.setMaHoatDong(de.getMaHoatDong());

                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String email = prefs.getString("userEmail", null);
                if (email != null)
                    tienTrinh.setEmail(email);

                soCau = 0;
                hienCau(soCau);
            }
        });

        lienHoanTinhToanViewModel.loadDeLienHoanTinhToan();

        btnTiepTuc.setOnClickListener(v -> {
            if (countDownTimer != null)
                countDownTimer.cancel();

            long ketThucCau = System.currentTimeMillis();
            tongThoiGian += (ketThucCau - batDauCau);

            String nhap = edtDapAn.getText().toString().trim();
            String dapAnDung = danhSach.get(soCau).getDapAn().get(0).getNoiDungDapAn().trim();

            if (nhap.equalsIgnoreCase(dapAnDung)) {
                demCauDung++;
            } else {
                // SAI → Tính câu hiện tại + tất cả câu còn lại là sai
                int conLai = tongSoCau - (soCau + 1); // số câu chưa làm
                demCauSai += conLai + 1; // +1 là câu hiện tại đã sai

                Toast.makeText(this, "Sai rồi! Bài làm kết thúc.", Toast.LENGTH_LONG).show();

                // Ép điểm = 0 và kết thúc ngay
                tienTrinh.setSoCauDung(demCauDung);
                tienTrinh.setDiemDatDuoc(0);
                tienTrinh.setSoCauDaLam(tongSoCau);
                tienTrinh.setDaHoanThanh(0);
                lienHoanTinhToanViewModel.guiTienTrinh(tienTrinh);
                long tongGiay = tongThoiGian / 1000;
                Intent intent = new Intent(this, KetQuaLienHoanTinhToan.class);
                intent.putExtra("DIEM", 0);
                intent.putExtra("CAU_DUNG", demCauDung);
                intent.putExtra("CAU_SAI", demCauSai);
                intent.putExtra("THOI_GIAN", tongGiay);
                startActivity(intent);
                finish();
                return; // Dừng không cho làm tiếp
            }

            // Nếu đúng → kiểm tra có phải câu cuối không
            if (soCau < danhSach.size() - 1) {
                soCau++;
                hienCau(soCau);
            } else {
                // Hoàn thành đúng hết → dùng hàm cũ để tính điểm bình thường
                Toast.makeText(this, "Xuất sắc! Bạn đã hoàn thành Liên hoàn tính toán!", Toast.LENGTH_LONG).show();
                ketThucBaiLam();
            }
        });

        tvGiaiThich.setVisibility(View.INVISIBLE);
        btn_huong_dan.setOnClickListener(v -> {
            tvGiaiThich.setVisibility(View.VISIBLE);
        });

        quayLai.setOnClickListener(v -> {
            finish();
        });
    }
    private void hienCau(int i) {
        batDauCau = System.currentTimeMillis();
        CauHoi ch = danhSach.get(i);
        tvCauHoi.setText(ch.getNoiDungCauHoi());
        tvGiaiThich.setText(ch.getGiaiThich());
        edtDapAn.setText("");
        tv_question_number.setText("Câu " + (soCau + 1) + "/" + danhSach.size());
        startTimer();
    }
    private void startTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        timeLeft = TIME_LIMIT;

        countDownTimer = new CountDownTimer(timeLeft * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                tv_thoi_gian.setText("Thời gian còn: " + timeLeft + "s");
            }

            @Override
            public void onFinish() {
                long ketThucCau = System.currentTimeMillis();
                tongThoiGian += (ketThucCau - batDauCau);
                tv_thoi_gian.setText("Hết thời gian!");

                // Hết giờ → sai câu hiện tại + tất cả câu còn lại
                int conLai = tongSoCau - (soCau + 1);
                demCauSai += conLai + 1;

                Toast.makeText(NhapDapAnLienHoanTinhToan.this, "Hết giờ! Bài làm kết thúc.", Toast.LENGTH_LONG).show();

                tienTrinh.setSoCauDung(demCauDung);
                tienTrinh.setDiemDatDuoc(0);
                tienTrinh.setSoCauDaLam(tongSoCau);
                lienHoanTinhToanViewModel.guiTienTrinh(tienTrinh);

                long tongGiay = tongThoiGian / 1000;

                Intent intent = new Intent(NhapDapAnLienHoanTinhToan.this, KetQuaLienHoanTinhToan.class);
                intent.putExtra("DIEM", 0);
                intent.putExtra("CAU_DUNG", demCauDung);
                intent.putExtra("CAU_SAI", demCauSai);
                intent.putExtra("THOI_GIAN", tongGiay);
                startActivity(intent);
                finish();
            }
        }.start();
    }
    private void ketThucBaiLam() {
        tienTrinh.setDaHoanThanh(1);
        tienTrinh.setSoCauDung(demCauDung);
        tienTrinh.setDiemDatDuoc(demCauDung * 5);
        tienTrinh.setSoCauDaLam(demCauDung + demCauSai);
        lienHoanTinhToanViewModel.guiTienTrinh(tienTrinh);
        long tongGiay = tongThoiGian / 1000;
        Intent intent = new Intent(this, KetQuaLienHoanTinhToan.class);
        intent.putExtra("DIEM", demCauDung * 5);
        intent.putExtra("CAU_DUNG", demCauDung);
        intent.putExtra("CAU_SAI", demCauSai);
        intent.putExtra("THOI_GIAN", tongGiay);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}