package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.model.TienTrinh;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.DeOnLuyenViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TracNghiem2 extends AppCompatActivity {

    private TextView tvCauHoi, tvDapAnA, tvDapAnB, tvDapAnC, tvDapAnD,
            tvGiaiThich, tvQuestionNumber, tvTieuDePhu;
    private RadioButton btnA, btnB, btnC, btnD;
    private Button btnTiepTuc;
    private LinearLayout btn_huong_dan;
    private ImageView backButton;

    private String tenDe, maHoatDong;
    private DeOnLuyenViewModel deOnLuyenViewModel;
    private List<CauHoi> danhSachCauHoi;

    private TienTrinh tienTrinh = new TienTrinh();
    private int currentIndex = 0;
    private int demCauDung = 0, demCauSai = 0, diemThuong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem2);

        anhXa();
        xuLyQuayLai();
        xuLyTieuDe();
        loadData();
        xuLyButton();
    }

    private void anhXa() {
        tvCauHoi = findViewById(R.id.tvCauHoi);
        tvDapAnA = findViewById(R.id.tvDapAnA);
        tvDapAnB = findViewById(R.id.tvDapAnB);
        tvDapAnC = findViewById(R.id.tvDapAnC);
        tvDapAnD = findViewById(R.id.tvDapAnD);
        tvGiaiThich = findViewById(R.id.tvGiaiThich);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvTieuDePhu = findViewById(R.id.tvTieuDePhu);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btn_huong_dan = findViewById(R.id.btn_huong_dan);
        backButton = findViewById(R.id.quayLai);
    }

    private void xuLyQuayLai() {
        backButton.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void xuLyTieuDe() {
        tenDe = getIntent().getStringExtra("TEN_DE");
        int soDe = Integer.parseInt(tenDe.replaceAll("\\D+", ""));
        String tieuDe = "";

        if (tenDe.toLowerCase().contains("cơ bản")) {
            tieuDe = "Ôn Cơ bản " + soDe;
            diemThuong = 5;
        } else if (tenDe.toLowerCase().contains("trung bình")) {
            tieuDe = "Ôn TB " + soDe;
            diemThuong = 8;
        } else if (tenDe.toLowerCase().contains("nâng cao")) {
            tieuDe = "Ôn NC " + soDe;
            diemThuong = 10;
        }

        tvTieuDePhu.setText(tenDe);

        deOnLuyenViewModel = new ViewModelProvider(this).get(DeOnLuyenViewModel.class);
        deOnLuyenViewModel.loadDeOnLuyen(tieuDe);
    }

    private void loadData() {
        deOnLuyenViewModel.deOnLuyenMutableLiveData.observe(this, de -> {
            if (de == null || de.getDanhSachCauHoi() == null || de.getDanhSachCauHoi().isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu câu hỏi", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            maHoatDong = de.getMaHoatDong();
            tienTrinh.setMaHoatDong(maHoatDong);

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            tienTrinh.setEmail(prefs.getString("userEmail", ""));

            danhSachCauHoi = de.getDanhSachCauHoi();
            currentIndex = 0;

            loadCauHoi();
        });
    }

    private void xuLyButton() {
        btnA.setOnClickListener(v -> checkAnswer(btnA, 0));
        btnB.setOnClickListener(v -> checkAnswer(btnB, 1));
        btnC.setOnClickListener(v -> checkAnswer(btnC, 2));
        btnD.setOnClickListener(v -> checkAnswer(btnD, 3));

        btnTiepTuc.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < danhSachCauHoi.size()) {
                resetButtons();
                loadCauHoi();
            } else {
                hoanThanh();
            }
        });

        btn_huong_dan.setOnClickListener(v -> tvGiaiThich.setVisibility(View.VISIBLE));

        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(TracNghiem2.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
    }

    private void loadCauHoi() {
        if (danhSachCauHoi == null || danhSachCauHoi.isEmpty() || currentIndex >= danhSachCauHoi.size()) {
            Toast.makeText(this, "Không có câu hỏi hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // reset tint
        btnA.getBackground().setTintList(null);
        btnB.getBackground().setTintList(null);
        btnC.getBackground().setTintList(null);
        btnD.getBackground().setTintList(null);

        CauHoi cauHoi = danhSachCauHoi.get(currentIndex);

        tvCauHoi.setText(cauHoi.getNoiDungCauHoi());

        String giaiThich = cauHoi.getGiaiThich();
        if (giaiThich == null || giaiThich.trim().isEmpty()) {
            tvGiaiThich.setText("Không có giải thích cho câu này");
        } else {
            tvGiaiThich.setText(giaiThich);
        }
        tvGiaiThich.setVisibility(View.INVISIBLE);

        tvQuestionNumber.setText("Câu " + (currentIndex + 1) + "/" + danhSachCauHoi.size());

        // ===== FIX CRASH: đáp án có thể không đủ 4 =====
        List<?> dapAn = cauHoi.getDapAn();
        anTatCaDapAn();

        if (dapAn != null) {
            if (dapAn.size() > 0) hienDapAn(btnA, tvDapAnA, "A.", cauHoi.getDapAn().get(0).getNoiDungDapAn());
            if (dapAn.size() > 1) hienDapAn(btnB, tvDapAnB, "B.", cauHoi.getDapAn().get(1).getNoiDungDapAn());
            if (dapAn.size() > 2) hienDapAn(btnC, tvDapAnC, "C.", cauHoi.getDapAn().get(2).getNoiDungDapAn());
            if (dapAn.size() > 3) hienDapAn(btnD, tvDapAnD, "D.", cauHoi.getDapAn().get(3).getNoiDungDapAn());
        }
    }

    private void hienDapAn(RadioButton btn, TextView tv, String prefix, String text) {
        btn.setVisibility(View.VISIBLE);
        tv.setText(prefix + text);
    }

    private void anTatCaDapAn() {
        btnA.setVisibility(View.GONE);
        btnB.setVisibility(View.GONE);
        btnC.setVisibility(View.GONE);
        btnD.setVisibility(View.GONE);
    }

    private void resetButtons() {
        btnA.setBackgroundResource(R.drawable.option_button_bg);
        btnB.setBackgroundResource(R.drawable.option_button_bg);
        btnC.setBackgroundResource(R.drawable.option_button_bg);
        btnD.setBackgroundResource(R.drawable.option_button_bg);

        btnA.setChecked(false);
        btnB.setChecked(false);
        btnC.setChecked(false);
        btnD.setChecked(false);

        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);

        btnA.setClickable(true);
        btnB.setClickable(true);
        btnC.setClickable(true);
        btnD.setClickable(true);

        btnA.setAlpha(1f);
        btnB.setAlpha(1f);
        btnC.setAlpha(1f);
        btnD.setAlpha(1f);

        btnA.getBackground().setTintList(null);
        btnB.getBackground().setTintList(null);
        btnC.getBackground().setTintList(null);
        btnD.getBackground().setTintList(null);
    }

    private void checkAnswer(RadioButton btn, int index) {
        CauHoi cauHoi = danhSachCauHoi.get(currentIndex);

        // ===== FIX CRASH: bấm đáp án không tồn tại =====
        if (cauHoi.getDapAn() == null || index >= cauHoi.getDapAn().size()) return;

        boolean isCorrect = cauHoi.getDapAn().get(index).isLaDapAnDung();

        if (isCorrect) {
            btn.getBackground().setTint(Color.parseColor("#4CAF50"));
            demCauDung++;
        } else {
            btn.getBackground().setTint(Color.parseColor("#FF4444"));
            demCauSai++;
        }

        tvGiaiThich.setVisibility(View.VISIBLE);
        khoaDapAn();
    }

    private void khoaDapAn() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);

        btnA.setClickable(false);
        btnB.setClickable(false);
        btnC.setClickable(false);
        btnD.setClickable(false);

        btnA.setAlpha(0.5f);
        btnB.setAlpha(0.5f);
        btnC.setAlpha(0.5f);
        btnD.setAlpha(0.5f);
    }

    private void hoanThanh() {
        Toast.makeText(this, "Bạn đã hoàn thành bài!", Toast.LENGTH_SHORT).show();
        tienTrinh.setSoCauDung(demCauDung);
        tienTrinh.setSoCauDaLam(demCauDung + demCauSai);
        tienTrinh.setDiemDatDuoc(demCauDung * diemThuong);

        if (tienTrinh.getSoCauDung() == tienTrinh.getSoCauDaLam())
            tienTrinh.setDaHoanThanh(1);

        taoTienTrinh();
    }

    private void taoTienTrinh() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.taoTienTrinh(tienTrinh).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Intent data = new Intent();
                data.putExtra("MA_HOAT_DONG", maHoatDong);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Intent data = new Intent();
                data.putExtra("MA_HOAT_DONG", maHoatDong);
                setResult(RESULT_OK, data);

                Toast.makeText(TracNghiem2.this, "Lỗi gửi về be", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
