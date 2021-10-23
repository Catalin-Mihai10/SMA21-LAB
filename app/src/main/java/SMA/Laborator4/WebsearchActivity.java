package SMA.Laborator4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class WebsearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loadBack = (Button) findViewById(R.id.LoadBack);
        Button loadFor = (Button) findViewById(R.id.LoadFor);
        WebView wv = (WebView) findViewById(R.id.wv);

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wv.setWebViewClient(new MyWebViewClient());
        wv.loadUrl("https://www.google.com/search?q=cat&tbm=isch&source=lnms&sa=X");


    }

    protected void foregroundLoadImage(){
        Intent notificationIntent = new Intent(this, ImageActivity.class);
        MyIntentService foregroundService = new MyIntentService();
        foregroundService.onHandleIntent(notificationIntent);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).toString().contains("tbm=isch")) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }


    public void loadImage(View view){

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData abc = clipboardManager.getPrimaryClip();
        ClipData.Item item = abc.getItemAt(0);
        String url = item.getText().toString();

        if(!url.contains("https://images.app.goo.gl")) {
            Toast.makeText(this, "Url not valid. Try another", Toast.LENGTH_SHORT).show();
            Log.d("Url:", url);
        }else{
            if(view.getId() == R.id.LoadBack){
                Intent intent = new Intent(this, MyIntentService.class);
                intent.putExtra("EXTRA_URL", url);
                System.out.println("macar aici");
                startService(intent);
            }else if(view.getId() == R.id.LoadFor){
                Intent intent = new Intent(this, ForegroundImageService.class);
                intent.setAction(ForegroundImageService.STARTFOREGROUND_ACTION);
                intent.putExtra("EXTRA_URL", url);
                System.out.println("intent foreground");
                startService(intent);
            }
        }
    }
}