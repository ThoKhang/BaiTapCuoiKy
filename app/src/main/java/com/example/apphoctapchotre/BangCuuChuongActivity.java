package com.example.apphoctapchotre;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BangCuuChuongActivity extends AppCompatActivity {

    private TextView tvBangCuuChuong, tvCircleNumber;
    private GridLayout gridNumbers;
    private Button selectedButton; // Lưu nút đang được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangcuuchuong);

        // Ánh xạ view
        tvBangCuuChuong = findViewById(R.id.tvBangCuuChuong);
        tvCircleNumber = findViewById(R.id.tvCircleNumber);
        gridNumbers = findViewById(R.id.gridNumbers);

        // Gán sự kiện cho các nút trong GridLayout
        for (int i = 0; i < gridNumbers.getChildCount(); i++) {
            View view = gridNumbers.getChildAt(i);
            if (view instanceof Button) {
                Button btn = (Button) view;
                btn.setOnClickListener(v -> onNumberClicked(btn));
            }
        }
    }

    // Khi nhấn vào 1 nút
    private void onNumberClicked(Button button) {
        // Lấy giá trị số từ text của nút
        String text = button.getText().toString();
        int number = Integer.parseInt(text);

        // Hiển thị số lên hình tròn đỏ
        tvCircleNumber.setText(String.valueOf(number));

        // Tạo nội dung bảng cửu chương
        StringBuilder bang = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            bang.append(number).append(" x ").append(i)
                    .append(" = ").append(number * i).append("\n");
        }
        tvBangCuuChuong.setText(bang.toString());

        // Reset màu cho nút cũ
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.bg_number_button);
        }

        // Đổi màu nút đang chọn
        button.setBackgroundResource(R.drawable.bg_number_button_selected);
        selectedButton = button;
    }
}
