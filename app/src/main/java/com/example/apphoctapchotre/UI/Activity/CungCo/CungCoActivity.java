package com.example.apphoctapchotre.UI.Activity.CungCo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.CungCoViewModel;

public class CungCoActivity extends AppCompatActivity {

    private TextView tvTenMonHocTV, txtTienDoTV;
    private TextView txtTenMonHocT, txtTienDoT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cung_co_mon_hoc1);

        // Ánh xạ view
        tvTenMonHocTV = findViewById(R.id.tvTenMonHocTV);
        txtTienDoTV = findViewById(R.id.txtTienDoTV);

        txtTenMonHocT = findViewById(R.id.txtTenMonHocT);
        txtTienDoT = findViewById(R.id.txtTienDoT);

        // Tạo ViewModel
        CungCoViewModel viewModel = new ViewModelProvider(this).get(CungCoViewModel.class);

        // GỌI API TẠI ĐÂY
        viewModel.loadTienDo("ND004");  // <-- sau này bạn truyền mã người dùng thật

        // Lấy dữ liệu và cập nhật UI
        viewModel.getTienDo().observe(this, list -> {
            if (list != null) {
                for (var item : list) {

                    if (item.getMaMonHoc().equals("MH002")) { // Tiếng Việt
                        tvTenMonHocTV.setText(item.getTenMonHoc());
                        txtTienDoTV.setText("Tổng số bài đã học: " +
                                item.getSoDaHoc() + "/" + item.getTongSoBai());
                    }

                    if (item.getMaMonHoc().equals("MH001")) { // Toán
                        txtTenMonHocT.setText(item.getTenMonHoc());
                        txtTienDoT.setText("Tổng số bài đã học: " +
                                item.getSoDaHoc() + "/" + item.getTongSoBai());
                    }
                }
            }
        });
    }
}
