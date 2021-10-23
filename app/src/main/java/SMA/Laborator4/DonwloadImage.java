package SMA.Laborator4;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DonwloadImage extends AsyncTask<String, Void, Bitmap> {
    private Context context;


    public DonwloadImage(Context context) {
        this.context = context;
        Toast.makeText(context, "Please wait, it may take a few seconds.", Toast.LENGTH_SHORT).show();
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap bmp = null;
        try {
            String longURL = MyIntentService.getLongUrl(urls[0]);
            try {
                InputStream in = new URL(longURL).openStream();
                bmp = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }

            // simulate longer job ...
            Thread.sleep(5000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        // save bitmap result in application class
        ((MyCustomApplication) context.getApplicationContext()).setBitmap(result);
        // send intent to stop foreground service
        Intent stopIntent = new Intent(context.getApplicationContext(), ForegroundImageService.class);
        stopIntent.setAction(ForegroundImageService.STOPFOREGROUND_ACTION);
        context.startService(stopIntent);

    }
}
