package com.example.MacGo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.parse.Parse;
import android.app.Application;

/**
 * Created by KD on 11/16/2014.
 */
public class MacGoApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }
}
