package com.example.MacGo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.drivemode.android.typeface.TypefaceHelper;
import com.parse.ParseObject;

/**
 * Created by KD on 1/30/2015.
 */
public class PurchaseItemHistory extends Activity{

    PurchaseHistory mPurchaseHistoryView;
    ItemHistory mItemHistoryView;
    FrameLayout rootLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_item_history);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
        //getActionBar().setCustomView(R.layout.main_actionbar);
        getActionBar().setCustomView(TypefaceHelper.getInstance().setTypeface(this,R.layout.main_actionbar, "fonts/Helvetica-Light.otf"));
        //getActionBar().setLogo(R.drawable.ic_ab_up);
        ImageButton itemDate = (ImageButton)findViewById(R.id.item_close);
        itemDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPurchaseHistoryView = new PurchaseHistory(this);

        mItemHistoryView = new ItemHistory(this);

        rootLayout = (FrameLayout) findViewById(R.id.rootLayout);

        rootLayout.addView(mPurchaseHistoryView);
        updateActionBar();
    }

    public void removePurchaseView(){
        if(isNetworkAvailable()) {
            mItemHistoryView.clearItemView();
            mItemHistoryView.getItem(Util.getParseObject());

            rootLayout.addView(mItemHistoryView);
            mItemHistoryView.setAlpha(0f);
            mItemHistoryView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setListener(null);

            mPurchaseHistoryView.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rootLayout.removeView(mPurchaseHistoryView);
                            updateActionBar();
                        }
                    });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ( !(activeNetworkInfo != null && activeNetworkInfo.isConnected()) ) {
            Toast.makeText(PurchaseItemHistory.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateActionBar(){
        ParseObject purchaseId = Util.getParseObject();
        TextView itemDate = (TextView) findViewById(R.id.item_purchaseDate);
        TextView itemDesc = (TextView) findViewById(R.id.item_purchaseDesc);

        itemDate.setVisibility(View.VISIBLE);
        itemDesc.setTextSize(25);
        if(rootLayout.getChildAt(0) instanceof PurchaseHistory) {
            itemDate.setVisibility(View.GONE);
            itemDesc.setText("History");
        } else if (rootLayout.getChildAt(0) instanceof  ItemHistory){
            itemDesc.setText(purchaseId.getString("Description"));
            itemDate.setText(Item.covertDataFormat(purchaseId.getCreatedAt(),"MMMM dd, yyyy"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed(){
        Boolean isItemHistoryViewFound = false;
        for(int i=0; i<rootLayout.getChildCount(); ++i) {
            View nextChild = rootLayout.getChildAt(i);
            if(nextChild instanceof ItemHistory){

                rootLayout.addView(mPurchaseHistoryView);
                mPurchaseHistoryView.setAlpha(0f);
                mPurchaseHistoryView.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .setListener(null);

                mItemHistoryView.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                rootLayout.removeView(mItemHistoryView);
                                updateActionBar();
                            }
                        });
                isItemHistoryViewFound = true;
            }
        }
        if(!isItemHistoryViewFound){
            finish();
        }
    }
}
