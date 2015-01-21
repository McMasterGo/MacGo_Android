package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KD on 1/16/2015.
 */
public class ItemHistory extends Activity{
    private RecyclerView mItemView ;
    private ParseObject purchaseId;
    private RecyclerView.Adapter mItemAdapter;
    private RecyclerView.LayoutManager mItemLayout;
    final ArrayList<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view);
        mItemView = (RecyclerView) findViewById(R.id.item_history);
        mItemView.setHasFixedSize(true);
        mItemLayout = new LinearLayoutManager(this);
        mItemView.setLayoutManager(mItemLayout);

        purchaseId = Util.getParseObject();
        BlurBehind.getInstance().setBackground(this);

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getActionBar().setCustomView(R.layout.item_actionbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        TextView itemDate = (TextView) findViewById(R.id.item_purchaseDate);
        TextView itemDesc = (TextView) findViewById(R.id.item_purchaseDesc);
        itemDesc.setText(purchaseId.getString("Description"));
        itemDate.setText(Item.covertDataFormat(purchaseId.getCreatedAt(),"MMMM dd, yyyy"));



        //ParseProxyObject purchaseId = (ParseProxyObject) intent.getSerializableExtra("purchaseId");
        //purchaseId = extras.getgetString("purchaseId");
        getItem( purchaseId);

        //mItemAdapter = new ItemAdapter(itemList);
        //mItemView.setAdapter(mItemAdapter);
    }

    void getItem(ParseObject purchaseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PurchaseItem");
        //ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");
        ParseObject pPurchase = new ParseObject("Purchase");
        //itemQuery.whereEqualTo("objectId",pPurchase.getParseObject("item"));
        //ParseObject pItem = new ParseObject("Item");
        query.whereEqualTo("purchase",purchaseId);
        query.include("item");
            //query.include("item.name");
        //query.whereMatchesQuery("item", itemQuery);
        //query.whereEqualTo("item", pItem.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> items, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject item : items) {
                        ParseObject currItem = item.getParseObject("item");

                        Log.d("Items", "Items " + items.size() + " Size");
                        //itemList.add(currItem.);
                        itemList.add(new Item( currItem.getObjectId(), currItem.getString("name"),
                                currItem.getNumber("price").floatValue(),currItem.getNumber("calories").intValue(),
                                currItem.getParseObject("category")) );


                    }
                    for (int j =0 ; j< itemList.size();j++) {
                        Log.e("Object Id" ,"Object"+itemList.get(j).getItemId());
                        Log.e("Description ","Name"+itemList.get(j).getItemName());
                        Log.e("Cost","Cost"+itemList.get(j).getItemPrice());
                        Log.e("Cat","Category"+itemList.get(j).getItemCategory().getObjectId());
                        Log.e("Calories","Calories"+itemList.get(j).getItemCalories());
                    }

                } else {
                    Log.d("item", "Error: " + e.getMessage());
                }
                mItemAdapter = new ItemAdapter(itemList);
                mItemView.setAdapter(mItemAdapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
}
