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
import androidx.lifecycle.ViewModelProviders;
import com.example.realmakeup.R;

public class SkinSettingFragment extends Fragment {

    private SkinSettingViewModel SkinSettingViewModel;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static SkinSettingFragment newInstance() {
        return new SkinSettingFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SkinSettingViewModel =
                ViewModelProviders.of(this).get(SkinSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skin_setting, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        SkinSettingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}