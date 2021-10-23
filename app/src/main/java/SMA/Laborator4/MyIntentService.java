package SMA.Laborator4;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("Load Image");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String param = intent.getStringExtra("EXTRA_URL");
            System.out.println("in MyIntentService");
            handleDownloadAction(param);
        }
    }
    /**
     * Handle action in the provided background thread with the provided parameters.
     */
    private void handleDownloadAction(String url) {
        // start task on separate thread
        //new DownloadImageTask().execute(url);
        //.execute("https://news.nationalgeographic.com/content/dam/news/2018/05/17/you-can-train-your-cat/02-cat-training-NationalGeographic_1484324.ngsversion.1526587209178.adapt.1900.1.jpg");
        System.out.println("In handler");
        try {
            String longURL = getLongUrl(url);
            Bitmap bmp = null;
            try {
                InputStream in = new URL(longURL).openStream();
                bmp = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }

            // simulate longer job ...
            Thread.sleep(5000);

            ((MyCustomApplication) getApplicationContext()).setBitmap(bmp);
            // start second activity to show result
            Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
            System.out.println("Incearca sa deschida ImageActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getLongUrl(String shortUrl) throws MalformedURLException, IOException {
        String result = shortUrl;
        String header;
        do {
            URL url = new URL(result);
            HttpURLConnection.setFollowRedirects(false);
            URLConnection conn = url.openConnection();
            header = conn.getHeaderField(null);
            String location = conn.getHeaderField("location");
            if (location != null) {
                result = location;
            }
        } while (header.contains("301"));

        // also decode URL
        result = URLDecoder.decode(result, "UTF-8");
        // trim to extract bitmap
        result = result.substring(result.indexOf("imgurl=") + "imgurl=".length());
        result = result.substring(0, result.indexOf("&"));

        return result;
    }
}
