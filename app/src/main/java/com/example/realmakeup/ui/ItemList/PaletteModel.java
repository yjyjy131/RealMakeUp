package com.example.realmakeup.ui.ItemList;

public class PaletteModel {
    public String product_id = "";
    public String brand = "";
    public String colorCode = "";
    public String colorKey = "";

    public PaletteModel() {

    }

    public PaletteModel(String brand, String product_id, String colorKey, String colorCode) {
        this.brand = brand;
        this.product_id = product_id;
        this.colorKey = colorKey;
        this.colorCode = colorCode;
    }
}
