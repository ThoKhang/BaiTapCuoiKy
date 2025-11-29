package com.example.apphoctapchotre;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class ThongBao extends AppCompatActivity {

    private SwitchMaterial ctSang, ctTrua, ctToi;
    private TextView txtGioSang, txtGioTrua, txtGioToi;

    private SharedPreferences pref;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao);

        requestExactAlarmPermission();
        requestNotificationPermission();

        pref = getSharedPreferences("ALARM_PREF", MODE_PRIVATE);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        ctSang = findViewById(R.id.ctSang);
        ctTrua = findViewById(R.id.ctTrua);
        ctToi = findViewById(R.id.ctToi);

        txtGioSang = findViewById(R.id.txtGioSang);
        txtGioTrua = findViewById(R.id.txtGioTrua);
        txtGioToi = findViewById(R.id.txtGioToi);

        loadSavedData();

        // mở dialog layout
        txtGioSang.setOnClickListener(v -> showTimePickerDialog("morning", txtGioSang));
        txtGioTrua.setOnClickListener(v -> showTimePickerDialog("noon", txtGioTrua));
        txtGioToi.setOnClickListener(v -> showTimePickerDialog("evening", txtGioToi));

        ctSang.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("morning", checked, txtGioSang.getText().toString(), "Báo thức sáng"));

        ctTrua.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("noon", checked, txtGioTrua.getText().toString(), "Báo thức trưa"));

        ctToi.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("evening", checked, txtGioToi.getText().toString(), "Báo thức tối"));
    }

    // Yêu cầu cấp quyền
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (am != null && !am.canScheduleExactAlarms()) {
                Intent i = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    //load giờ đã cài trước đó
    private void loadSavedData() {
        txtGioSang.setText(pref.getString("time_morning", "06:00"));
        txtGioTrua.setText(pref.getString("time_noon", "12:00"));
        txtGioToi.setText(pref.getString("time_evening", "20:00"));

        ctSang.setChecked(pref.getBoolean("alarm_morning", false));
        ctTrua.setChecked(pref.getBoolean("alarm_noon", false));
        ctToi.setChecked(pref.getBoolean("alarm_evening", false));

        if (ctSang.isChecked()) {
            handleAlarm("morning", true, txtGioSang.getText().toString(), "Báo thức sáng");
        }
        if (ctTrua.isChecked()) {
            handleAlarm("noon", true, txtGioTrua.getText().toString(), "Báo thức trưa");
        }
        if (ctToi.isChecked()) {
            handleAlarm("evening", true, txtGioToi.getText().toString(), "Báo thức tối");
        }
    }

    private void saveTime(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    private void saveSwitch(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }

    // hàm gọi dialog
    private void showTimePickerDialog(String type, TextView targetView) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_picker);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        android.widget.TimePicker tp = dialog.findViewById(R.id.DongHo);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnCancel = dialog.findViewById(R.id.btnHuy);

        tp.setIs24HourView(true);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnOk.setOnClickListener(v -> {
            int hour = tp.getHour();
            int minute = tp.getMinute();

            String time = String.format("%02d:%02d", hour, minute);

            targetView.setText(time);
            saveTime("time_" + type, time);

            switch (type) {
                case "morning":
                    if (ctSang.isChecked())
                        handleAlarm("morning", true, time, "Báo thức sáng");
                    break;

                case "noon":
                    if (ctTrua.isChecked())
                        handleAlarm("noon", true, time, "Báo thức trưa");
                    break;

                case "evening":
                    if (ctToi.isChecked())
                        handleAlarm("evening", true, time, "Báo thức tối");
                    break;
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    // Cài/ hủy báo thức
    private void handleAlarm(String type, boolean enable, String timeText, String title) {

        saveSwitch("alarm_" + type, enable);

        String[] split = timeText.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", "Đã đến giờ rồi!");
        intent.putExtra("reqCode", type.hashCode());
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                type.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (enable) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
            }

            Toast.makeText(this, "Đã đặt " + title + " lúc " + timeText, Toast.LENGTH_SHORT).show();
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Đã hủy " + title, Toast.LENGTH_SHORT).show();
        }
    }
}
