package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
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

public class CauHoi100 extends AppCompatActivity {

    private TextView tvQuestion, tvTitleTop;
    private Button btnAns1, btnAns2, btnAns3, btnAns4;

    private ApiService apiService;

    private List<List<CauHoiDapAnResponse>> nhomCauHoi = new ArrayList<>();
    private int index = 0;

    private int soCauDung = 0;
    private int soCauSai = 0;

    private long startTime; // thời gian bắt đầu làm bài

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_100cauhoi);

        mappingUI();

        apiService = RetrofitClient.getClient().create(ApiService.class);

        startTime = System.currentTimeMillis();

        loadData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void mappingUI() {
        tvQuestion = findViewById(R.id.tvQuestion);
        tvTitleTop = findViewById(R.id.tvTitleTop);

        btnAns1 = findViewById(R.id.btnAns1);
        btnAns2 = findViewById(R.id.btnAns2);
        btnAns3 = findViewById(R.id.btnAns3);
        btnAns4 = findViewById(R.id.btnAns4);
    }

    private void loadData() {
        apiService.getCauHoiBaiLam("TT001").enqueue(new Callback<List<CauHoiDapAnResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiDapAnResponse>> call, Response<List<CauHoiDapAnResponse>> res) {

                if (!res.isSuccessful() || res.body() == null) {
                    Toast.makeText(CauHoi100.this, "Không tải được câu hỏi!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CauHoi100.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void hienThiCauHoi() {

        if (index >= nhomCauHoi.size()) {
            moManHinhKetQua();
            return;
        }

        List<CauHoiDapAnResponse> cauHoi = nhomCauHoi.get(index);

        // HIỂN THỊ ĐỀ BÀI ĐÚNG
        tvQuestion.setText(cauHoi.get(0).getNoiDungCauHoi());

        tvTitleTop.setText("Câu hỏi " + (index + 1) + "/" + nhomCauHoi.size());

        btnAns1.setText(cauHoi.get(0).getNoiDungDapAn());
        btnAns2.setText(cauHoi.get(1).getNoiDungDapAn());
        btnAns3.setText(cauHoi.get(2).getNoiDungDapAn());
        btnAns4.setText(cauHoi.get(3).getNoiDungDapAn());

        resetButtonColor();

        btnAns1.setOnClickListener(v -> xuLyChon(btnAns1, cauHoi.get(0)));
        btnAns2.setOnClickListener(v -> xuLyChon(btnAns2, cauHoi.get(1)));
        btnAns3.setOnClickListener(v -> xuLyChon(btnAns3, cauHoi.get(2)));
        btnAns4.setOnClickListener(v -> xuLyChon(btnAns4, cauHoi.get(3)));

        btnAns1.setTag(cauHoi.get(0));
        btnAns2.setTag(cauHoi.get(1));
        btnAns3.setTag(cauHoi.get(2));
        btnAns4.setTag(cauHoi.get(3));
    }


    private void xuLyChon(Button btn, CauHoiDapAnResponse dapAn) {

        Button[] allButtons = {btnAns1, btnAns2, btnAns3, btnAns4};

        // Nếu người dùng chọn đúng
        if (dapAn.isLaDapAnDung()) {
            tintButton(btn, Color.parseColor("#4CAF50")); // Xanh lá
            soCauDung++;
        } else {
            // Tô đỏ nút người dùng vừa chọn
            tintButton(btn, Color.parseColor("#F44336"));
            soCauSai++;

            // Highlight đáp án đúng
            for (Button b : allButtons) {
                CauHoiDapAnResponse dap = (CauHoiDapAnResponse) b.getTag(); // lấy đáp án gán vào nút
                if (dap != null && dap.isLaDapAnDung()) {
                    tintButton(b, Color.parseColor("#4CAF50")); // tô xanh cho đáp án đúng
                    break;
                }
            }
        }

        // Đợi 1.5 giây rồi chuyển câu
        new Handler().postDelayed(() -> {
            index++;
            hienThiCauHoi();
        }, 1500);
    }


    private void resetButtonColor() {
        btnAns1.setBackgroundResource(R.drawable.answer_bg);
        btnAns2.setBackgroundResource(R.drawable.answer_bg);
        btnAns3.setBackgroundResource(R.drawable.answer_bg);
        btnAns4.setBackgroundResource(R.drawable.answer_bg);
    }

    // 📌 HÀM MỚI — đổi màu nhưng GIỮ BO GÓC answer_bg
    private void tintButton(Button btn, int color) {
        Drawable drawable = DrawableCompat.wrap(btn.getBackground().mutate());
        DrawableCompat.setTint(drawable, color);
        btn.setBackground(drawable);
    }

    private void moManHinhKetQua() {

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int tongCau = nhomCauHoi.size();
        int diem = soCauDung * 5;

        String maNguoiDung = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("MA_NGUOI_DUNG", null);

        String maHoatDong = "TT001";

        apiService.hoanThanh(
                maNguoiDung,
                maHoatDong,
                soCauDung,
                tongCau,
                diem
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                Intent intent = new Intent(CauHoi100.this, KetQuaActivity.class);
                intent.putExtra("SO_CAU_DUNG", soCauDung);
                intent.putExtra("SO_CAU_SAI", soCauSai);
                intent.putExtra("TONG_CAU", tongCau);
                intent.putExtra("THOI_GIAN", duration);
                intent.putExtra("TOTAL_SCORE", diem);

                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CauHoi100.this, "Không thể cập nhật tiến trình!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CauHoi100.this, KetQuaActivity.class);
                intent.putExtra("SO_CAU_DUNG", soCauDung);
                intent.putExtra("SO_CAU_SAI", soCauSai);
                intent.putExtra("TONG_CAU", tongCau);
                intent.putExtra("THOI_GIAN", duration);
                intent.putExtra("TOTAL_SCORE", diem);

                startActivity(intent);
                finish();
            }
        });
    }
}
