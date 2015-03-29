package com.example.MacGo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
public class ItemHistory extends FrameLayout {
    private RecyclerView mItemRecyclerView ;
    private ParseObject purchaseId;
    private ItemAdapter mItemAdapter;
    private RecyclerView.LayoutManager mItemLayout;
    final ArrayList<Item> itemList = new ArrayList<Item>();

    public ItemHistory(Context context) {
        super(context);
        View itemView = inflate(context, R.layout.item_view, this);
        mItemRecyclerView= (RecyclerView) itemView.findViewById(R.id.item_history);
        mItemRecyclerView.setHasFixedSize(true);
        mItemLayout = new LinearLayoutManager(context);
        mItemRecyclerView.setLayoutManager(mItemLayout);

        purchaseId = Util.getParseObject();
        getItem( purchaseId);
    }

    public void getItem(ParseObject purchaseId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PurchaseItem");
        query.whereEqualTo("purchase",purchaseId);
        query.include("item");
        query.addAscendingOrder("quantity");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> items, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject item : items) {
                        ParseObject currItem = item.getParseObject("item");

                        Log.d("Items", "Items " + items.size() + " Size");
                        itemList.add(new Item( currItem.getObjectId(), currItem.getString("name"),
                                currItem.getNumber("price").floatValue(),currItem.getNumber("calories").intValue(),
                                item.getNumber("quantity"),currItem.getParseObject("category")) );
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
                mItemRecyclerView.setAdapter(mItemAdapter);
            }
        });
    }

    public void clearItemView(){
        mItemAdapter.clearItems();
        mItemRecyclerView.setAdapter(mItemAdapter);
    }
}
