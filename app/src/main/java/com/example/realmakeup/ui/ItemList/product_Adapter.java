package com.example.realmakeup.ui.ItemList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class product_Adapter extends BaseAdapter {

    ArrayList<product_SingleItem> items = new ArrayList<product_SingleItem>();
    String name;
    String price;
    String imgUrl;
    String key;
    LayoutInflater inf;


    public product_Adapter() {

    }


    public product_Adapter(Context context, String name, String price, String imgUrl, String key) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.key = key;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public product_SingleItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(product_SingleItem singleItem) {
        items.add(singleItem);
    }
    public void resetItem() {
        items = new ArrayList<product_SingleItem>();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        product_SingleViewer singerViewer = new product_SingleViewer(viewGroup.getContext().getApplicationContext());
        singerViewer.setItem(items.get(i));
        Log.d("My_Adapter:getView:i ",String.valueOf(i));
        return singerViewer;
    }
}