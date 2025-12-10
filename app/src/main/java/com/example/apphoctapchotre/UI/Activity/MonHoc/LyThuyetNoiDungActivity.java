package com.example.apphoctapchotre.UI.Activity.MonHoc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.remote.ApiService;
import com.example.apphoctapchotre.DATA.remote.RetrofitClient;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.LyThuyetNoiDungViewModel;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyThuyetNoiDungActivity extends AppCompatActivity {

    private TextView tvTieuDe, tvNoiDung;
    private MaterialButton btnHieuRoi;

    private String maHoatDong, maNguoiDung;
    private int tongDiemToiDa;

    private LyThuyetNoiDungViewModel viewModel;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ly_thuyet_noi_dung);

        tvTieuDe = findViewById(R.id.tv_tieude);
        tvNoiDung = findViewById(R.id.tv_noidung);
        btnHieuRoi = findViewById(R.id.btnHieuRoi);

        api = RetrofitClient.getClient().create(ApiService.class);
        viewModel = new ViewModelProvider(this).get(LyThuyetNoiDungViewModel.class);

        maHoatDong = getIntent().getStringExtra("maHoatDong");
        maNguoiDung = getIntent().getStringExtra("maNguoiDung");

        if (maHoatDong == null || maNguoiDung == null) {
            Toast.makeText(this, "Lỗi dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.loadNoiDung(maHoatDong);
        viewModel.getNoiDung().observe(this, data -> {
            if (data == null) {
                Toast.makeText(this, "Không tải được nội dung!", Toast.LENGTH_SHORT).show();
                return;
            }
            tvTieuDe.setText(data.getTieuDe());
            tvNoiDung.setText(data.getMoTa());
            tongDiemToiDa = data.getTongDiemToiDa();
        });

        btnHieuRoi.setOnClickListener(v -> hoanThanh());

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

    private void hoanThanh() {
        btnHieuRoi.setEnabled(false);
        btnHieuRoi.setText("Đang lưu...");

        api.hoanThanhLyThuyet(maNguoiDung, maHoatDong, tongDiemToiDa).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(LyThuyetNoiDungActivity.this, "Hoàn thành bài học!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LyThuyetNoiDungActivity.this, "Lỗi mạng!", Toast.LENGTH_SHORT).show();
                btnHieuRoi.setEnabled(true);
                btnHieuRoi.setText("Con hiểu rồi !");
            }
        });
    }
}