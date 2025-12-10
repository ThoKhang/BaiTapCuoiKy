package com.example.apphoctapchotre.Utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.apphoctapchotre.R;
import com.example.apphoctapchotre.UI.Activity.Settings.ThongBao;

public class NotificationHelper {

    public static final String CHANNEL_ID = "alarm_channel";

    public static void showNotification(Context context, String title, String desc) {

        NotificationManager nm =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Notification Channel cho Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Báo thức",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            // Cấu hình channel
            channel.setDescription("Kênh thông báo cho báo thức");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableLights(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            // Âm thanh báo thức
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (soundUri == null) {
                soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            channel.setSound(soundUri, audioAttributes);

            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }

        // Intent mở app khi nhấn vào thông báo
        Intent intent = new Intent(context, ThongBao.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Âm thanh cho Android < 8
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // Tạo notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)                     // Icon nhỏ của thông báo (bắt buộc)
                .setContentTitle(title)                                // Tiêu đề thông báo
                .setContentText(desc)                                  // Nội dung thông báo
                .setContentIntent(contentIntent)                       // Khi bấm vào thông báo → mở Activity
                .setAutoCancel(true)                                   // Thông báo tự biến mất khi bấm vào
                .setPriority(NotificationCompat.PRIORITY_HIGH)         // Ưu tiên cao (hiện dạng popup heads-up)
                .setCategory(NotificationCompat.CATEGORY_ALARM)        // Xếp loại báo thức -> quyền ưu tiên cao nhất
                .setSound(alarmSound)                                  // Âm thanh báo thức
                .setVibrate(new long[]{0, 1000, 500, 1000})            // Rung: bắt đầu ngay -> rung 1s -> nghỉ 0.5s -> rung 1s
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)   // Hiển thị đầy đủ trên màn hình khóa
                .setFullScreenIntent(contentIntent, true)    //Mở toàn màn hình khi khóa máy (giống báo thức thật)
                .setOngoing(false);                                    // Không cố định, có thể vuốt để xóa


        // Hiển thị notification
        if (nm != null) {
            nm.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}