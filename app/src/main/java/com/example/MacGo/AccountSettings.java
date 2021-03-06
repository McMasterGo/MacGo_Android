package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.rey.material.widget.Switch;


import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.ParseUser;


/**
 * Created by KD on 4/10/15.
 */
public class AccountSettings extends Activity {
    private TextView changePasscode;
    private Switch passcodeSwitch;
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);
        changePasscode = (TextView) findViewById(R.id.change_passcode);
        TypefaceHelper.getInstance().setTypeface(this, "fonts/Helvetica-Light.otf");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(TypefaceHelper.getInstance().setTypeface(this, R.layout.main_actionbar, "fonts/Helvetica-Light.otf"));
        updateActionBar();

        findViewById(R.id.action_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().logOut();
                Intent intent = new Intent(AccountSettings.this, MyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Util.resetData(getApplicationContext());
                finish();
            }
        });

        passcodeSwitch = (Switch) findViewById(R.id.passcode_switch);
        String css[] = Util.readDataFromStorage(getApplicationContext());

        changePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callChangePasscode();
            }
        });
        changePasscode.setText(R.string.change_password_text);

        passcodeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                if (b) {
                    changePasscode.setEnabled(true);
                    String css[] = Util.readDataFromStorage(getApplicationContext());
                    if (css[0].equals("-1")) {
                        Util.writeDataToStorage(new String[]{"1", "-1", css[2]}, getApplicationContext());
                        callChangePasscode();
                    }
                } else {
                    changePasscode.setEnabled(false);
                    String[] css = Util.readDataFromStorage(getApplicationContext());
                    Util.writeDataToStorage(new String[]{"-1","-1",css[1]}, getApplicationContext());
                }
            }
        });

        if (css[0].equals("1")) {
            passcodeSwitch.setChecked(true);
        } else if (css[0].equals("-1")) {
            // Password is not set
            passcodeSwitch.setChecked(false);
        } else if (css[0].equals("0")){
            passcodeSwitch.setChecked(false);
        }
    }

    public void onResume(){
        String css[] = Util.readDataFromStorage(getApplicationContext());
        if(css[1].equals("-1")) {
            passcodeSwitch.setChecked(false);
        }
        super.onResume();
    }

    public void callChangePasscode(){
        Intent intent = new Intent(AccountSettings.this, ChangePasscodeActivity.class);
        startActivity(intent);
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
