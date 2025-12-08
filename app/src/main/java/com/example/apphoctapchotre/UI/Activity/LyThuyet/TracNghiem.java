package com.example.apphoctapchotre.UI.Activity.LyThuyet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView cho nút quay lại
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.DATA.model.CauHoi;
import com.example.apphoctapchotre.DATA.model.CauHoiResponse;
import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.DeOnLuyenViewModel;

import java.util.List;

public class TracNghiem extends AppCompatActivity {
    private TextView tvCauHoi, tvDapAnA, tvDapAnB, tvDapAnC, tvDapAnD, tvGiaiThich;
    private RadioButton btnA, btnB, btnC, btnD;
    private Button btnTiepTuc;
    private ImageView backButton;
    private String tenDe;
    private DeOnLuyenViewModel deOnLuyenViewModel;
    private List<CauHoi> danhSachCauHoi;
    int currentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        tvCauHoi = findViewById(R.id.tvCauHoi);
        tvDapAnA = findViewById(R.id.tvDapAnA);
        tvDapAnB = findViewById(R.id.tvDapAnB);
        tvDapAnC = findViewById(R.id.tvDapAnC);
        tvDapAnD = findViewById(R.id.tvDapAnD);
        tvGiaiThich = findViewById(R.id.tvGiaiThich);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        backButton = findViewById(R.id.quayLai);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }
        // lấy tên đề và sửa lại để gọi api
        tenDe=getIntent().getStringExtra("TEN_DE");
        int soDe=Integer.parseInt(tenDe.replaceAll("\\D+",""));
        String tieuDe="Ôn cơ bản "+soDe;
        deOnLuyenViewModel = new ViewModelProvider(this).get(DeOnLuyenViewModel.class);
        deOnLuyenViewModel.loadDeOnLuyen(tieuDe);
        deOnLuyenViewModel.deOnLuyenMutableLiveData.observe(this,de ->{
            if(de==null) {
                Toast.makeText(this, "Không load được dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }
            danhSachCauHoi=de.getDanhSachCauHoi();
            currentIndex = 0;
            loadCauHoi();
            btnA.setOnClickListener(v -> checkAnswer(btnA, 0));
            btnB.setOnClickListener(v -> checkAnswer(btnB, 1));
            btnC.setOnClickListener(v -> checkAnswer(btnC, 2));
            btnD.setOnClickListener(v -> checkAnswer(btnD, 3));

        });
        btnTiepTuc.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < danhSachCauHoi.size()) {
                resetButtons();
                loadCauHoi();
            } else {
                Toast.makeText(this, "Bạn đã hoàn thành bài!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void loadCauHoi(){
        CauHoi cauHoi = danhSachCauHoi.get(currentIndex);
        tvCauHoi.setText(cauHoi.getNoiDungCauHoi());
        tvGiaiThich.setText(cauHoi.getGiaiThich());
        tvDapAnA.setText("A."+cauHoi.getDapAn().get(0).getNoiDungDapAn());
        tvDapAnB.setText("B."+cauHoi.getDapAn().get(1).getNoiDungDapAn());
        tvDapAnC.setText("C."+cauHoi.getDapAn().get(2).getNoiDungDapAn());
        tvDapAnD.setText("D."+cauHoi.getDapAn().get(3).getNoiDungDapAn());

        tvGiaiThich.setVisibility(View.INVISIBLE);

//
//        setAnswer(btnA, cauHoi.getDapAn().get(0).isLaDapAnDung());
//        setAnswer(btnB, cauHoi.getDapAn().get(1).isLaDapAnDung());
//        setAnswer(btnC, cauHoi.getDapAn().get(2).isLaDapAnDung());
//        setAnswer(btnD, cauHoi.getDapAn().get(3).isLaDapAnDung());
    }


    private void setAnswer(RadioButton rd,boolean check){
        if(check)
            rd.setBackgroundColor(Color.parseColor("#4CAF50"));

        else
            rd.setBackgroundColor(Color.parseColor("#FF4444"));
    }
    private void resetButtons() {
        btnA.setBackgroundResource(R.drawable.option_button_bg);
        btnB.setBackgroundResource(R.drawable.option_button_bg);
        btnC.setBackgroundResource(R.drawable.option_button_bg);
        btnD.setBackgroundResource(R.drawable.option_button_bg);

        btnA.setChecked(false);
        btnB.setChecked(false);
        btnC.setChecked(false);
        btnD.setChecked(false);
    }
    private void checkAnswer(RadioButton btn, int index) {
        CauHoi cauHoi = danhSachCauHoi.get(currentIndex);

        boolean isCorrect = cauHoi.getDapAn().get(index).isLaDapAnDung();

        if (isCorrect)
            btn.setBackgroundColor(Color.parseColor("#4CAF50"));
        else
            btn.setBackgroundColor(Color.parseColor("#FF4444"));

        tvGiaiThich.setVisibility(View.VISIBLE);
    }


}