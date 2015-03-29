package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * Created by KD on 11/17/2014.
 */
public class LoginActivity extends Activity{

    private EditText usernameView, passwordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Set up the login form.
        usernameView = (EditText) findViewById(R.id.user_name);
        passwordView = (EditText) findViewById(R.id.password);

        TypefaceHelper.getInstance().setTypeface(this, "fonts/Helvetica-Light.otf");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.i("MacGo", "Enter pressed");
                    if(isNetworkAvailable()) {

                        boolean validateError = false;
                        StringBuilder validaitionErrorMessage = new StringBuilder("Please");

                        if (isEmpty(usernameView)) {
                            validateError = true;
                            validaitionErrorMessage.append("Enter a UserName");
                        }
                        if (isEmpty(passwordView)) {
                            if (validateError) {
                                validaitionErrorMessage.append(", and");
                            }
                            validateError = true;
                            validaitionErrorMessage.append("Enter a Password");
                        }
                        if (validateError) {
                            Toast.makeText(LoginActivity.this, validaitionErrorMessage.toString(), Toast.LENGTH_LONG).show();
                            return false;
                        }

                        final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                        dlg.setTitle("Please wait.");
                        dlg.setMessage("Logging in.  Please wait.");
                        dlg.show();

                        // Calling the Parse login method
                        ParseUser.logInInBackground(usernameView.getText().toString().toLowerCase(), passwordView.getText().toString(), new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {
                                dlg.dismiss();
                                if (e != null) {
                                    // Show the error message
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    resetLayout();
                                } else {
                                    // Start an intent for the dispatch activity
                                    Intent intent = new Intent(LoginActivity.this, PrePurchaseActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "No Network Connection Detected!", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }

    private boolean isEmpty(EditText eText) {
        if (eText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void resetLayout(){
        usernameView.setText("");
        passwordView.setText("");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
