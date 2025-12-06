package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.OnLuyenViewModel;
import com.example.apphoctapchotre.UI.ViewModel.OnLuyenViewModelFactory;

public class OnLuyen extends AppCompatActivity {
    private Button btnDeCoBan,btndeTrungBinh,btnDeNangCao,btnSoDeHoanThanhCB,btnSoDeHoanThanhTB,btnSoDeHoanThanhNC;
    private TextView back;
    private OnLuyenViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_on_luyen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnDeCoBan=findViewById(R.id.btnDeCoBan);
        btndeTrungBinh=findViewById(R.id.btnDeTrungBinh);
        btnDeNangCao=findViewById(R.id.btnDeNangCao);
        btnSoDeHoanThanhCB=findViewById(R.id.btnSoDeCB);
        btnSoDeHoanThanhTB=findViewById(R.id.btnSoDeTb);
        btnSoDeHoanThanhNC=findViewById(R.id.btnSoDeNc);
        btnDeCoBan.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeCoBan.class);
            startActivity(intent);
        });
        btndeTrungBinh.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeTrungBinh.class);
            startActivity(intent);
        });
        btnDeNangCao.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeNangCao.class);
            startActivity(intent);
        });
        back =findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, GiaoDienTong.class);
            startActivity(intent);
        });
        // ViewModel + Factory
        viewModel = new ViewModelProvider(
                this,
                new OnLuyenViewModelFactory(this)
        ).get(OnLuyenViewModel.class);

        // Lấy mã người dùng đã lưu khi login
        String maNguoiDung = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                .getString("MA_NGUOI_DUNG", null);

        // Gọi API
        viewModel.loadTongQuan(maNguoiDung);

        // Nhận dữ liệu từ API & cập nhật UI
//        viewModel.getTongQuanLiveData().observe(this, data -> {
//            if (data != null) {
//                btnSoDeHoanThanhCB.setText("Hoàn thành: " +
//                        data.coBan.tongDeDaLam + "/" + data.coBan.tongDe);
//
//                btnSoDeHoanThanhTB.setText("Hoàn thành: " +
//                        data.trungBinh.tongDeDaLam + "/" + data.trungBinh.tongDe);
//
//                btnSoDeHoanThanhNC.setText("Hoàn thành: " +
//                        data.nangCao.tongDeDaLam + "/" + data.nangCao.tongDe);
//            }
//        });

        // Nhận lỗi
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}