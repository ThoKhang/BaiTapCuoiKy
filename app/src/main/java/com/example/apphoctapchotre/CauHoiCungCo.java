package com.example.apphoctapchotre;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.model.CauHoiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CauHoiCungCo extends AppCompatActivity {

    private TextView tvNoiDungCauHoi;
    private TextView tvQuestionNumber;

    private Button btnA, btnB, btnC, btnD, btnTiepTuc;
    private TextView tvDapAnA, tvDapAnB, tvDapAnC, tvDapAnD;

    private List<CauHoiResponse> danhSachCauHoi = new ArrayList<>();
    private int index = 0;
    private int maMonHoc;
    private String tieuDePhu;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_trac_nghiem);

        // Ánh xạ view
        tvNoiDungCauHoi = findViewById(R.id.tvCauHoi);
        tvDapAnA = findViewById(R.id.tvDapAnA);
        tvDapAnB = findViewById(R.id.tvDapAnB);
        tvDapAnC = findViewById(R.id.tvDapAnC);
        tvDapAnD = findViewById(R.id.tvDapAnD);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvQuestionNumber = findViewById(R.id.tv_question_number);

        TextView tvTieuDePhu = findViewById(R.id.tvTieuDePhu);

        // Nhận dữ liệu từ Intent
        maMonHoc = getIntent().getIntExtra("maMonHoc", 1);
        tieuDePhu = getIntent().getStringExtra("tieuDePhu");

        if (tvTieuDePhu != null && tieuDePhu != null)
            tvTieuDePhu.setText(tieuDePhu);

        // Tạo Retrofit API
        api = RetrofitClient.getClient().create(ApiService.class);

        // Gọi API lấy câu hỏi theo môn và tiêu đề phụ
        taiCauHoiTheoMonVaTieuDePhu();

        ImageView btnQuayLai = findViewById(R.id.quayLai);
        btnQuayLai.setOnClickListener(v -> onBackPressed());
    }

    private void taiCauHoiTheoMonVaTieuDePhu() {
        maMonHoc = getIntent().getIntExtra("maMonHoc", 1);
        tieuDePhu = getIntent().getStringExtra("tieuDePhu");
        int maTieuDePhuIntent = getIntent().getIntExtra("maTieuDePhu", 0);

        Log.d("DEBUG_INTENT", "maMonHoc=" + maMonHoc
                + ", tieuDePhu=" + tieuDePhu
                + ", maTieuDePhu=" + maTieuDePhuIntent);

        if (tieuDePhu == null || tieuDePhu.trim().isEmpty()) {
            Toast.makeText(this, "Thiếu tiêu đề phụ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        api.getCauHoiTheoTieuDePhu(maMonHoc, tieuDePhu)
                .enqueue(new Callback<List<CauHoiResponse>>() {
                    @Override
                    public void onResponse(Call<List<CauHoiResponse>> call, Response<List<CauHoiResponse>> response) {
                        Log.d("API_DEBUG", "Response code: " + response.code());
                        if (!response.isSuccessful()) {
                            Toast.makeText(CauHoiCungCo.this, "Lỗi API code: " + response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<CauHoiResponse> list = response.body();
                        if (list == null || list.isEmpty()) {
                            Toast.makeText(CauHoiCungCo.this, "Không có câu hỏi phù hợp!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        danhSachCauHoi = list;
                        index = 0;
                        hienThiCauHoi();
                    }

                    @Override
                    public void onFailure(Call<List<CauHoiResponse>> call, Throwable t) {
                        Toast.makeText(CauHoiCungCo.this, "Lỗi tải câu hỏi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("API_DEBUG", "Error", t);
                    }
                });
    }

    private void hienThiCauHoi() {
        if (danhSachCauHoi == null || danhSachCauHoi.isEmpty()) return;

        Map<Integer, List<CauHoiResponse>> nhomCauHoi = danhSachCauHoi.stream()
                .collect(Collectors.groupingBy(CauHoiResponse::getMaCauHoi));

        List<Integer> danhSachMaCauHoi = new ArrayList<>(nhomCauHoi.keySet());
        if (index >= danhSachMaCauHoi.size()) {
            Toast.makeText(this, "🎉 Bạn đã hoàn thành tất cả câu hỏi!", Toast.LENGTH_SHORT).show();

            int maNguoiDung = getSharedPreferences("UserSession", MODE_PRIVATE)
                    .getInt("maNguoiDung", 1);
            int maTieuDePhu = getIntent().getIntExtra("maTieuDePhu", 0);
            if (maTieuDePhu == 0) {
                Toast.makeText(this, "Thiếu thông tin tiêu đề phụ!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            api.hoanThanhTieuDePhu(maNguoiDung, maTieuDePhu).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CauHoiCungCo.this, "✅ Hoàn thành tiêu đề phụ! +50 điểm!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("maTieuDePhuHoanThanh", maTieuDePhu);
                        setResult(RESULT_OK, intent);
                    } else {
                        Toast.makeText(CauHoiCungCo.this, "Đã hoàn thành trước đó hoặc lỗi thưởng điểm!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CauHoiCungCo.this, "Lỗi khi gửi thưởng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            return;
        }

        int maCauHoiHienTai = danhSachMaCauHoi.get(index);
        List<CauHoiResponse> nhom = nhomCauHoi.get(maCauHoiHienTai);
        if (nhom == null || nhom.isEmpty()) return;

        CauHoiResponse cauHoi = nhom.get(0);
        int tongSoCauHoi = nhomCauHoi.size();

        tvQuestionNumber.setText("Câu " + (index + 1) + "/" + tongSoCauHoi);
        tvNoiDungCauHoi.setText("Câu " + (index + 1) + ": " + cauHoi.getCauHoi());

        // Hiển thị các đáp án
        for (CauHoiResponse ch : nhom) {
            String dapAn = ch.getDapAn().replace("Đáp án đúng:", "").trim();
            switch (ch.getNhanLuaChon()) {
                case "A":
                    tvDapAnA.setText("A. " + dapAn);
                    break;
                case "B":
                    tvDapAnB.setText("B. " + dapAn);
                    break;
                case "C":
                    tvDapAnC.setText("C. " + dapAn);
                    break;
                case "D":
                    tvDapAnD.setText("D. " + dapAn);
                    break;
            }
        }

        btnA.setText("A");
        btnB.setText("B");
        btnC.setText("C");
        btnD.setText("D");
        resetButtonColors();

        // 🔹 Vô hiệu hóa nút Tiếp Tục ban đầu
        btnTiepTuc.setEnabled(false);
        setButtonTint(btnTiepTuc, Color.GRAY);

        // Biến cờ: chỉ cần chọn đúng 1 lần là được phép nhấn tiếp tục
        final boolean[] daChonDung = {false};

        View.OnClickListener listener = v -> {
            Button btn = (Button) v;
            String nhan = btn.getText().toString();

            boolean laDung = nhom.stream()
                    .anyMatch(x -> x.getNhanLuaChon().equals(nhan) && x.isLaDung());

            if (laDung) {
                tintButton(btn, Color.GREEN);
                daChonDung[0] = true;
            } else {
                tintButton(btn, Color.RED);
            }

            // ✅ Nếu đã từng chọn đúng, bật nút Tiếp Tục (màu #5699CF)
            if (daChonDung[0]) {
                btnTiepTuc.setEnabled(true);
                setButtonTint(btnTiepTuc, Color.parseColor("#5699CF"));
            } else {
                btnTiepTuc.setEnabled(false);
                setButtonTint(btnTiepTuc, Color.GRAY);
            }
        };

        btnA.setOnClickListener(listener);
        btnB.setOnClickListener(listener);
        btnC.setOnClickListener(listener);
        btnD.setOnClickListener(listener);

        btnTiepTuc.setOnClickListener(v -> {
            index++;
            hienThiCauHoi();
        });
    }

    /** Giữ bo góc khi tô màu cho button */
    private void setButtonTint(Button button, int color) {
        Drawable background = button.getBackground().mutate();
        Drawable wrapped = DrawableCompat.wrap(background);
        DrawableCompat.setTint(wrapped, color);
        button.setBackground(wrapped);
    }




    // 🔹 Giữ lại bo tròn gốc bằng cách gán lại drawable thay vì setBackgroundColor
    private void resetButtonColors() {
        // Gán lại drawable gốc (bo tròn)
        btnA.setBackgroundResource(R.drawable.option_button_bg);
        btnB.setBackgroundResource(R.drawable.option_button_bg);
        btnC.setBackgroundResource(R.drawable.option_button_bg);
        btnD.setBackgroundResource(R.drawable.option_button_bg);

        // Xóa tint còn sót lại (bắt buộc để không giữ màu cũ)
        clearTint(btnA);
        clearTint(btnB);
        clearTint(btnC);
        clearTint(btnD);
    }

    private void clearTint(Button btn) {
        Drawable bg = btn.getBackground().mutate();
        Drawable wrapped = DrawableCompat.wrap(bg);
        DrawableCompat.setTintList(wrapped, null); // Xóa tint về mặc định
        btn.setBackground(wrapped);
    }


    // 🔹 Hàm tô màu mà vẫn giữ bo góc
    private void tintButton(Button btn, int color) {
        Drawable wrapped = DrawableCompat.wrap(btn.getBackground().mutate());
        DrawableCompat.setTint(wrapped, color);
        btn.setBackground(wrapped);
    }
}
