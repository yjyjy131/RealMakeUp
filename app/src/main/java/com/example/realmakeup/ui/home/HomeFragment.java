package com.example.realmakeup.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.MainActivity;
import com.example.realmakeup.MakeupActivity;
import com.example.realmakeup.R;
//import com.example.realmakeup.ui.ItemList.ItemListFragment;
//import com.example.realmakeup.ui.MyPalette.MyPaletteFragment;
import com.example.realmakeup.ui.MyPalette.MyPaletteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button list_btn;
    Button palette_btn;
    Button arCamera_btn;
    String email = user.getEmail();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

//        Context context = getActivity();
//        prefs = context.getSharedPreferences("Pref", MODE_PRIVATE);
//        checkFirstRun();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        list_btn = root.findViewById(R.id.item_list_btn);
        palette_btn = root.findViewById(R.id.my_palette_btn);
        arCamera_btn = root.findViewById(R.id.arCamera_btn);

        list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            }
        });

        palette_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        arCamera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((getActivity()), MakeupActivity.class);
                startActivity(intent);
            }
        });

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(email + " 님!");
            }
        });
        return root;
    }

}