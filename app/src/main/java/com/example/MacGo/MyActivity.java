package com.example.MacGo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.parse.ParseUser;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, PrePurchaseActivity.class));
            finish();
        } else {
            //Start and intent for the logged out activity
            //Invalid User
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }
}
