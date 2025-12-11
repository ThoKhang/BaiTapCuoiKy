package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.DATA.model.CauHoiDapAnResponse;
import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CauHoiThuThach extends AppCompatActivity {

    private TextView tvCauHoi, tvSoCau, tvThoiGian, tvCauDung;
    private RelativeLayout option1, option2, option3, option4;
    private TextView dapan1, dapan2, dapan3, dapan4;
    private ImageView tick1, tick2, tick3, tick4;

    private ApiService apiService;

    private List<List<CauHoiDapAnResponse>> nhomCauHoi = new ArrayList<>();
    private int index = 0;

    private int soCauDung = 0;
    private int soCauSai = 0;

    private long startTime;
    private CountDownTimer timer;

    private Handler blinkHandler = new Handler();
    private boolean isBlinking = false;

    private final int TIME_PER_QUESTION = 20000; // 20 GIÂY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau_hoi_thu_thach);

        mappingUI();

        apiService = RetrofitClient.getClient().create(ApiService.class);
        startTime = System.currentTimeMillis();

        loadData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void mappingUI() {

        tvCauHoi = findViewById(R.id.tvCauHoi);
        tvSoCau = findViewById(R.id.tvSoCau);
        tvThoiGian = findViewById(R.id.tvThoiGian);
        tvCauDung = findViewById(R.id.tvCauDung);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        dapan1 = findViewById(R.id.dapan1);
        dapan2 = findViewById(R.id.dapan2);
        dapan3 = findViewById(R.id.dapan3);
        dapan4 = findViewById(R.id.dapan4);

        tick1 = findViewById(R.id.tick1);
        tick2 = findViewById(R.id.tick2);
        tick3 = findViewById(R.id.tick3);
        tick4 = findViewById(R.id.tick4);
    }

    private void capNhatCauDung() {
        tvCauDung.setText(" " + soCauDung);
    }

    private void loadData() {
        apiService.getCauHoiBaiLam("TT002").enqueue(new Callback<List<CauHoiDapAnResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiDapAnResponse>> call, Response<List<CauHoiDapAnResponse>> res) {

                if (!res.isSuccessful() || res.body() == null) {
                    Toast.makeText(CauHoiThuThach.this, "Không tải được câu hỏi!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                Map<String, List<CauHoiDapAnResponse>> grouped =
                        res.body().stream().collect(Collectors.groupingBy(CauHoiDapAnResponse::getMaCauHoi));

                nhomCauHoi = new ArrayList<>(grouped.values());
                index = 0;

                hienThiCauHoi();
            }

            @Override
            public void onFailure(Call<List<CauHoiDapAnResponse>> call, Throwable t) {
                Toast.makeText(CauHoiThuThach.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void hienThiCauHoi() {

        if (index >= nhomCauHoi.size()) {
            ketThucThuThach();
            return;
        }

        List<CauHoiDapAnResponse> cauHoi = nhomCauHoi.get(index);

        tvCauHoi.setText(cauHoi.get(0).getNoiDungCauHoi());
        tvSoCau.setText("Câu hỏi " + (index + 1) + "/" + nhomCauHoi.size());

        dapan1.setText(cauHoi.get(0).getNoiDungDapAn());
        dapan2.setText(cauHoi.get(1).getNoiDungDapAn());
        dapan3.setText(cauHoi.get(2).getNoiDungDapAn());
        dapan4.setText(cauHoi.get(3).getNoiDungDapAn());

        resetOptions();
        capNhatCauDung();
        enableAllOptions();

        stopBlinkEffect(); // reset blink
        tvThoiGian.setTextColor(Color.GREEN);
        tvThoiGian.setVisibility(View.VISIBLE);

        startTimer();

        option1.setOnClickListener(v -> xuLyChon(option1, cauHoi.get(0), tick1));
        option2.setOnClickListener(v -> xuLyChon(option2, cauHoi.get(1), tick2));
        option3.setOnClickListener(v -> xuLyChon(option3, cauHoi.get(2), tick3));
        option4.setOnClickListener(v -> xuLyChon(option4, cauHoi.get(3), tick4));
    }

    private void startTimer() {

        if (timer != null) timer.cancel();

        timer = new CountDownTimer(TIME_PER_QUESTION, 1000) {
            @Override
            public void onTick(long ms) {

                long seconds = ms / 1000;
                tvThoiGian.setText(String.valueOf(seconds));

                // ĐỔI MÀU THEO THỜI GIAN
                if (seconds <= 5) {
                    tvThoiGian.setTextColor(Color.RED);
                } else if (seconds <= 10) {
                    tvThoiGian.setTextColor(Color.parseColor("#FFC107"));
                } else {
                    tvThoiGian.setTextColor(Color.GREEN);
                }

                // NHẤP NHÁY 3 GIÂY CUỐI (BẰNG CODE)
                if (seconds <= 3 && !isBlinking) {
                    startBlinkEffect();
                }
            }

            @Override
            public void onFinish() {

                stopBlinkEffect();

                soCauSai++;
                disableAllOptions();

                new android.app.AlertDialog.Builder(CauHoiThuThach.this)
                        .setTitle("Hết Giờ!")
                        .setMessage("Bạn đã hết thời gian mất rồi:((")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> ketThucThuThach())
                        .show();
            }

        }.start();
    }

    // HIỆU ỨNG NHẤP NHÁY BẰNG CODE
    private void startBlinkEffect() {
        isBlinking = true;
        blinkHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isBlinking) return;

                tvThoiGian.setVisibility(tvThoiGian.getVisibility() == View.VISIBLE
                        ? View.INVISIBLE : View.VISIBLE);

                blinkHandler.postDelayed(this, 300);
            }
        });
    }

    private void stopBlinkEffect() {
        isBlinking = false;
        blinkHandler.removeCallbacksAndMessages(null);
        tvThoiGian.setVisibility(View.VISIBLE);
    }

    private void resetOptions() {

        option1.setBackgroundResource(R.drawable.bg_rounded_white);
        option2.setBackgroundResource(R.drawable.bg_rounded_white);
        option3.setBackgroundResource(R.drawable.bg_rounded_white);
        option4.setBackgroundResource(R.drawable.bg_rounded_white);

        tick1.setVisibility(View.INVISIBLE);
        tick2.setVisibility(View.INVISIBLE);
        tick3.setVisibility(View.INVISIBLE);
        tick4.setVisibility(View.INVISIBLE);
    }

    private void disableAllOptions() {
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        option4.setClickable(false);
    }

    private void enableAllOptions() {
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);
        option4.setClickable(true);
    }

    private void xuLyChon(RelativeLayout layout, CauHoiDapAnResponse dapAn, ImageView tick) {

        timer.cancel();
        stopBlinkEffect();
        disableAllOptions();

        if (!dapAn.isLaDapAnDung()) {

            layout.setBackgroundResource(R.drawable.bg_red);
            tick.setImageResource(R.drawable.remove);
            tick.setVisibility(View.VISIBLE);

            soCauSai++;

            ketThucThuThach();
            return;
        }

        layout.setBackgroundResource(R.drawable.bg_green);
        tick.setImageResource(R.drawable.accept);
        tick.setVisibility(View.VISIBLE);

        soCauDung++;
        capNhatCauDung();

        new Handler().postDelayed(() -> {
            index++;
            hienThiCauHoi();
        }, 800);
    }

    private void ketThucThuThach() {

        if (timer != null) timer.cancel();
        stopBlinkEffect();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int tongCau = nhomCauHoi.size();
        int diem = soCauDung * 5;

        String maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("MA_NGUOI_DUNG", null);

        String maHoatDong = "TT002";

        apiService.hoanThanh(
                maNguoiDung,
                maHoatDong,
                soCauDung,
                tongCau,
                diem
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                moManHinhKetQua(duration, tongCau, diem);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CauHoiThuThach.this, "Không thể cập nhật tiến trình!", Toast.LENGTH_SHORT).show();
                moManHinhKetQua(duration, tongCau, diem);
            }
        });
    }

    private void moManHinhKetQua(long duration, int tongCau, int diem) {

        Intent intent = new Intent(CauHoiThuThach.this, KetQuaThuThachDaHoc.class);
        intent.putExtra("SO_CAU_DUNG", soCauDung);
        intent.putExtra("SO_CAU_SAI", soCauSai);
        intent.putExtra("TONG_CAU", tongCau);
        intent.putExtra("THOI_GIAN", duration);
        intent.putExtra("TOTAL_SCORE", diem);

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 150);
    }
}
