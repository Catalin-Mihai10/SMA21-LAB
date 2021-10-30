package com.monitorbatterylife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    private TextView textView;
    public static int notificationId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        ChangingStatus battery = new ChangingStatus();
        battery.onReceive(this, batteryStatus);

        boolean chargingStatus = battery.isCharging(this);
        textView = findViewById(R.id.textView);
        textView.setText(String.valueOf(chargingStatus));

        // Create an explicit intent for an Activity in your app
        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
        //newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        newIntent.putExtra("status", chargingStatus);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, newIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), MainActivity.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Charging status changed!")
                .setContentText(String.valueOf(chargingStatus))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(MainActivity.notificationId, mBuilder.build());
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="Catalin";
            String description = "channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}