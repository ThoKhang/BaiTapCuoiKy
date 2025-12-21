package com.example.apphoctapchotre;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.apphoctapchotre.UI.Activity.TroChoi.LienHoanTinhToan.NhapDapAnLienHoanTinhToan;
import com.example.apphoctapchotre.UI.Activity.TroChoi.Trochoi;

public class KetQuaLienHoanTinhToan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ket_qua_trum_tinh_nham);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int diem = getIntent().getIntExtra("DIEM", 0);
        int cauDung = getIntent().getIntExtra("CAU_DUNG", 0);
        int cauSai = getIntent().getIntExtra("CAU_SAI", 0);
        long thoiGian = getIntent().getLongExtra("THOI_GIAN", 0);

        long phut = thoiGian / 60;
        long giay = thoiGian % 60;

        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvCorrect = findViewById(R.id.tv_correct);
        TextView tvWrong = findViewById(R.id.tv_wrong);
        TextView tvTime = findViewById(R.id.tv_time);

        tvScore.setText(diem + "/100");
        tvCorrect.setText(cauDung + "");
        tvWrong.setText(cauSai + "");
        tvTime.setText(phut + " phút " + giay + " giây");

        ImageView ivBack = findViewById(R.id.quayLai);
        Button btnQuayLai = findViewById(R.id.btnQuayLai);
        LinearLayout btnChiaSe = findViewById(R.id.btnChiaSe);

        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(KetQuaLienHoanTinhToan.this,
                    Trochoi.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });
        btnQuayLai.setOnClickListener(v -> {
            Intent intent= new Intent(this, NhapDapAnLienHoanTinhToan.class);
            startActivity(intent);
        });

        btnChiaSe.setOnClickListener(v -> {
            String msg = "Tôi đã đạt " + diem + "/100 trong Trùm tính nhẩm!\n"
                    + "✔ Đúng: " + cauDung + "\n"
                    + "✘ Sai: " + cauSai + "\n"
                    + "⏱ Thời gian: " + phut + " phút " + giay + " giây\n"
                    + "Bạn làm thử xem!";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

            startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
        });
    }
}
