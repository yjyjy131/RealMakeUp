package com.example.realmakeup.ui.PersonalColor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalColorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PersonalColorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is personal color fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}