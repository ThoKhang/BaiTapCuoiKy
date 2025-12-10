package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
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

    // UI
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

        // TextView đếm số câu đúng
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

    // Hàm cập nhật số câu đúng
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
        startTimer();
        capNhatCauDung(); // cập nhật mỗi lần đổi câu

        enableAllOptions();

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
                tvThoiGian.setText(String.valueOf(ms / 1000));
            }

            @Override
            public void onFinish() {
                soCauSai++;
                disableAllOptions();
                ketThucThuThach();
            }
        }.start();
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
        disableAllOptions();

        // Sai → kết thúc thử thách ngay lập tức
        if (!dapAn.isLaDapAnDung()) {

            layout.setBackgroundResource(R.drawable.bg_red);
            tick.setImageResource(R.drawable.remove);
            tick.setVisibility(View.VISIBLE);

            soCauSai++;

            ketThucThuThach();
            return;
        }

        // Đúng → chuyển câu
        layout.setBackgroundResource(R.drawable.bg_green);
        tick.setImageResource(R.drawable.accept);
        tick.setVisibility(View.VISIBLE);

        soCauDung++;
        capNhatCauDung(); // cập nhật ngay khi trả lời đúng

        new Handler().postDelayed(() -> {
            index++;
            hienThiCauHoi();
        }, 800);
    }

    private void ketThucThuThach() {

        if (timer != null) timer.cancel();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Intent intent = new Intent(CauHoiThuThach.this, KetQuaThuThachDaHoc.class);
        intent.putExtra("SO_CAU_DUNG", soCauDung);
        intent.putExtra("SO_CAU_SAI", soCauSai);
        intent.putExtra("TONG_CAU", nhomCauHoi.size());
        intent.putExtra("THOI_GIAN", duration);
        intent.putExtra("TOTAL_SCORE", soCauDung * 5);

        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 150);
    }
}
