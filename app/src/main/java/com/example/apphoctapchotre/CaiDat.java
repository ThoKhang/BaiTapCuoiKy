package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class CaiDat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        ImageButton btnBack = findViewById(R.id.btnBack);
        CardView itemGioiThieu = findViewById(R.id.itemGioiThieu);
        CardView itemThongBao = findViewById(R.id.itemThongBao);
        CardView itemNgonNgu = findViewById(R.id.itemNgonNgu);
        CardView itemChiaSe = findViewById(R.id.itemChiaSe);


        btnBack.setOnClickListener(v -> finish());

        itemGioiThieu.setOnClickListener(v ->
                startActivity(new Intent(this, GioiThieuKHViewPager.class))
        );

        itemThongBao.setOnClickListener(v ->
                startActivity(new Intent(this, ThongBao.class))
        );

        itemNgonNgu.setOnClickListener(v ->
                startActivity(new Intent(this, NgonNgu.class))
        );

        itemChiaSe.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "Hãy thử ứng dụng này!");
            startActivity(Intent.createChooser(share, "Chia sẻ qua"));
        });
    }
}
