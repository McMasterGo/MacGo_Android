package com.example.MacGo;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.*;
import android.app.Application;
import android.os.Debug;

/**
 * Created by KD on 11/16/2014.
 */
public class MacGoApplication extends Application {
    public void onCreate() {
        TypefaceHelper.initialize(this);
        ParseCrashReporting.enable(this);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
    }

    @Override
    public void onTerminate() {
        TypefaceHelper.destroy();
        super.onTerminate();
    }
}
