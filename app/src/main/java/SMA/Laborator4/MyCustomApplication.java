package SMA.Laborator4;

import android.app.Application;
import android.graphics.Bitmap;

public class MyCustomApplication extends Application {
    private static MyCustomApplication singleton;

    private Bitmap bitmap;

    public MyCustomApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        singleton = this;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

}
