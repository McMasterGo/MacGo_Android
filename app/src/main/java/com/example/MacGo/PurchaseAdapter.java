package com.example.MacGo;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by KD on 1/5/2015.
 */
public class PurchaseAdapter extends RecyclerView.Adapter <PurchaseAdapter.MyViewHolder>{

    private ArrayList<Purchase> purchase;
    //private PurchaseHistory.ClickListener clickListener;

    public PurchaseAdapter(ArrayList<Purchase> purchaseList) {
        this.purchase = purchaseList;
    }


    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView desc = (TextView) holder.view.findViewById(R.id.purchase_desc);
        //TextView items = (TextView) holder.view.findViewById(R.id.purchase_items);
        TextView date = (TextView) holder.view.findViewById(R.id.purchase_date);
        TextView cost = (TextView) holder.view.findViewById(R.id.purchase_cost);


        desc.setText(purchase.get(position).getPurchaseDescription());
        cost.setText("$"+ Float.toString(purchase.get(position).getPurchaseCost()));
        date.setText(purchase.get(position).getPurchaseDateString("MMMM dd, yyyy"));
        if(position %2 ==0) {
            desc.setTextColor(Color.parseColor("#d0c17d"));
            date.setTextColor(Color.parseColor("#d0c17d"));
            cost.setTextColor(Color.parseColor("#d0c17d"));
        }
        //items.setText(purchase.get(position).getItemIdList().size()+" Items");
    }


    @Override
    public int getItemCount() {
        return purchase.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}