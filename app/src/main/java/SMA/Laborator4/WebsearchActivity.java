package SMA.Laborator4;

import androidx.appcompat.app.AppCompatActivity;
import SMA.Laborator4.MyIntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

        loadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foregroundLoadImage();
            }
        });

        ((MyCustomApplication)getApplicationContext()).getBitmap();
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
}