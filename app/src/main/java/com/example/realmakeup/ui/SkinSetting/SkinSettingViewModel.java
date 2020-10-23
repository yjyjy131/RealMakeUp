package com.example.realmakeup.ui.SkinSetting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SkinSettingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SkinSettingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is skin setting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}