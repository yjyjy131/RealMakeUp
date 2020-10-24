package com.example.realmakeup;

public class UserModel {
    public String id = "";
    public String skinCode = "";
    public String lipCode = "";
    public UserModel() {

    }
    public UserModel(String id, String skinCode, String lipCode) {
        this.id = id;
        this.skinCode = skinCode;
        this.lipCode = lipCode;
    }
}