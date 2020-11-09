package com.example.realmakeup;

public class ColorModel {
    public String skinCode = "";
    public String lipCode = "";
    public int personalCode;
    public ColorModel() {

    }
    public ColorModel(String skinCode, String lipCode, int personal_code) {
        this.skinCode = skinCode;
        this.lipCode = lipCode;
        this.personalCode = personal_code;
    }
}