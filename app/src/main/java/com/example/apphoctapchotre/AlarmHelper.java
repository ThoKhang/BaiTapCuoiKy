package com.example.apphoctapchotre;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmHelper {
    @SuppressLint("ScheduleExactAlarm")
    public static void setExactAlarm(Context context,
                                     int hour, int minute,
                                     String title, String desc,
                                     int reqCode) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        intent.putExtra("reqCode", reqCode);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);

        PendingIntent pi = PendingIntent.getBroadcast(
                context,
                reqCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        am.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                pi
        );
    }

    public static void cancel(Context context, int req) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, req, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }
}
