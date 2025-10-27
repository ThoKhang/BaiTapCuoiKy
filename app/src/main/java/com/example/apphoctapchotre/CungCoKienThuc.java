package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.model.BaiKiemTra;
import com.example.apphoctapchotre.model.NguoiDung;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CungCoKienThuc extends AppCompatActivity {

    private TextView tvToan, tvTiengViet;
    private LinearLayout layoutToan, layoutTiengViet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cung_co_kien_thuc);

        // Ánh xạ
        tvToan = findViewById(R.id.tvToan);
        tvTiengViet = findViewById(R.id.tvTiengViet);
        layoutToan = findViewById(R.id.layoutToan);
        layoutTiengViet = findViewById(R.id.layoutTiengViet);

        int maNguoiDung = 1;
        taiTongBaiHocTheoMon(maNguoiDung);

        layoutToan.setOnClickListener(v -> {
            Intent intent = new Intent(CungCoKienThuc.this, CungCoMonHoc.class);
            intent.putExtra("maMonHoc", 1);
            intent.putExtra("tenMonHoc", "Toán");
            intent.putExtra("bgRes", R.drawable.bg_banner_green);
            startActivity(intent);
        });

        layoutTiengViet.setOnClickListener(v -> {
            Intent intent = new Intent(CungCoKienThuc.this, CungCoMonHoc.class);
            intent.putExtra("maMonHoc", 2);
            intent.putExtra("tenMonHoc", "Tiếng Việt");
            intent.putExtra("bgRes", R.drawable.bg_banner_blue);
            startActivity(intent);
        });

        ImageView btnQuayLai = findViewById(R.id.quayLai);
        btnQuayLai.setOnClickListener(v -> {
            onBackPressed(); // Quay lại trang trước
        });

    }

    private void taiTongBaiHocTheoMon(int maNguoiDung) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<NguoiDung> call = apiService.getNguoiDungTongBaiHocTheoMon(maNguoiDung);

        call.enqueue(new Callback<NguoiDung>() {
            @Override
            public void onResponse(Call<NguoiDung> call, Response<NguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NguoiDung nguoiDung = response.body();
                    tvToan.setText("Tiến độ: " + nguoiDung.getToanTienDo());
                    tvTiengViet.setText("Tiến độ: " + nguoiDung.getTiengVietTienDo());
                } else {
                    Toast.makeText(CungCoKienThuc.this, "Không có dữ liệu từ server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NguoiDung> call, Throwable t) {
                Toast.makeText(CungCoKienThuc.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
