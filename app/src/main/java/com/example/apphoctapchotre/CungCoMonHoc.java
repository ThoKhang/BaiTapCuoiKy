package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.Api.ApiService;
import com.example.apphoctapchotre.Api.RetrofitClient;
import com.example.apphoctapchotre.model.BaiKiemTra;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CungCoMonHoc extends AppCompatActivity {

    private ListView listView;
    private BaiKiemTraAdapter adapter;
    private int maMonHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_hoc);

        listView = findViewById(R.id.listViewLyThuyet);
        maMonHoc = getIntent().getIntExtra("maMonHoc", 1);

        TextView tvTenMonHoc = findViewById(R.id.tvTenMonHoc);
        String tenMonHoc = getIntent().getStringExtra("tenMonHoc");
        if (tenMonHoc != null) {
            tvTenMonHoc.setText("Môn học: " + tenMonHoc);
        }

        ImageView btnQuayLai = findViewById(R.id.quayLai);
        btnQuayLai.setOnClickListener(v -> onBackPressed());

        taiDanhSachBaiCungCo(maMonHoc);
    }

    private void taiDanhSachBaiCungCo(int maMonHoc) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        int maNguoiDung = getSharedPreferences("UserSession", MODE_PRIVATE)
                .getInt("maNguoiDung", 1);
        Call<List<BaiKiemTra>> call = api.getBaiKiemTraCungCoTheoMon(maMonHoc, maNguoiDung);
        call.enqueue(new Callback<List<BaiKiemTra>>() {
            @Override
            public void onResponse(Call<List<BaiKiemTra>> call, Response<List<BaiKiemTra>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CungCoMonHoc.this,
                            "Lỗi server: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "Response code: " + response.code());
                    return;
                }

                List<BaiKiemTra> danhSach = response.body();
                if (danhSach == null || danhSach.isEmpty()) {
                    Toast.makeText(CungCoMonHoc.this, "Không có bài củng cố!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("API_CHECK", "Số bài củng cố: " + danhSach.size());

                // 🔹 Nếu muốn log thử tiêu đề phụ để debug
                for (BaiKiemTra bkt : danhSach) {
                    Log.d("API_CHECK", "ID=" + bkt.getMaBaiKiemTra()
                            + ", Tiêu đề phụ=" + bkt.getTieuDePhu());
                }

                adapter = new BaiKiemTraAdapter(CungCoMonHoc.this, danhSach, maMonHoc);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    BaiKiemTra bai = danhSach.get(position);

                    if (bai == null) {
                        Toast.makeText(CungCoMonHoc.this, "Không lấy được dữ liệu bài!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bai.getTieuDePhu() == null || bai.getTieuDePhu().trim().isEmpty()) {
                        Toast.makeText(CungCoMonHoc.this, "Thiếu tiêu đề phụ!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bai.getMaTieuDePhu() == 0) {
                        Toast.makeText(CungCoMonHoc.this, "Thiếu mã tiêu đề phụ!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(CungCoMonHoc.this, CauHoiCungCo.class);
                    intent.putExtra("maBaiKiemTra", bai.getMaBaiKiemTra());
                    intent.putExtra("tieuDePhu", bai.getTieuDePhu());
                    intent.putExtra("maTieuDePhu", bai.getMaTieuDePhu());
                    intent.putExtra("maMonHoc", maMonHoc);

                    Log.d("CLICK_ITEM", "Chuyển tới: " + bai.getTieuDePhu()
                            + " (maTieuDePhu=" + bai.getMaTieuDePhu() + ")");
                    startActivityForResult(intent, 100);
                });



            }

            @Override
            public void onFailure(Call<List<BaiKiemTra>> call, Throwable t) {
                Toast.makeText(CungCoMonHoc.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Lỗi API: ", t);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            int maTieuDePhuHoanThanh = data.getIntExtra("maTieuDePhuHoanThanh", -1);
            if (maTieuDePhuHoanThanh != -1 && adapter != null) {
                adapter.danhDauHoanThanh(maTieuDePhuHoanThanh);
                Toast.makeText(this, "Bài đã được đánh dấu hoàn thành!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
