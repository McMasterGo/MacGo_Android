package com.example.MacGo;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;
import java.sql.Date;

import com.drivemode.android.typeface.TypefaceHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.parse.*;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KD on 11/18/2014.
 */
public class PurchaseToken extends Activity{
    private Bitmap qrCode;
    private static final String TAG = "MacGo-Debug";
    private String tokenId = null;
    private Button cancelButton;
    private View pastActivity;
    private Button refreshButton;
    Calendar expiryDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);

        TypefaceHelper.getInstance().setTypeface(this, "fonts/Helvetica-Light.otf");
        final ParseObject token = new ParseObject("Tokens");
        cancelButton = (Button) findViewById(R.id.btn_cancel);
        populateToken(token);

        LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pastActivity = li.inflate(R.layout.pre_purchase_activity,null);
        refreshButton = (Button) pastActivity.findViewById(R.id.btn_refresh);
        refreshButton.setVisibility(View.INVISIBLE);
        new Timer().schedule(new TimerTask(){
            public void run() {
                finish();
            }
        }, 120000);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                updateToken(tokenId);
            }
        });
    }

    public void updateToken(String tokenId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tokens");
        // Retrieve the object by id
        query.getInBackground(tokenId, new GetCallback<ParseObject>() {
            public void done(ParseObject token, ParseException e) {
                if (e == null) {
                    token.put("active", false);
                    token.saveInBackground();
                }
            }
        });
    }

    /**
     * Writes the given Matrix on a new Bitmap object.
     * @param matrix the matrix to write.
     * @return the new {@link Bitmap}-object.
     */
    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int [] pixels = new int[width*height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                pixels[(y * width) + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                //bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    public void createQRCode(final String tokenId) {
        Log.i("Token ID = ",tokenId);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(tokenId, BarcodeFormat.QR_CODE, 400, 400);

            qrCode = toBitmap(matrix);
            Bitmap circleBitmap = Bitmap.createBitmap(qrCode.getWidth(), qrCode.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(qrCode,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(qrCode.getWidth()/2, qrCode.getHeight()/2, qrCode.getWidth()/2, paint);
            ImageView imgOnUI = (ImageView)findViewById(R.id.imageView);
            imgOnUI.setImageBitmap(circleBitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        };
    }
    public void populateToken(final ParseObject token) {
        token.put("active", true);
        expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.MINUTE, 2);
        Date abc = new Date(expiryDate.getTimeInMillis());

        //Log.i("Expiry Time",abc.toString());
        token.put("user",ParseUser.getCurrentUser());
        token.put("expiry",abc);


        token.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                //  Access the object id here
                //String tokenId = "";
                if (e == null) {
                    tokenId = token.getObjectId();
                    createQRCode(tokenId);
                    Log.e("blah", tokenId);
                }
                else {
                    Log.d(TAG, "User update error: " + e);
                }
                //return tokenId;
            }
        });
        //Log.i("TokenID",token.get("objectId").toString());
    }
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        finish();
    }

}
