package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.drivemode.android.typeface.TypefaceHelper;

/**
 * Created by KD on 4/11/2015.
 */
public class PasscodeAuthenticationActivity extends Activity {

    private int count = 0;
    EditText et_passcode1, et_passcode2, et_passcode3, et_passcode4;
    String passcodeValue="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.passcode_activity);
        ((TextView) findViewById(R.id.tv_passcodeLabel)).setText(R.string.passcode_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView = TypefaceHelper.getInstance().setTypeface(this, R.layout.main_actionbar, "fonts/Helvetica-Light.otf");
        actionBar.setCustomView(customView);

        /* Removing extra padding in action bar */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar parent =(Toolbar) customView.getParent();
            parent.setContentInsetsAbsolute(0,0);
        }

        updateActionBar();

        et_passcode1 = ((EditText)findViewById(R.id.et_passcodeValue1));
        et_passcode1.addTextChangedListener(new GenericTextWatcher(et_passcode1));

        et_passcode2 = ((EditText)findViewById(R.id.et_passcodeValue2));
        et_passcode2.addTextChangedListener(new GenericTextWatcher(et_passcode2));
        et_passcode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    et_passcode2.setText("");
                    passcodeValue = Util.removeLastChar(passcodeValue);
                    Util.changeFocus(et_passcode2, et_passcode1);

                }
                return false;
            }
        });

        et_passcode3 = ((EditText)findViewById(R.id.et_passcodeValue3));
        et_passcode3.addTextChangedListener(new GenericTextWatcher(et_passcode3));
        et_passcode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    et_passcode3.setText("");
                    passcodeValue = Util.removeLastChar(passcodeValue);
                    Util.changeFocus(et_passcode3, et_passcode2);

                }
                return false;
            }
        });

        et_passcode4 = ((EditText)findViewById(R.id.et_passcodeValue4));
        et_passcode4.addTextChangedListener(new GenericTextWatcher(et_passcode4));
        et_passcode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    et_passcode4.setText("");
                    passcodeValue = Util.removeLastChar(passcodeValue);
                    Util.changeFocus(et_passcode4, et_passcode3);
                }
                return false;
            }
        });
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (!text.isEmpty()) {
                passcodeValue += text;
                switch (view.getId()) {
                    case R.id.et_passcodeValue1:
                        Log.e("MacGo:editText" + view, "after");
                        Util.changeFocus(et_passcode1, et_passcode2);
                        break;
                    case R.id.et_passcodeValue2:
                        Log.e("MacGo:editText" + view, "after");
                        Util.changeFocus(et_passcode2, et_passcode3);
                        break;
                    case R.id.et_passcodeValue3:
                        Log.e("MacGo:editText" + view, "after");
                        Util.changeFocus(et_passcode3, et_passcode4);
                        break;
                    case R.id.et_passcodeValue4:
                        Log.e("MacGo:editText" + view, "after");
                        /* 4 Digit passcode */
                        if (passcodeValue.length() == 4) {
                            confirmPasscode();
                        }
                        break;
                }

            }
        }
    }

    public void confirmPasscode(){
        String css[] = Util.readDataFromStorage(getApplicationContext());
        if (passcodeValue.equals(css[1])) {
            startActivity(new Intent(PasscodeAuthenticationActivity.this, PrePurchaseActivity.class));
            finish();
        } else {
            count++;
            passcodeValue="";
            resetEditTexts();
            Util.changeFocus(et_passcode4,et_passcode1);
            Toast.makeText(getApplicationContext(), "Attempt " + count + " failed", Toast.LENGTH_SHORT).show();
            if (count == 3) {
                startActivity(new Intent(PasscodeAuthenticationActivity.this, LoginActivity.class));
                finish();
            }
        }
    }

    public void resetEditTexts(){
        et_passcode1.setText("");
        et_passcode2.setText("");
        et_passcode3.setText("");
        et_passcode4.setText("");
    }

    public void updateActionBar(){
        TextView desc = (TextView) findViewById(R.id.item_purchaseDesc);
        findViewById(R.id.item_purchaseDate).setVisibility(View.GONE);
        findViewById(R.id.item_close).setVisibility(View.INVISIBLE);
        desc.setTextSize(25);
        desc.setText("Passcode");
        findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
