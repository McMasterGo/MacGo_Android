package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.parse.*;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;


/**
 * Created by KD on 1/5/2015.
 */
public class PurchaseHistory extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FrameLayout mFrameLayout;
    private ImageView image;
    private int count = 1;

    final ArrayList<Purchase> purchaseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_history_view);
        //android.support.v7.app.ActionBar actionBar =getSupportActionBar();
        // getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.purchase_actionbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //setupActionBar();
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.purchase_actionbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.purchase_history);
        mRecyclerView.setHasFixedSize(true);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.70f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //BlurBehind.getInstance().setBackground(this);
        mFrameLayout = (FrameLayout) findViewById(R.id.purchase_frame);
        mFrameLayout.getBackground().setAlpha(0);
       // BlurBehind.getInstance().setBackground(mFrameLayout);
        /*Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        BitmapDrawable ob = new BitmapDrawable(getResources(),bmp);
        mFrameLayout.setBackground(ob);*/
        /*image = (ImageView)findViewById(R.id.blur_bg);
        ImageView image = (ImageView) findViewById(R.id.blur_bg);
        image.setImageBitmap(bmp);
        /*
        mFrameLayout.setDrawingCacheEnabled(true);
        mFrameLayout.buildDrawingCache();
        Bitmap bitmapOriginal = mFrameLayout.getDrawingCache();
        BitmapDrawable ob = new BitmapDrawable(getResources(),bitmapOriginal);
        image = (ImageView)findViewById(R.id.blur_bg);
        image.setBackground(ob);
        applyBlur();
*/
/*        //this will blur the bitmapOriginal with a radius of 8 and save it in bitmapOriginal
        final Allocation input = Allocation.createFromBitmap(rs, bitmapOriginal); //use this constructor for best performance, because it uses USAGE_SHARED mode which reuses memory
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(8f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmapOriginal);
        BitmapDrawable ob = new BitmapDrawable(getResources(), blur(getApplicationContext(),bitmapOriginal));
        bk.setBackground(ob);
*/
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(
            new PurchaseTouchListener (this, new PurchaseTouchListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {

                    Log.d("The pos is"+position,"POsition");
                    Intent intent = new Intent(PurchaseHistory.this, ItemHistory.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    //getItem(getPurchaseId(position));
                    //intent.put
                    //intent.putExtra("purchaseId",getP);
                    Util.setObject(getPurchaseId(position));
                    startActivity(intent);
                    /* do whatever
                    Intent intent = new Intent(this, abc.class);
                    intent.putExtra("purchaseId", purchaseList.get(position).purchaseId);*/
                }
            })
        );


        getData();
        ArrayList<StaticData> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new StaticData(
                    "Title" + i,
                    "This is description" + i));
        }
                //mAdapter = new PurchaseAdapter(purchaseList);
        //mRecyclerView.setAdapter(mAdapter);
    }

    /*
    private void setupActionBar() {
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        View customNav = LayoutInflater.from(this).inflate(R.layout.purchase_actionbar, null); // layout which contains your button.

        actionBar.setCustomView(customNav, lp1);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
    public ParseObject getPurchaseId(int i) {
        return purchaseList.get(i).purchaseId;
    }

    void getItem(ParseObject purchaseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PurchaseItem");
        //ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");
        ParseObject pPurchase = new ParseObject("Purchase");
        //itemQuery.whereEqualTo("objectId",pPurchase.getParseObject("item"));
        //ParseObject pItem = new ParseObject("Item");
        query.whereEqualTo("purchase",purchaseId);
        query.include("Item");
        //query.whereMatchesQuery("item", itemQuery);
        //query.whereEqualTo("item", pItem.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> items, com.parse.ParseException e) {
                if (e == null) {
                    int i =0;
                    for (ParseObject item : items) {
                        ParseObject currItem = item.getParseObject("Item");
                        //itemList.add(currItem.);
                        Log.d("Items", "Items " + items.size() + " Size");
                        i++;
                    }

                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                //mItemAdapter = new ItemAdapter(itemList);
                //mItemView.setAdapter(mItemAdapter);
            }
        });
    }
   /* public void getItems(final String purchaseId, final int index) {

        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("PurchaseItem");
        ParseObject pPurchase = new ParseObject("PurchaseItem");
        itemQuery.whereEqualTo(purchaseId, pPurchase.getParseObject("purchase"));
        itemQuery.findInBackground(new FindCallback <ParseObject>() {
            @Override
            public void done(List<ParseObject> items, ParseException e) {
                if (e == null) {
                    Log.d("score", "Items " + items.size() + " Size");

                    for (int i = 0; i < items.size(); i++) {
                        if (purchaseId.equals( items.get(i).getParseObject("purchase").getObjectId()) ) {
                            purchaseList.get(index).itemIdList.add(items.get(i).getParseObject("item"));
                        }
                    }
                } else {
                    Log.d("Items", "Error: " + e.getMessage());
                }
                if( count == purchaseList.size()) {

                }
                count++;
            }
        });
    }*/

    public void getData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Purchase");
        //ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("PurchaseItem");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback <ParseObject>() {
           @Override
            public void done(List<ParseObject> purchase, com.parse.ParseException e) {
                if (e == null) {

                    //Log.d("score", "Retrieved " + purchase.size() + " scores");
                    //Code to update if size is greater than 100
                    for (int i =0; i<purchase.size();i++) {

                        purchaseList.add(new Purchase(purchase.get(i),
                            purchase.get(i).getString("Description"),
                            purchase.get(i).getNumber("totalCost").floatValue(),
                            purchase.get(i).getCreatedAt()));

                        //getItems(purchase.get(i).getObjectId(),i);
                    }

                    for (int i =0 ; i< purchaseList.size();i++) {
                        //Log.e("Object Id" ,"Object"+purchaseList.get(i).getPurchaseId());
                        Log.e("Description ","Description"+purchaseList.get(i).getPurchaseDescription());
                        Log.e("Cost","Cost"+purchaseList.get(i).getPurchaseCost());
                        Log.e("Date","Date"+purchaseList.get(i).getPurchaseDate());
                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
               mAdapter = new PurchaseAdapter(purchaseList);
               mRecyclerView.setAdapter(mAdapter);
            }
        });


    }
   static class PurchaseTouchListener implements RecyclerView.OnItemTouchListener {
       //private GestureDetector gestureDetector;
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
