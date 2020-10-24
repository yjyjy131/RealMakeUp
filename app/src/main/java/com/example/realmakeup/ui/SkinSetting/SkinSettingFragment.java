package com.example.realmakeup.ui.SkinSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;
import com.example.realmakeup.SkinActivity;
import com.example.realmakeup.autoimageprocessing;


public class SkinSettingFragment extends Fragment {

    private SkinSettingViewModel skinSettingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        skinSettingViewModel =
                ViewModelProviders.of(this).get(SkinSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skin_setting, container, false);

        Spinner spinner = (Spinner)root.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), com.example.realmakeup.skindetection.class);
                String select = (String) parent.getItemAtPosition(position);
                if(select == "어두운 실내"){
                    intent.putExtra("environment","dark_inside");
                }
                else if (select == "실외"){
                    intent.putExtra("environment","outside");
                }
                else{
                    intent.putExtra("environment","bright_inside");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Intent intent = new Intent(getActivity().getApplicationContext(), com.example.realmakeup.skindetection.class);
                intent.putExtra("environment","bright_inside");
            }
        });
        Button Button1 = (Button)root.findViewById(R.id.autobutton);
        Button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), autoimageprocessing.class);
                startActivity(intent);
            }
        });
        Button Button2 = (Button)root.findViewById(R.id.filterbutton);
        Button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), SkinActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
