package com.example.realmakeup.ui.ItemList;

public class PaletteModel {
    public String product_id = "";
    public String product_name = "";
    public String brand = "";

    public PaletteModel() {

    }
    public PaletteModel(String brand, String product_name, String product_id) {
        this.brand = brand;
        this.product_name = product_name;
        this.product_id = product_id;
    }
}
