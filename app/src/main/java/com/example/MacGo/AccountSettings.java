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
                Util.writeDataToStorage("", getApplicationContext());
                finish();
            }
        });

        Switch passcodeSwitch = (Switch) findViewById(R.id.passcode_switch);
        String css = Util.readDataFromStorage(getApplicationContext());
        String passcodeAttributes[] = css.split(",");

        changePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettings.this, ChangePasscodeActivity.class);
                startActivity(intent);
            }
        });


        passcodeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch aSwitch, boolean b) {
                if (b) {
                    changePasscode.setEnabled(true);
                    String css = Util.readDataFromStorage(getApplicationContext());
                    String passcodeAttributes[] = css.split(",");
                    if (passcodeAttributes.length <= 1) {
                        Util.writeDataToStorage("1", getApplicationContext());
                        Intent intent = new Intent(AccountSettings.this, ChangePasscodeActivity.class);
                        startActivity(intent);
                    } else {
                        Util.writeDataToStorage("1\n" + passcodeAttributes[1], getApplicationContext());
                    }
                } else {
                    changePasscode.setEnabled(false);
                    String css = Util.readDataFromStorage(getApplicationContext());
                    String passcodeAttributes[] = css.split(",");
                    if (passcodeAttributes.length == 1) {
                        Util.writeDataToStorage("0", getApplicationContext());
                    } else {
                        Util.writeDataToStorage("0\n" + passcodeAttributes[1], getApplicationContext());
                    }
                }
            }
        });

        if(passcodeAttributes[0].length() == 1){
            changePasscode.setText(R.string.change_password_text);
            if(passcodeAttributes[0].equals("1")){
                passcodeSwitch.setChecked(true);
            } else {
                passcodeSwitch.setChecked(false);
            }
        } else {
            //Passcode is not set
            changePasscode.setText(R.string.setup_password_text);
            passcodeSwitch.setChecked(false);
        }
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
