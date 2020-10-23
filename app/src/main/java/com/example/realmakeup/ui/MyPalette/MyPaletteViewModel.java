package com.example.realmakeup.ui.MyPalette;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyPaletteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyPaletteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my Paletee fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}