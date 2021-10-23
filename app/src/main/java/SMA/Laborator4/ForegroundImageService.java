package SMA.Laborator4;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ForegroundImageService extends IntentService {
    public static final String STARTFOREGROUND_ACTION = "myaction.startforeground";
    public static final String STOPFOREGROUND_ACTION = "myaction.stopforeground";
    public static final int FOREGROUND_SERVICE_ID = 1234;

    public ForegroundImageService() {
       super("Load Image in Foreground");
        System.out.println("in constructor foreground");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            System.out.println("in MyIntentService");
            onStartCommand(intent, 0,FOREGROUND_SERVICE_ID );
        }
        System.out.println("vede intent null");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("in onStartCommand");
        if (intent.getAction().equals(STARTFOREGROUND_ACTION)) {
            Intent notificationIntent = new Intent(this, ImageActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            final String channelID = "my channel id";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelID, "my channel name", NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(false);
                channel.setSound(null, null);
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            }

            Notification notification = new NotificationCompat.Builder(this, channelID)
                    .setContentTitle("Image download")
                    .setTicker("Ticker")
                    .setContentText("Please wait a few seconds ...")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(FOREGROUND_SERVICE_ID, notification);


            final String param = intent.getStringExtra("EXTRA_URL");
            System.out.println("download");
            new DonwloadImage(this).execute(param);

        } else if (intent.getAction().equals(STOPFOREGROUND_ACTION)) {
            stopForeground(true);
            stopSelf();

            // start second activity to show result
            Intent anotherIntent = new Intent(getApplicationContext(), ImageActivity.class);
            anotherIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(anotherIntent);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
