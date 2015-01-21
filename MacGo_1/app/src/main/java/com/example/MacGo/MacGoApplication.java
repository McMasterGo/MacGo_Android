package com.example.MacGo;

import com.parse.Parse;
import android.app.Application;

/**
 * Created by KD on 11/16/2014.
 */
public class MacGoApplication extends Application {
    public void onCreate() {
        TypefaceManager.initialize(this, R.xml.fonts);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }
}
