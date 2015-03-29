package com.example.MacGo;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.Parse;
import android.app.Application;

/**
 * Created by KD on 11/16/2014.
 */
public class MacGoApplication extends Application {
    public void onCreate() {
        TypefaceHelper.initialize(this);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }

    @Override
    public void onTerminate() {
        TypefaceHelper.destroy();
        super.onTerminate();
    }
}
