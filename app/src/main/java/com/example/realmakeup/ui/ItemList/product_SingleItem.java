package com.example.realmakeup.ui.ItemList;

import android.widget.ImageView;

public class product_SingleItem {
    private String name;
    private String price;
    private String imgUrl;
    private String product_key;
    public product_SingleItem() {

    }

    public product_SingleItem(String name, String price, String imgUrl, String product_key) {
        this.name = name;
        this.price = price;
        // 일단 임시로 로고 띄우려고
        this.imgUrl = imgUrl;
        this.product_key = product_key;
    }

    public String getName() {return this.name;}
    public String getPrice() {return this.price;}
    public String getImage() {
        return imgUrl;
    }
    public String getProductKey() {return product_key;}

}
