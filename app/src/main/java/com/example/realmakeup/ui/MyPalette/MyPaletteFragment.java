package com.example.realmakeup.ui.MyPalette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;

public class MyPaletteFragment extends Fragment {

    private MyPaletteViewModel myPaletteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPaletteViewModel =
                ViewModelProviders.of(this).get(MyPaletteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_palette, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        myPaletteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}