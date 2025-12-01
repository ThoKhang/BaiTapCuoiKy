package com.example.apphoctapchotre.Activity.MonHoc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.model.ui.HoanThanhLyThuyetRequest;  // ĐÃ ĐỔI TÊN ĐÚNG
import com.example.apphoctapchotre.model.ui.LyThuyetDetailResponse;
import com.google.android.material.button.MaterialButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoiDungLyThuyet extends AppCompatActivity {

    private TextView txtTieuDe, txtNoiDung, back;
    private ImageView imgVuongMieng;
    private MaterialButton btnHieuRoi;

    private String email, maHoatDong;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noi_dung_lt);

        txtTieuDe = findViewById(R.id.txtTieuDe);
        txtNoiDung = findViewById(R.id.txtNoiDung);
        back = findViewById(R.id.back);
        imgVuongMieng = findViewById(R.id.imgVuongMieng);
        btnHieuRoi = findViewById(R.id.btnHieuRoi);

        email = getIntent().getStringExtra("email");
        maHoatDong = getIntent().getStringExtra("maHoatDong");

        if (email == null || maHoatDong == null) {
            Toast.makeText(this, "Lỗi: Không nhận được thông tin bài học!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        api = RetrofitClient.getClient().create(ApiService.class);
        back.setOnClickListener(v -> finish());
        loadChiTiet();

        // NÚT HIỂU RỒI – ĐÃ KHỚP 100% VỚI BACKEND
        btnHieuRoi.setOnClickListener(v -> {
            btnHieuRoi.setEnabled(false);

            // ĐÃ DÙNG ĐÚNG TÊN CLASS NHƯ BACKEND
            HoanThanhLyThuyetRequest request = new HoanThanhLyThuyetRequest(maHoatDong, email);

            api.hoanThanh(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        imgVuongMieng.setVisibility(ImageView.VISIBLE);
                        btnHieuRoi.setText("ĐÃ HIỂU RỒI!");
                        Toast.makeText(NoiDungLyThuyet.this, "Giỏi quá! Bé đã hiểu bài rồi! +10 điểm", Toast.LENGTH_LONG).show();
                        setResult(RESULT_OK);
                    } else {
                        btnHieuRoi.setEnabled(true);
                        Toast.makeText(NoiDungLyThuyet.this, "Lỗi lưu tiến độ, thử lại nhé!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnHieuRoi.setEnabled(true);
                    Toast.makeText(NoiDungLyThuyet.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void loadChiTiet() {
        api.getChiTietLyThuyet(maHoatDong, email)
                .enqueue(new Callback<LyThuyetDetailResponse>() {
                    @Override
                    public void onResponse(Call<LyThuyetDetailResponse> call, Response<LyThuyetDetailResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(NoiDungLyThuyet.this, "Không lấy được dữ liệu", Toast.LENGTH_LONG).show();
                            return;
                        }
                        LyThuyetDetailResponse data = response.body();
                        txtTieuDe.setText(data.getTieuDe());
                        txtNoiDung.setText(data.getNoiDung());

                        if (data.isDaHoanThanh()) {
                            imgVuongMieng.setVisibility(ImageView.VISIBLE);
                            btnHieuRoi.setText("ĐÃ HIỂU RỒI!");
                            btnHieuRoi.setEnabled(false);
                        } else {
                            imgVuongMieng.setVisibility(ImageView.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<LyThuyetDetailResponse> call, Throwable t) {
                        Toast.makeText(NoiDungLyThuyet.this, "Lỗi mạng!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}