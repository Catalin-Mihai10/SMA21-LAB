package SMA.Laborator4;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("Load Image");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            final String channelID = "my channel id";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                NotificationChannel notificationChannel = new NotificationChannel(channelID, "my channel name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setShowBadge(false);
                notificationChannel.setSound(null, null);
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            }

            Notification notification = new NotificationCompat.Builder(this, channelID)
                    .setContentTitle("Image download")
                    .setTicker("Ticker")
                    .setContentText("Please wait a few seconds ...")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(Integer.parseInt(FOREGROUND_SERVICE), notification);
    }
}
