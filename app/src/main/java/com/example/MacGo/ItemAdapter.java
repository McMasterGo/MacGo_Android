package com.example.MacGo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drivemode.android.typeface.TypefaceHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by KD on 1/16/2015.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<Item> items;

    public ItemAdapter(ArrayList<Item> itemList) {
        this.items = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView itemName = (TextView) holder.view.findViewById(R.id.item_desc);
        TextView itemQuantity = (TextView) holder.view.findViewById(R.id.item_quantity);

        TypefaceHelper.getInstance().setTypeface(itemName, "fonts/Helvetica-Light.otf");
        TypefaceHelper.getInstance().setTypeface(itemQuantity, "fonts/Helvetica-Light.otf");

        DecimalFormat df = new DecimalFormat("0.00");
        itemName.setText(items.get(position).getItemName());
        itemQuantity.setText(items.get(position).getPurchaseItemQuantity().toString());
        if(position %2 ==0) {
            itemName.setTextColor(Color.parseColor("#d0c17d"));
            itemQuantity.setTextColor(Color.parseColor("#d0c17d"));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
    public void clearItems(){
        this.items.clear();
    }
}
