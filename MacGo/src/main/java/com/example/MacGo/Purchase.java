package com.example.MacGo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import java.sql.Date;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.parse.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KD on 11/18/2014.
 */
public class Purchase extends Activity{
    private Bitmap qrCode;
    private static final String TAG = "MacGo-Debug";
    private String tokenId = null;
    Calendar expiryDate;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        final ParseObject token = new ParseObject("Tokens");

        populateToken(token);

        Calendar currentTime = Calendar.getInstance();
        //Log.i("Expiry Date is : ",expiryDate.getTime().toString());

        new Timer().schedule(new TimerTask(){
            public void run() {
                //startActivity(new Intent(Purchase.this, PrePurchaseActivity.class));
                finish();
            }
        }, 5000);
        //String img = ParseUser.getCurrentUser().get("objectId").toString().concat("qrCode");

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
