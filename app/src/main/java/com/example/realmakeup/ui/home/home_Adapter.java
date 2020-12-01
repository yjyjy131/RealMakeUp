package com.example.realmakeup.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.realmakeup.ui.ItemList.product_SingleItem;
import com.example.realmakeup.ui.ItemList.product_SingleViewer;

import java.util.ArrayList;

public class home_Adapter extends BaseAdapter {

    ArrayList<home_SingleItem> items = new ArrayList<home_SingleItem>();
    String name;
    String price;
    String imgUrl;
    String key;
    LayoutInflater inf;


    public home_Adapter() {

    }


    public home_Adapter(Context context, String name, String price, String imgUrl, String key) {
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
    public home_SingleItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(home_SingleItem singleItem) {
        items.add(singleItem);
    }
    public void resetItem() {
        items = new ArrayList<home_SingleItem>();
    }
    public void removeItem(int position){
        items.remove(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        home_SingleViewer singerViewer = new home_SingleViewer(viewGroup.getContext().getApplicationContext());
        singerViewer.setItem(items.get(i));
        Log.d("My_Adapter:getView:i ",String.valueOf(i));
        return singerViewer;
    }
}