package com.example.realmakeup.ui.home;

public class home_SingleItem {
    private String name;
    private String price;
    private String imgUrl;
    private String imgUrl2;
    private String product_key;
    public home_SingleItem() {

    }

    public home_SingleItem(String name, String price, String imgUrl, String imgUrl2, String product_key) {
        this.name = name;
        this.price = price;
        // 일단 임시로 로고 띄우려고
        this.imgUrl = imgUrl;
        this.imgUrl2 = imgUrl2;
        this.product_key = product_key;
    }

    public String getName() {return this.name;}
    public String getPrice() {return this.price;}
    public String getImage() {
        return imgUrl;
    }
    public String getImage2() {
        return imgUrl2;
    }
    public String getProductKey() {return product_key;}

}
