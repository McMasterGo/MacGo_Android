package com.example.MacGo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.GetCallback;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.text.DecimalFormat;

public class PrePurchaseActivity extends Activity {
    private TextView userName;
    private TextView userBalance;
    private Button refreshButton;
    private RippleView purchaseButton;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_purchase_activity);
        TypefaceHelper.getInstance().setTypeface(this, "fonts/Helvetica-Light.otf");

        userName = (TextView)findViewById(R.id.txt_welcome) ;
        userBalance = (TextView)findViewById(R.id.txt_balance) ;
        refreshButton = (Button) findViewById(R.id.btn_refresh);
        purchaseButton = (RippleView) findViewById(R.id.btn_purchase);
        purchaseButton.setBackgroundColor(Color.TRANSPARENT);

        findViewById(R.id.rv_account_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(PrePurchaseActivity.this, AccountSettings.class);
                    startActivity(intent);
                }
            }
        });


        findViewById(R.id.junk_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    findViewById(R.id.junk_category).setVisibility(View.INVISIBLE);
                    TextView junkCategoryStat = (TextView) findViewById(R.id.junk_category_stat);
                    junkCategoryStat.setVisibility(View.VISIBLE);
                    junkCategoryStat.setText("20%");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.junk_category_stat).setVisibility(View.INVISIBLE);
                            findViewById(R.id.junk_category).setVisibility(View.VISIBLE);
                        }
                    }, 2000);
                }
            }
        });

       findViewById(R.id.drinks_category).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isNetworkAvailable()) {
                   findViewById(R.id.drinks_category).setVisibility(View.INVISIBLE);
                   TextView drinksCategoryStat = (TextView) findViewById(R.id.drinks_category_stat);
                   drinksCategoryStat.setVisibility(View.VISIBLE);
                   drinksCategoryStat.setText("21%");
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           findViewById(R.id.drinks_category_stat).setVisibility(View.INVISIBLE);
                           findViewById(R.id.drinks_category).setVisibility(View.VISIBLE);
                       }
                   }, 2000);
               }
           }
       });

       findViewById(R.id.fruit_category).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isNetworkAvailable()) {
                   findViewById(R.id.fruit_category).setVisibility(View.INVISIBLE);
                   TextView fruitCategoryStat = (TextView) findViewById(R.id.fruit_category_stat);
                   fruitCategoryStat.setVisibility(View.VISIBLE);
                   fruitCategoryStat.setText("22%");
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           findViewById(R.id.fruit_category_stat).setVisibility(View.INVISIBLE);
                           findViewById(R.id.fruit_category).setVisibility(View.VISIBLE);
                       }
                   }, 2000);
               }
           }
       });

       findViewById(R.id.candy_category).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isNetworkAvailable()) {
                   findViewById(R.id.candy_category).setVisibility(View.INVISIBLE);
                   TextView candyCategoryStat = (TextView) findViewById(R.id.candy_category_stat);
                   candyCategoryStat.setVisibility(View.VISIBLE);
                   candyCategoryStat.setText("23%");
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           findViewById(R.id.candy_category_stat).setVisibility(View.INVISIBLE);
                           findViewById(R.id.candy_category).setVisibility(View.VISIBLE);
                       }
                   }, 2000);
               }
           }
       });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        String userFullName = ParseUser.getCurrentUser().get("firstName").toString() + " " + ParseUser.getCurrentUser().get("lastName").toString();
        refreshAccountBalance(ParseUser.getCurrentUser());
        userName.append(userFullName);
        userName.setTextColor(Color.WHITE);

        refreshButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    refreshAccountBalance(ParseUser.getCurrentUser());
                }
            }
        });

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkAvailable() && checkBalance()) {
                            refreshButton.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(PrePurchaseActivity.this, PurchaseToken.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            purchaseButton.setEnabled(false);
                        }
                    }
                }, getResources().getInteger(R.integer.ripple_duration)  * 2);
            }
        });

        findViewById(R.id.btn_recycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(PrePurchaseActivity.this, PurchaseItemHistory.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshAccountBalance(ParseObject userId) {
        userId.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                if (e == null) {

                    if (user.getNumber("balance") != null) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        userBalance.setText("$" + Util.getDesiredDecimalPrecesion(user.
                                getNumber("balance")));
                    } else {
                        userBalance.setText("0.0");
                    }
                    Log.d("Items", "Items " + user + " Size");
                } else {
                    Toast.makeText(PrePurchaseActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ( !(activeNetworkInfo != null && activeNetworkInfo.isConnected()) ) {
            Toast.makeText(PrePurchaseActivity.this, R.string.toast_internet, Toast.LENGTH_SHORT).show();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean checkBalance(){
        Float currBalance = ParseUser.getCurrentUser().getNumber("balance").floatValue();
        if (currBalance > 0) {
            return true;
        }
        Toast.makeText(PrePurchaseActivity.this, R.string.toast_balance, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        purchaseButton.setEnabled(true);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();
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