package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

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

public class CauHoiActivity extends AppCompatActivity {

    private TextView tvCauHoi, tvQuestionNumber, tvDapAnA, tvDapAnB, tvDapAnC, tvDapAnD;
    private Button btnA, btnB, btnC, btnD, btnTiepTuc;

    private ApiService api;
    private List<List<CauHoiDapAnResponse>> nhomCauHoi = new ArrayList<>();
    private int index = 0;

    private String maBaiLam;
    private String maHoatDong;
    private String maNguoiDung;

    private int soCauDung = 0;     // tổng số câu đúng người dùng chọn
    private int tongCauHoi = 0;   // gửi qua backend

    private boolean daChonDapAn = false; // để tránh click nhiều lần trong 1 câu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        bindUI();

        api = RetrofitClient.getClient().create(ApiService.class);

        maBaiLam = getIntent().getStringExtra("maBaiLam");
        maHoatDong = getIntent().getStringExtra("maHoatDong");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung");

        if (maBaiLam == null || maHoatDong == null || maNguoiDung == null) {
            Toast.makeText(this, "Thiếu dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnTiepTuc.setEnabled(false);
        tintButton(btnTiepTuc, Color.GRAY);

        loadData();

        findViewById(R.id.quayLai).setOnClickListener(v -> finish());
    }

    private void bindUI() {
        tvCauHoi = findViewById(R.id.tvCauHoi);
        tvQuestionNumber = findViewById(R.id.tv_question_number);

        tvDapAnA = findViewById(R.id.tvDapAnA);
        tvDapAnB = findViewById(R.id.tvDapAnB);
        tvDapAnC = findViewById(R.id.tvDapAnC);
        tvDapAnD = findViewById(R.id.tvDapAnD);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
    }

    private void loadData() {

        api.getCauHoiBaiLam(maBaiLam).enqueue(new Callback<List<CauHoiDapAnResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiDapAnResponse>> call, Response<List<CauHoiDapAnResponse>> res) {

                if (!res.isSuccessful() || res.body() == null) {
                    Toast.makeText(CauHoiActivity.this, "Không tải được câu hỏi!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                Map<String, List<CauHoiDapAnResponse>> grouped =
                        res.body().stream().collect(Collectors.groupingBy(CauHoiDapAnResponse::getMaCauHoi));

                nhomCauHoi = new ArrayList<>(grouped.values());

                tongCauHoi = nhomCauHoi.size();

                index = 0;
                hienThiCauHoi();
            }

            @Override
            public void onFailure(Call<List<CauHoiDapAnResponse>> call, Throwable t) {
                Toast.makeText(CauHoiActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void hienThiCauHoi() {

        if (index >= nhomCauHoi.size()) {
            hoanThanhBaiLam();
            return;
        }

        daChonDapAn = false;

        List<CauHoiDapAnResponse> cauHoi = nhomCauHoi.get(index);

        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + tongCauHoi);
        tvCauHoi.setText(cauHoi.get(0).getNoiDungCauHoi());

        tvDapAnA.setText("A. " + cauHoi.get(0).getNoiDungDapAn());
        tvDapAnB.setText("B. " + cauHoi.get(1).getNoiDungDapAn());
        tvDapAnC.setText("C. " + cauHoi.get(2).getNoiDungDapAn());
        tvDapAnD.setText("D. " + cauHoi.get(3).getNoiDungDapAn());

        btnA.setTag(cauHoi.get(0));
        btnB.setTag(cauHoi.get(1));
        btnC.setTag(cauHoi.get(2));
        btnD.setTag(cauHoi.get(3));

        resetButtonColors();
        btnTiepTuc.setEnabled(false);
        tintButton(btnTiepTuc, Color.GRAY);

        Button[] arr = {btnA, btnB, btnC, btnD};

        for (Button b : arr) {
            b.setOnClickListener(v -> {

                if (daChonDapAn) return; // tránh chọn nhiều lần

                daChonDapAn = true;
                CauHoiDapAnResponse dap = (CauHoiDapAnResponse) b.getTag();

                if (dap.isLaDapAnDung()) {
                    tintButton(b, Color.GREEN);
                    soCauDung++;  // chỉ cộng khi người dùng CHỌN đúng
                } else {
                    tintButton(b, Color.RED);
                }

                btnTiepTuc.setEnabled(true);
                tintButton(btnTiepTuc, Color.parseColor("#5699CF"));
            });
        }

        btnTiepTuc.setOnClickListener(v -> {
            index++;
            hienThiCauHoi();
        });
    }

    // ======================================================
    // GỌI API HOÀN THÀNH
    // ======================================================
    private void hoanThanhBaiLam() {

        int diem = soCauDung * 5;

        api.hoanThanh(
                maNguoiDung,
                maHoatDong,
                soCauDung,
                tongCauHoi,
                diem
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> res) {

                Intent data = new Intent();
                data.putExtra("maHoatDongHoanThanh", maHoatDong);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CauHoiActivity.this, "Không thể cập nhật tiến trình!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // ================= Hiệu ứng màu =================
    private void tintButton(Button btn, int color) {
        Drawable wrapped = DrawableCompat.wrap(btn.getBackground().mutate());
        DrawableCompat.setTint(wrapped, color);
        btn.setBackground(wrapped);
    }

    private void resetButtonColors() {
        btnA.setBackgroundResource(R.drawable.option_button_bg);
        btnB.setBackgroundResource(R.drawable.option_button_bg);
        btnC.setBackgroundResource(R.drawable.option_button_bg);
        btnD.setBackgroundResource(R.drawable.option_button_bg);

        clearTint(btnA);
        clearTint(btnB);
        clearTint(btnC);
        clearTint(btnD);
    }

    private void clearTint(Button btn) {
        Drawable wrapped = DrawableCompat.wrap(btn.getBackground().mutate());
        DrawableCompat.setTintList(wrapped, null);
        btn.setBackground(wrapped);
    }
}
