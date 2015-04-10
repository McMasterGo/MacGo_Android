package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * Created by KD on 4/10/15.
 */
public class AccountSettings extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        TypefaceHelper.getInstance().setTypeface(this, "fonts/Helvetica-Light.otf");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(TypefaceHelper.getInstance().setTypeface(this,R.layout.item_actionbar, "fonts/Helvetica-Light.otf"));
        updateActionBar();

        findViewById(R.id.action_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(AccountSettings.this, MyActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void updateActionBar(){
        TextView desc = (TextView) findViewById(R.id.item_purchaseDesc);
        findViewById(R.id.item_purchaseDate).setVisibility(View.GONE);
        desc.setTextSize(25);
        desc.setText("Settings");
        findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
