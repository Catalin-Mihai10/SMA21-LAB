package SMA.Laborator4;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        MyCustomApplication myCustomApplication = (MyCustomApplication) getApplicationContext();
        if(myCustomApplication.getBitmap() == null){
            Toast.makeText(this, "Error transmitting URL.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(myCustomApplication.getBitmap());
        }
    }


}
