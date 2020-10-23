package com.example.realmakeup.ui.SkinSetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;
import com.example.realmakeup.ui.MyPalette.MyPaletteViewModel;

public class SkinSettingFragment extends Fragment {

    private SkinSettingViewModel skinSettingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        skinSettingViewModel =
                ViewModelProviders.of(this).get(SkinSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skin_setting, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        skinSettingViewModel.getText().observe(this, new Observer<String>() {
        return root;
    }
}