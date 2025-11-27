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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class ThongBao extends AppCompatActivity {

    // Constants
    private static final String PREF_NAME = "ALARM_PREF";
    private static final int NOTIFICATION_PERMISSION_CODE = 101;
    private static final String TIME_FORMAT = "%02d:%02d";

    // Views
    private SwitchMaterial switchMorning, switchNoon, switchEvening;
    private TextView txtMorningTime, txtNoonTime, txtEveningTime;

    // Data
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao);

        initializeViews();
        requestPermissions();
        loadSavedData();
        setupListeners();
    }

    // Khởi tạo các views
    private void initializeViews() {
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        switchMorning = findViewById(R.id.switchMorning);
        switchNoon = findViewById(R.id.switchNoon);
        switchEvening = findViewById(R.id.switchEvening);

        txtMorningTime = findViewById(R.id.txtMorningTime);
        txtNoonTime = findViewById(R.id.txtNoonTime);
        txtEveningTime = findViewById(R.id.txtEveningTime);
    }

    //Yêu cầu các quyền cần thiết
    private void requestPermissions() {
        requestExactAlarmPermission();
        requestNotificationPermission();
    }

    //Thiết lập các listeners
    private void setupListeners() {
        // TextView click listeners
        txtMorningTime.setOnClickListener(v ->
                showTimePickerDialog("morning", txtMorningTime));
        txtNoonTime.setOnClickListener(v ->
                showTimePickerDialog("noon", txtNoonTime));
        txtEveningTime.setOnClickListener(v ->
                showTimePickerDialog("evening", txtEveningTime));

        // Switch change listeners
        switchMorning.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("morning", checked, txtMorningTime.getText().toString(), "Báo thức sáng"));
        switchNoon.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("noon", checked, txtNoonTime.getText().toString(), "Báo thức trưa"));
        switchEvening.setOnCheckedChangeListener((b, checked) ->
                handleAlarm("evening", checked, txtEveningTime.getText().toString(), "Báo thức tối"));
    }

    //Yêu cầu quyền thông báo
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

    //Yêu cầu quyền đặt báo thức chính xác
    private void requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (am != null && !am.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Đã cấp quyền thông báo");
            } else {
                showToast("Cần cấp quyền thông báo để báo thức hoạt động");
            }
        }
    }

    //Load dữ liệu đã lưu
    private void loadSavedData() {
        // Load thời gian
        txtMorningTime.setText(pref.getString("time_morning", "06:00"));
        txtNoonTime.setText(pref.getString("time_noon", "12:00"));
        txtEveningTime.setText(pref.getString("time_evening", "20:00"));

        // Load trạng thái switch
        switchMorning.setChecked(pref.getBoolean("alarm_morning", false));
        switchNoon.setChecked(pref.getBoolean("alarm_noon", false));
        switchEvening.setChecked(pref.getBoolean("alarm_evening", false));

        // Kích hoạt lại báo thức nếu đã bật trước đó
        restoreActiveAlarms();
    }

    //Khôi phục các báo thức đã kích hoạt
    private void restoreActiveAlarms() {
        if (switchMorning.isChecked()) {
            handleAlarm("morning", true, txtMorningTime.getText().toString(), "Báo thức sáng");
        }
        if (switchNoon.isChecked()) {
            handleAlarm("noon", true, txtNoonTime.getText().toString(), "Báo thức trưa");
        }
        if (switchEvening.isChecked()) {
            handleAlarm("evening", true, txtEveningTime.getText().toString(), "Báo thức tối");
        }
    }

    //Lưu thời gian vào SharedPreferences
    private void saveTime(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    //Lưu trạng thái switch vào SharedPreferences
    private void saveSwitch(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }

    //Hiển thị dialog chọn giờ
    private void showTimePickerDialog(String type, TextView targetView) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_time_picker);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Cấu hình TimePicker
        timePicker.setIs24HourView(true);
        setTimePickerValue(timePicker, targetView.getText().toString());

        // Xử lý nút Cancel
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý nút OK
        btnOk.setOnClickListener(v -> {
            String time = getTimeFromPicker(timePicker);
            updateTimeAndAlarm(type, time, targetView);
            dialog.dismiss();
        });

        dialog.show();
    }

    //Set giá trị cho TimePicker từ chuỗi thời gian
    private void setTimePickerValue(TimePicker timePicker, String timeString) {
        String[] parts = timeString.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    //Lấy thời gian từ TimePicker
    private String getTimeFromPicker(TimePicker timePicker) {
        int hour, minute;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        return String.format(TIME_FORMAT, hour, minute);
    }

    //Cập nhật thời gian và báo thức
    private void updateTimeAndAlarm(String type, String time, TextView targetView) {
        targetView.setText(time);
        saveTime("time_" + type, time);

        // Cập nhật báo thức nếu switch đang bật
        boolean isEnabled = false;
        String title = "";

        switch (type) {
            case "morning":
                isEnabled = switchMorning.isChecked();
                title = "Báo thức sáng";
                break;
            case "noon":
                isEnabled = switchNoon.isChecked();
                title = "Báo thức trưa";
                break;
            case "evening":
                isEnabled = switchEvening.isChecked();
                title = "Báo thức tối";
                break;
        }

        if (isEnabled) {
            handleAlarm(type, true, time, title);
        }
    }

    //Xử lý đặt/hủy báo thức
    private void handleAlarm(String type, boolean enable, String timeText, String title) {
        saveSwitch("alarm_" + type, enable);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = createAlarmPendingIntent(type, timeText, title);

        if (enable) {
            setAlarm(alarmManager, timeText, pendingIntent, title);
        } else {
            cancelAlarm(alarmManager, pendingIntent, title);
        }
    }

    //Tạo PendingIntent cho báo thức
    private PendingIntent createAlarmPendingIntent(String type, String timeText, String title) {
        String[] parts = timeText.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", "Đã đến giờ rồi!");
        intent.putExtra("reqCode", type.hashCode());
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        return PendingIntent.getBroadcast(
                this,
                type.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    //Đặt báo thức
    private void setAlarm(AlarmManager alarmManager, String timeText,
                          PendingIntent pendingIntent, String title) {
        Calendar calendar = getAlarmCalendar(timeText);

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

        showToast("Đã đặt " + title + " lúc " + timeText);
    }

    //Hủy báo thức
    private void cancelAlarm(AlarmManager alarmManager, PendingIntent pendingIntent, String title) {
        alarmManager.cancel(pendingIntent);
        showToast("Đã hủy " + title);
    }

    //Tạo Calendar cho báo thức
    private Calendar getAlarmCalendar(String timeText) {
        String[] parts = timeText.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Nếu thời gian đã qua trong ngày, chuyển sang ngày mai
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return calendar;
    }

    //Hiển thị Toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}