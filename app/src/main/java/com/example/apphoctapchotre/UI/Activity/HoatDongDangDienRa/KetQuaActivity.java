package com.example.apphoctapchotre.UI.Activity.HoatDongDangDienRa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.GiaoDienTong.GiaoDienTong;

public class KetQuaActivity extends AppCompatActivity {

    TextView tvScore, tvCorrect, tvWrong, tvTime, tvMessage;
    LinearLayout btnChiaSe;
    TextView btnQuayLai;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua_thu_thach);

        // Mở trang Premium khi bấm vào icon vương miện
        findViewById(R.id.imgVuongMieng).setOnClickListener(v -> {
            Intent intent = new Intent(KetQuaActivity.this,
                    com.example.apphoctapchotre.UI.Activity.Premium.Premium.class);
            startActivity(intent);
        });
        // Ánh xạ view
        tvScore = findViewById(R.id.tv_score);
        tvCorrect = findViewById(R.id.tv_correct);
        tvWrong = findViewById(R.id.tv_wrong);
        tvTime = findViewById(R.id.tv_time);
        tvMessage = findViewById(R.id.tv_result_message);

        btnChiaSe = findViewById(R.id.btnChiaSe);
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnBack = findViewById(R.id.quayLai);

        // ================================
        // NHẬN DỮ LIỆU TỪ CauHoi100
        // ================================
        int totalScore = getIntent().getIntExtra("TOTAL_SCORE", 0);
        int dung = getIntent().getIntExtra("SO_CAU_DUNG", 0);
        int sai = getIntent().getIntExtra("SO_CAU_SAI", 0);
        int tongCau = getIntent().getIntExtra("TONG_CAU", 0);
        long duration = getIntent().getLongExtra("THOI_GIAN", 0);

        // Thời gian
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        tvTime.setText(minutes + " phút " + seconds + " giây");

        // Tính tổng điểm
        int tongDiem = tongCau * 5;

        // Hiển thị điểm
        tvScore.setText(totalScore + "/" + tongDiem);

        // Hiển thị đúng/sai
        tvCorrect.setText(dung + "/" + tongCau);
        tvWrong.setText(sai + "/" + tongCau);

        // Hiển thị thông điệp
        if (totalScore >= tongDiem * 0.8) {
            tvMessage.setText("Xuất sắc!");
        } else if (totalScore >= tongDiem * 0.5) {
            tvMessage.setText("Rất tốt!");
        } else {
            tvMessage.setText("Cố gắng hơn nhé!");
        }

        // ================================
        // BUTTON SHARE
        // ================================
        btnChiaSe.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");

            String msg =
                    "Mình vừa hoàn thành thử thách và đạt được " + totalScore + "/" + tongDiem + " điểm!\n"
                            + "Bạn thử làm xem được bao nhiêu điểm nhé!";

            share.putExtra(Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(share, "Chia sẻ qua"));
        });

        // ================================
        // BACK ICON → đóng màn hình
        // ================================
        btnBack.setOnClickListener(v -> finish());

        // ================================
        // BUTTON QUAY LẠI TRANG CHỦ
        // ================================
        btnQuayLai.setOnClickListener(v -> {
            Intent intent = new Intent(KetQuaActivity.this, GiaoDienTong.class);
            intent.putExtra("OPEN_HOME", true);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }
}
