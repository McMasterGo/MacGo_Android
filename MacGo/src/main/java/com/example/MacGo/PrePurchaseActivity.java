package com.example.MacGo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by KD on 11/17/2014.
 */
public class PrePurchaseActivity extends Activity {
    private TextView welcomeText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_purchase_activity);

        welcomeText = (TextView)findViewById(R.id.txt_welcome) ;
        String userFullName = ParseUser.getCurrentUser().get("firstName").toString() + " " + ParseUser.getCurrentUser().get("lastName").toString();
        welcomeText.append(userFullName);

        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(PrePurchaseActivity.this, MyActivity.class));
                finish();
            }

        });
        findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(PrePurchaseActivity.this, Purchase.class));
                //finish();


            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
