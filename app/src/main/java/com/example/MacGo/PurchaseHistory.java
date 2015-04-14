package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;


/**
 * Created by KD on 1/5/2015.
 */
public class PurchaseHistory extends FrameLayout {
    private RecyclerView mPurchaseRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrameLayout mFrameLayout;
    Context mContext;
    private ImageView image;
    private int count = 1;

    final ArrayList<Purchase> purchaseList = new ArrayList<Purchase>();

    public PurchaseHistory(final Context context) {
        super(context);
        View purchaseView = inflate(context, R.layout.purchase_history_view, this);
        PurchaseItemHistory parentActivity = (PurchaseItemHistory) context;


        mPurchaseRecyclerView = (RecyclerView) purchaseView.findViewById(R.id.purchase_history);
        mPurchaseRecyclerView.setHasFixedSize(true);
        mContext = context;


        mLayoutManager = new LinearLayoutManager(context);
        mPurchaseRecyclerView.setLayoutManager(mLayoutManager);
        mPurchaseRecyclerView.addOnItemTouchListener(
                new PurchaseTouchListener (mContext, new PurchaseTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Log.d("The pos is" + position, "Position");
                        Util.setObject(getPurchaseId(position));
                        ((PurchaseItemHistory)context).removePurchaseView();
                    }
                })
        );

        getData();
    }

    public ParseObject getPurchaseId(int i) {
        return purchaseList.get(i).purchaseId;
    }

    void getItem(ParseObject purchaseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PurchaseItem");
        ParseObject pPurchase = new ParseObject("Purchase");
        query.whereEqualTo("purchase",purchaseId);
        query.include("Item");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> items, com.parse.ParseException e) {
                if (e == null) {
                    int i =0;
                    for (ParseObject item : items) {
                        ParseObject currItem = item.getParseObject("Item");
                        Log.d("Items", "Items " + items.size() + " Size");
                        i++;
                    }

                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void getData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Purchase");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback <ParseObject>() {
           @Override
            public void done(List<ParseObject> purchase, com.parse.ParseException e) {
                if (e == null) {
                    for (int i =0; i<purchase.size();i++) {

                        purchaseList.add(new Purchase(purchase.get(i),
                            purchase.get(i).getString("Description"),
                            purchase.get(i).getNumber("totalCost").floatValue(),
                            purchase.get(i).getCreatedAt()));
                    }

                    for (int i =0 ; i< purchaseList.size();i++) {
                        Log.e("Description ","Description"+purchaseList.get(i).getPurchaseDescription());
                        Log.e("Cost","Cost"+purchaseList.get(i).getPurchaseCost());
                        Log.e("Date","Date"+purchaseList.get(i).getPurchaseDate());
                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
               mAdapter = new PurchaseAdapter(purchaseList);
               mPurchaseRecyclerView.setAdapter(mAdapter);
            }
        });


    }
   static class PurchaseTouchListener implements RecyclerView.OnItemTouchListener {
       private OnItemClickListener mListener;
       public interface OnItemClickListener {
           public void onItemClick(View view, int position);
       }
       GestureDetector mGestureDetector;
       public PurchaseTouchListener(Context context, OnItemClickListener listener) {
           mListener = listener;
           mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
               @Override public boolean onSingleTapUp(MotionEvent e) {
                   return true;
               }
           });
       }


       @Override
       public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
           View child=rv.findChildViewUnder(e.getX(), e.getY());
           if(child!=null && mListener!=null && mGestureDetector.onTouchEvent(e))
           {
               mListener.onItemClick(child, rv.getChildPosition(child));
           }

           return false;
       }

       @Override
       public void onTouchEvent(RecyclerView rv, MotionEvent e) {

       }
   }
}
