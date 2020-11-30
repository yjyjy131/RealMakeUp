package com.example.realmakeup.ui.UserSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;



public class UserSettingFragment extends Fragment {

    private UserSettingViewModel userSettingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userSettingViewModel =
                ViewModelProviders.of(this).get(UserSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_setting, container, false);


        return root;
    }
}
