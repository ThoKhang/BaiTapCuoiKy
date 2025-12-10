package com.example.apphoctapchotre.Utility;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @SuppressLint("ScheduleExactAlarm")
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "AlarmReceiver triggered!");

        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        int req = intent.getIntExtra("reqCode", 0);
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);

        Log.d(TAG, "Alarm: " + title + " at " + hour + ":" + minute);

        // Gửi thông báo
        NotificationHelper.showNotification(context, title, desc);

        // Tạo thời gian lặp lại ngày hôm sau
        Calendar next = Calendar.getInstance();
        next.set(Calendar.HOUR_OF_DAY, hour);
        next.set(Calendar.MINUTE, minute);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        next.add(Calendar.DAY_OF_MONTH, 1); // Ngày mai

        // Tạo Intent mới cho lần báo thức tiếp theo
        Intent newIntent = new Intent(context, AlarmReceiver.class);
        newIntent.putExtra("title", title);
        newIntent.putExtra("desc", desc);
        newIntent.putExtra("reqCode", req);
        newIntent.putExtra("hour", hour);
        newIntent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                req,
                newIntent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            // Đặt báo thức lặp lại cho ngày mai
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        next.getTimeInMillis(),
                        pendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        next.getTimeInMillis(),
                        pendingIntent
                );
            }

            Log.d(TAG, "Next alarm scheduled for: " + next.getTime());
        }
    }
}