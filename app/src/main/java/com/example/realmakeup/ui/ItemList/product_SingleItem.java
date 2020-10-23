package com.example.realmakeup.ui.ItemList;

import android.widget.ImageView;

public class product_SingleItem {
    private String name;
    private String price;
    private String imgUrl;
    public product_SingleItem() {

    }

    public product_SingleItem(String name, String price, String imgUrl) {
        this.name = name;
        this.price = price;
        // 일단 임시로 로고 띄우려고
        this.imgUrl = imgUrl;
    }

    public String getName() {return this.name;}
    public String getPrice() {return this.price;}
    public String getImage() {
        return imgUrl;
    }

}
