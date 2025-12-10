package com.example.apphoctapchotre.DATA.Repository;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Calendar;

import com.example.apphoctapchotre.Utility.AlarmReceiver;

public class BaoThucRepository {
    private static final String PREF_NAME = "ALARM_PREF";
    private final Context appContext;
    private final SharedPreferences pref;

    public BaoThucRepository(Context context) {
        this.appContext = context.getApplicationContext();
        this.pref = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getTime(String key, String defaultVal) {
        return pref.getString(key, defaultVal);
    }

    public boolean getAlarmEnabled(String key, boolean def) {
        return pref.getBoolean(key, def);
    }

    public void saveTime(String key, String value) {
        pref.edit().putString(key, value).apply();
    }

    public void saveSwitch(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }

    // Đặt alarm
    public void setAlarm(String type, String timeText, String title) {
        String[] parts = timeText.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(appContext, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", "Đã đến giờ rồi!");
        int req = type.hashCode();
        intent.putExtra("reqCode", req);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                appContext,
                req,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        if (alarmManager != null) {
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
        }
    }

    // Hủy alarm
    public void cancelAlarm(String type) {
        int req = type.hashCode();
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(appContext, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                appContext,
                req,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (alarmManager != null) {
            alarmManager.cancel(pi);
        }
    }
}
