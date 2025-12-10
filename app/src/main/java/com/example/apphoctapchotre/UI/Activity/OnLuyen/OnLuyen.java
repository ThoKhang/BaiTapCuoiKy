package com.example.apphoctapchotre.UI.Activity.OnLuyen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;
import com.example.apphoctapchotre.R;

import com.example.apphoctapchotre.UI.ViewModel.OnLuyenViewModel;

public class OnLuyen extends AppCompatActivity {
    private Button btnDeCoBan,btndeTrungBinh,btnDeNangCao,btnSoCauDeCoBan,btnSoCauDeTrungBinh,btnSoCauDeNangCao;
    private TextView back;
    private OnLuyenViewModel viewModel;
    private com.example.apphoctapchotre.DATA.model.OnLuyen onLuyenPutextra;
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
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email=prefs.getString("userEmail",null);
        viewModel=new ViewModelProvider(this).get(OnLuyenViewModel.class);
        if(email!=null)
            viewModel.loadOnLuyen(email);

        btnDeCoBan=findViewById(R.id.btnDeCoBan);
        btndeTrungBinh=findViewById(R.id.btnDeTrungBinh);
        btnDeNangCao=findViewById(R.id.btnDeNangCao);
        btnSoCauDeCoBan=findViewById(R.id.btnSoCauDeCB);
        btnSoCauDeTrungBinh=findViewById(R.id.btnSoCauDTB);
        btnSoCauDeNangCao=findViewById(R.id.btnSoCauDeNC);
        btnDeCoBan.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeCoBan.class);
            intent.putExtra("TONG_DE_CO_BAN", onLuyenPutextra.getTongSoDeCoBan());
            startActivity(intent);
        });
        btndeTrungBinh.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeTrungBinh.class);
            intent.putExtra("TONG_DE_TRUNG_BINH", onLuyenPutextra.getTongSoDeTrungBinh());
            startActivity(intent);
        });
        btnDeNangCao.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeNangCao.class);
            intent.putExtra("TONG_DE_NANG_CAO", onLuyenPutextra.getTongSoDeNangCao());
            startActivity(intent);
        });
        back =findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, GiaoDienTong.class);
            startActivity(intent);
        });
        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(OnLuyen.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
        viewModel.onLuyen.observe(this,onLuyen -> {
            if (onLuyen != null) {
                onLuyenPutextra=onLuyen;
                btnSoCauDeCoBan.setText("Tổng số đề đã làm: " + onLuyen.getSoDeCoBanDaLam()+"/"+onLuyen.getTongSoDeCoBan());
                btnSoCauDeTrungBinh.setText("Tổng số đề đã làm: " + onLuyen.getSoDeTrungBinhDaLam()+"/"+onLuyen.getTongSoDeTrungBinh());
                btnSoCauDeNangCao.setText("Tổng số đề đã làm: " + onLuyen.getSoDeNangCaoDaLam()+"/"+onLuyen.getTongSoDeNangCao());
            }
        });
    }
}