package com.example.realmakeup.ui.UserSetting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserSettingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserSettingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user setting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}