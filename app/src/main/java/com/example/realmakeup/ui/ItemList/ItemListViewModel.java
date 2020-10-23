package com.example.realmakeup.ui.ItemList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ItemListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is item List fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}