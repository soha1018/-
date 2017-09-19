package com.example.traffic8_29.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import com.example.traffic8_29.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/29.
 */

public class Utils {
    public static String LongToString(long values) {
        Date date = new Date(values);
        SimpleDateFormat dateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static void showNotification(Context context, String title, String message, PendingIntent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.car);
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.getNotification();
        nm.notify(1024,notification);
    }
}