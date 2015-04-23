package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.drivemode.android.typeface.TypefaceHelper;

import java.util.HashMap;

public class ChangePasscodeActivity extends Activity {

    private TextView passcodeTextView;
    private int count = 0;
    private String passcode1 = "";
    private String passcode2 = "";
    EditText et_passcode1, et_passcode2, et_passcode3, et_passcode4;
    private String passcodeValue = "";

    HashMap<String, String> hashMap;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        hashMap = new HashMap<String, String>();
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

        passcodeTextView = (TextView) findViewById(R.id.tv_passcodeLabel);
        passcodeTextView.setText(R.string.passcode_new_text);
        et_passcode1 = ((EditText)findViewById(R.id.et_passcodeValue1));
        et_passcode1.addTextChangedListener(new GenericTextWatcher(et_passcode1));
        // Additional feature
        /*et_passcode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!isEmptyEditText(et_passcode1) &&
                    keyCode != KeyEvent.KEYCODE_DEL){
                    et_passcode1.setFocusable(false);
                    et_passcode2.setText(new StringBuilder().append("").append(event.getUnicodeChar(keyCode)).toString());
                    et_passcode2.setFocusableInTouchMode(true);
                    et_passcode2.requestFocus();
                }
                return false;
            }
        });*/

        et_passcode2 = ((EditText)findViewById(R.id.et_passcodeValue2));
        et_passcode2.addTextChangedListener(new GenericTextWatcher(et_passcode2));
        et_passcode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    et_passcode2.post(new Runnable() {
                        public void run() {
                            et_passcode2.setText("");
                            passcodeValue = Util.removeLastChar(passcodeValue);
                            Util.changeFocus(et_passcode2, et_passcode1);
                        }
                    });

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
                    et_passcode3.post(new Runnable() {
                        public void run() {
                            et_passcode3.setText("");
                            passcodeValue = Util.removeLastChar(passcodeValue);
                            Util.changeFocus(et_passcode3, et_passcode2);
                        }
                    });

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
                    et_passcode4.post(new Runnable() {
                        public void run() {
                            et_passcode4.setText("");
                            passcodeValue = Util.removeLastChar(passcodeValue);
                            Util.changeFocus(et_passcode4, et_passcode3);
                        }
                    });
                }
                return false;
            }
        });
    }
    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if(!text.isEmpty()) {
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
                        if (passcodeValue.length()==4){
                            confirmPasscode();
                        }
                        break;
                }

            }
        }
    }
    public boolean checkOldPassword(){

        String css[] = Util.readDataFromStorage(getApplicationContext());
        if(css[2].length() == 4) {
            while (true) {
                passcodeTextView.setText("Enter your old password");
                if (css[2].equals(passcodeValue)) {
                    return true;
                }
                resetEditTexts();
                Util.changeFocus(et_passcode4, et_passcode1);
                passcodeValue="";
            }
        } else return true;

    }
    public void confirmPasscode() {
        if (count < 1) {
            passcode1 = passcodeValue;
            resetEditTexts();
            Util.changeFocus(et_passcode4, et_passcode1);
            passcodeTextView.setText(R.string.passcode_confirm);
            count++;
        } else {
            passcode2 = passcodeValue;
            if (passcode1.equals(passcode2)) {

                Util.writeDataToStorage(new String[]{"1", passcodeValue, "-1"}, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Passcode Saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Passcode doesn't match", Toast.LENGTH_SHORT).show();
                count = 0;
                Util.changeFocus(et_passcode4, et_passcode1);
                resetEditTexts();
                passcodeTextView.setText(R.string.passcode_new_text);
            }
        }
        passcodeValue = "";
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
        desc.setTextSize(25);
        desc.setText("Passcode");
        findViewById(R.id.item_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        String css[] = Util.readDataFromStorage(getApplicationContext());
        if (css[0].equals("-1"))
            Util.writeDataToStorage(new String[]{"0","-1","-1"},this);
        super.onBackPressed();
    }
}

