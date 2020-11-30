package com.example.realmakeup.ui.SkinSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.realmakeup.skindetection;


public class SkinSettingFragment extends Fragment {

    String env;
    Spinner env_spinner;
    ArrayAdapter<CharSequence> adapter_env;

    private SkinSettingViewModel skinSettingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        skinSettingViewModel =
                ViewModelProviders.of(this).get(SkinSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_skin_setting, container, false);
        env_spinner = (Spinner)root.findViewById(R.id.spinner);
        adapter_env = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_env, android.R.layout.simple_spinner_dropdown_item);
        env_spinner.setAdapter(adapter_env);
        adapter_env.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        env_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Button Button1 = (Button)root.findViewById(R.id.autobutton);

        Button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                get_env();
                Intent intent = new Intent(getActivity(), autoimageprocessing.class);
                intent.putExtra("environment",env);
                startActivity(intent);
            }
        });
        Button Button2 = (Button)root.findViewById(R.id.filterbutton);
        Button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                get_env();
                Intent intent = new Intent(getActivity(), SkinActivity.class);
                intent.putExtra("environment",env);
                startActivity(intent);
            }
        });
        return root;
    }

    void get_env(){
        String spinner_text = env_spinner.getSelectedItem().toString();
        if(spinner_text.equals("어두운 실내")){
            env = "dark_inside";
        }
        else if (spinner_text.equals("실외")){
            env = "outside";
        }
        else{
            env = "bright_inside";
        }
    }
}
