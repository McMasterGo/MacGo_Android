package com.example.MacGo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by KD on 11/17/2014.
 */
public class PrePurchaseActivity extends Activity {
    private TextView userName;
    private TextView userBalance;
    private RelativeLayout mLayout;
    private FrameLayout mFrameLayout;
    private byte[] byteArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_purchase_activity);
        mFrameLayout = (FrameLayout) findViewById(R.id.pre_purchase);
        //mFrameLayout.getForeground().setAlpha(0);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        userName = (TextView)findViewById(R.id.txt_welcome) ;
        userBalance = (TextView)findViewById(R.id.txt_balance) ;

        String userFullName = ParseUser.getCurrentUser().get("firstName").toString() + " " + ParseUser.getCurrentUser().get("lastName").toString();
        if(ParseUser.getCurrentUser().getNumber("balance") != null) {
            userBalance.setText("$" + ParseUser.getCurrentUser().getNumber("balance").toString());
        }
        else {
            userBalance.setText("$0.00");
        }
        userName.append(userFullName);
        userName.setTextColor(Color.WHITE);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Helvetica-Light.otf");
        userName.setTypeface(tf, Typeface.NORMAL);



        findViewById(R.id.txt_welcome).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ParseUser.getCurrentUser().logOut();
                startActivity(new Intent(PrePurchaseActivity.this, MyActivity.class));
                finish();
            }

        });
        findViewById(R.id.btn_purchase).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //R mActivityView = (FrameLayout)findViewById(R.id.pre_purchase);

                //mFrameLayout.setBackground(ob);
               //mLayout = (RelativeLayout) findViewById(R.id.pre_purchase);
               //mLayout.setVisibility(View.GONE);

                startActivity(new Intent(PrePurchaseActivity.this, PurchaseToken.class));
                //mFrameLayout = (FrameLayout) findViewById(R.id.pre_purchase);
                //mFrameLayout.getForeground().setAlpha(220);
                //finish();
            }
        });

        findViewById(R.id.btn_recycle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Bitmap bmp = takeScreenShot(PrePurchaseActivity.this);
                //Bitmap fast=fastblur(bmp, 20);
                //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //fast.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byteArray = stream.toByteArray();
                /*Intent intent = new Intent(PrePurchaseActivity.this, RecyclerViewTest.class);
                //intent.putExtra("picture", byteArray);
                startActivity(intent);
                BlurBehind.getInstance().execute(PrePurchaseActivity.this, runnable);*/

                        Intent intent = new Intent(PrePurchaseActivity.this, PurchaseHistory.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    mFrameLayout.getForeground().setAlpha(220);
                        startActivity(intent);


                //startActivity(new Intent(PrePurchaseActivity.this, RecyclerViewTest.class));


                //Intent intent =  intent.putExtra("picture", byteArray);


                //finish();
            }
        });
    }
    /*
    @Override
    public void onStart() {

    }
*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private static Bitmap takeScreenShot(Activity activity)
    {
        Activity ac1 = activity;
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        //view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        //        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }


    public Bitmap fastblur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

}
