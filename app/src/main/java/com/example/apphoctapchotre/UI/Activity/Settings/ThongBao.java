package com.example.apphoctapchotre.UI.Activity.Settings;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.ViewModel.ThongBaoViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class ThongBao extends AppCompatActivity {

    private SwitchMaterial ctSang, ctTrua, ctToi;
    private TextView txtGioSang, txtGioTrua, txtGioToi;

    private ThongBaoViewModel viewModel;

    // Nếu cần mở setting để cấp quyền exact alarm (Android S+)
    private final ActivityResultLauncher<Intent> exactAlarmLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (am != null && am.canScheduleExactAlarms()) {
                        Toast.makeText(this, "Exact alarm được cho phép", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        ctSang = findViewById(R.id.ctSang);
        ctTrua = findViewById(R.id.ctTrua);
        ctToi = findViewById(R.id.ctToi);

        txtGioSang = findViewById(R.id.txtGioSang);
        txtGioTrua = findViewById(R.id.txtGioTrua);
        txtGioToi = findViewById(R.id.txtGioToi);

        viewModel = new ViewModelProvider(this).get(ThongBaoViewModel.class);

        viewModel.getTimeMorning().observe(this, s -> txtGioSang.setText(s));
        viewModel.getTimeNoon().observe(this, s -> txtGioTrua.setText(s));
        viewModel.getTimeEvening().observe(this, s -> txtGioToi.setText(s));

        viewModel.getAlarmMorning().observe(this, checked -> {
            if (ctSang.isChecked() != checked) ctSang.setChecked(checked);
        });
        viewModel.getAlarmNoon().observe(this, checked -> {
            if (ctTrua.isChecked() != checked) ctTrua.setChecked(checked);
        });
        viewModel.getAlarmEvening().observe(this, checked -> {
            if (ctToi.isChecked() != checked) ctToi.setChecked(checked);
        });

        txtGioSang.setOnClickListener(v -> showTimePickerDialog("morning", txtGioSang));
        txtGioTrua.setOnClickListener(v -> showTimePickerDialog("noon", txtGioTrua));
        txtGioToi.setOnClickListener(v -> showTimePickerDialog("evening", txtGioToi));

        ctSang.setOnCheckedChangeListener((b, checked) -> {
            viewModel.toggleAlarm("morning", checked);
            Toast.makeText(this, checked ? "Bật báo thức sáng" : "Tắt báo thức sáng", Toast.LENGTH_SHORT).show();
        });
        ctTrua.setOnCheckedChangeListener((b, checked) -> {
            viewModel.toggleAlarm("noon", checked);
            Toast.makeText(this, checked ? "Bật báo thức trưa" : "Tắt báo thức trưa", Toast.LENGTH_SHORT).show();
        });
        ctToi.setOnCheckedChangeListener((b, checked) -> {
            viewModel.toggleAlarm("evening", checked);
            Toast.makeText(this, checked ? "Bật báo thức tối" : "Tắt báo thức tối", Toast.LENGTH_SHORT).show();
        });
        requestExactAlarmIfNeeded();
    }

    private void requestExactAlarmIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (am != null && !am.canScheduleExactAlarms()) {
                Intent i = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                exactAlarmLauncher.launch(i);
            }
        }
    }

    //Hiển thị dialog TimePicker tuỳ chỉnh
    private void showTimePickerDialog(String type, TextView targetView) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_picker);
        if (dialog.getWindow() != null) dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        android.widget.TimePicker tp = dialog.findViewById(R.id.DongHo);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnCancel = dialog.findViewById(R.id.btnHuy);

        tp.setIs24HourView(true);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnOk.setOnClickListener(v -> {
            int hour = tp.getHour();
            int minute = tp.getMinute();
            String time = String.format("%02d:%02d", hour, minute);

            viewModel.updateTime(type, time);

            dialog.dismiss();
        });

        dialog.show();
    }
}
