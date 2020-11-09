package com.example.realmakeup.ui.PersonalColor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;
import com.example.realmakeup.SkinActivity;
import com.example.realmakeup.autoimageprocessing;
import com.example.realmakeup.ui.ItemList.product_SingleItem;
import com.example.realmakeup.ui.SkinSetting.SkinSettingViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.graphics.Color;

import java.util.StringTokenizer;

import static android.graphics.Color.rgb;


public class PersonalColorFragment extends Fragment {

    String env;
    Spinner env_spinner;
    TextView color_cate, color_code;
    ArrayAdapter<CharSequence> adapter_env;
    Button Button1;
    FrameLayout colorimage1, colorimage2, colorimage3, colorimage4;
    FrameLayout colorimage5, colorimage6, colorimage7, colorimage8;
    LinearLayout colorlayout;
    int personal_code;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference();


    private PersonalColorViewModel personalColorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalColorViewModel =
                ViewModelProviders.of(this).get(PersonalColorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_personal_color, container, false);

        colorlayout = (LinearLayout)root.findViewById(R.id.color);
        colorimage1 = (FrameLayout)root.findViewById(R.id.colorimage1);
        colorimage2 = (FrameLayout)root.findViewById(R.id.colorimage2);
        colorimage3 = (FrameLayout)root.findViewById(R.id.colorimage3);
        colorimage4 = (FrameLayout)root.findViewById(R.id.colorimage4);

        colorimage5 = (FrameLayout)root.findViewById(R.id.colorimage5);
        colorimage6 = (FrameLayout)root.findViewById(R.id.colorimage6);
        colorimage7 = (FrameLayout)root.findViewById(R.id.colorimage7);
        colorimage8 = (FrameLayout)root.findViewById(R.id.colorimage8);
        color_cate = (TextView)root.findViewById(R.id.color_cate);
        color_code = (TextView)root.findViewById(R.id.colorHex);

        env_spinner = (Spinner)root.findViewById(R.id.spinner);
        Button1 = (Button)root.findViewById(R.id.color_button);
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

        Button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                get_env();
                get_palette_lip();
                colorlayout.setVisibility(View.VISIBLE);
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

    void get_palette_lip(){
        if (userAuth != null) {
            ref.child("User").child(user_id).child("skinColor").child(env).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals("personalCode")){
                            String code = ds.getValue().toString();
                            Log.d("l code",code);
                            set_screen(code);
                        }
                        if (ds.getKey().equals("skinCode")){
                            String hex = ds.getValue().toString();
                            color_code.setText("( 나의 피부색: " + hex + " )");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    void set_color(int R, int G, int B, FrameLayout frame){
        int color = Color.rgb(R, G, B);
        frame.setBackgroundColor(color);
    }

    void set_screen(String personal_code){
        switch (Integer.parseInt(personal_code)){
            case 0:
                color_cate.setText("\'봄 라이트\' 입니다.");
                set_color(241, 56, 51, colorimage1);
                set_color(242, 83, 64, colorimage2);
                set_color(255, 134, 117, colorimage3);
                set_color(252, 147, 115, colorimage4);

                set_color(252, 193, 163, colorimage5);
                set_color(242, 204, 142, colorimage6);
                set_color(227, 144, 100, colorimage7);
                set_color(191, 125, 91, colorimage8);
                break;
            case 1:
                color_cate.setText("\'봄 브라이트\' 입니다.");
                set_color(231, 0, 44, colorimage1);
                set_color(246, 54, 77, colorimage2);
                set_color(254, 87, 105, colorimage3);
                set_color(255, 99, 74, colorimage4);

                set_color(254, 222, 197, colorimage5);
                set_color(249, 165, 131, colorimage6);
                set_color(244, 105, 101, colorimage7);
                set_color(103, 74, 78, colorimage8);
                break;
            case 2:
                color_cate.setText("\'가을 뮤트\' 입니다.");
                set_color(176, 90, 103, colorimage1);
                set_color(157, 81, 94, colorimage2);
                set_color(150, 69, 66, colorimage3);
                set_color(141, 40, 56, colorimage4);

                set_color(234, 186, 174, colorimage5);
                set_color(202, 137, 133, colorimage6);
                set_color(106, 77, 69, colorimage7);
                set_color(90, 66, 66, colorimage8);
                break;
            case 3:
                color_cate.setText("\'가을 딥\' 입니다.");
                set_color(219, 80, 77, colorimage1);
                set_color(206, 93, 66, colorimage2);
                set_color(152, 41, 51, colorimage3);
                set_color(119, 47, 48, colorimage4);

                set_color(174, 137, 93, colorimage5);
                set_color(165, 117, 95, colorimage6);
                set_color(134, 100, 90, colorimage7);
                set_color(104, 51, 45, colorimage8);
                break;
            case 4:
                color_cate.setText("\'여름 브라이트\' 입니다.");
                set_color(252, 0, 139, colorimage1);
                set_color(255, 90, 150, colorimage2);
                set_color(245, 119, 201, colorimage3);
                set_color(255, 72, 178, colorimage4);

                set_color(232, 172, 157, colorimage5);
                set_color(232, 98, 135, colorimage6);
                set_color(136, 84, 97, colorimage7);
                set_color(202, 62, 107, colorimage8);
                break;
            case 5:
                color_cate.setText("\'여름 뮤트\' 입니다.");
                set_color(166, 87, 116, colorimage1);
                set_color(162, 90, 101, colorimage2);
                set_color(172, 114, 136, colorimage3);
                set_color(169, 76, 86, colorimage4);

                set_color(196, 139, 146, colorimage5);
                set_color(198, 159, 152, colorimage6);
                set_color(205, 169, 173, colorimage7);
                set_color(228, 137, 144, colorimage8);
                break;
            case 6:
                color_cate.setText("\'여름 라이트\' 입니다.");
                set_color(218, 78, 113, colorimage1);
                set_color(225, 84, 137, colorimage2);
                set_color(225, 90, 150, colorimage3);
                set_color(254, 182, 219, colorimage4);

                set_color(244, 211, 218, colorimage5);
                set_color(232, 164, 185, colorimage6);
                set_color(234, 186, 174, colorimage7);
                set_color(144, 146, 161, colorimage8);
                break;
            case 7:
                color_cate.setText("\'겨울 브라이트\' 입니다.");
                set_color(204, 72, 145, colorimage1);
                set_color(217, 61, 134, colorimage2);
                set_color(213, 103, 137, colorimage3);
                set_color(223, 103, 151, colorimage4);

                set_color(96, 71, 77, colorimage5);
                set_color(136, 84, 97, colorimage6);
                set_color(237, 183, 111, colorimage7);
                set_color(135, 73, 88, colorimage8);
                break;
            case 8:
                color_cate.setText("\'겨울 딥\' 입니다.");
                set_color(190, 76, 125, colorimage1);
                set_color(175, 57, 107, colorimage2);
                set_color(176, 43, 60, colorimage3);
                set_color(141, 40, 56, colorimage4);

                set_color(163, 99, 55, colorimage5);
                set_color(103, 74, 78, colorimage6);
                set_color(136, 71, 87, colorimage7);
                set_color(64, 37, 56, colorimage8);
                break;
            default:
                color_cate.setText("잘못된 피부색입니다.");
                break;

        }
    }
}
