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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.realmakeup.ui.ItemList.product_Adapter;
import com.example.realmakeup.ui.ItemList.product_SingleItem;
import com.example.realmakeup.ui.MyPalette.MyPaletteFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String detail_key;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();
    StringTokenizer stringTokenizer = new StringTokenizer(email, "@");
    String user_id = stringTokenizer.nextToken();

    GridView shadow_grid;
    GridView lip_grid;
    product_Adapter lipAdapter;
    product_Adapter shadowAdapter;
    List<String> shadow_list = new ArrayList<>();
    List<String> lip_list = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference();

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

        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(user_id + " 님!");
            }
        });

        shadow_grid = (GridView)root.findViewById(R.id.home_shadow);
        shadowAdapter = new product_Adapter();

        lip_grid = (GridView)root.findViewById(R.id.home_lip);
        lipAdapter = new product_Adapter();

        // 나의 palette 제품 키값 받아오기
        get_user_code();

        lip_grid.setAdapter(lipAdapter);
        shadow_grid.setAdapter(shadowAdapter);

        // 제품 클릭시
        lip_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textureInfo = "face1";
                int textureId = getActivity().getResources().getIdentifier(textureInfo, "drawable", getActivity().getPackageName());
                Intent intent = new Intent(getActivity(), MakeupActivity.class);
                intent.putExtra("textureid", textureId);
                startActivity(intent);
            }
        });
        shadow_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textureInfo = "face1";
                int textureId = getActivity().getResources().getIdentifier(textureInfo, "drawable", getActivity().getPackageName());
                Intent intent = new Intent(getActivity(), MakeupActivity.class);
                intent.putExtra("textureid", textureId);
                startActivity(intent);
            }
        });

        return root;
    }

    void get_user_code() {
        if (user != null) {
            // user의 퍼스널 칼라 값 가져오기
            ref.child("User").child(user_id).child("skinColor").child("bright_inside").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals("personalCode")) {
                            String code = ds.getValue().toString();
                            get_code_info(code);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "피부색을 등록해주세요!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void get_code_info(String code){
        ref.child("LipColor").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lipAdapter.resetItem(); // 중복 방지
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StringTokenizer stringTokenizer = new StringTokenizer(ds.getKey(), "\\");
                    final String product_key = stringTokenizer.nextToken();
                    final String detail_key = stringTokenizer.nextToken();
                    lip_list.add(product_key);

                    //제품 정보 가져오기
                    ref.child("etude").child("lips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals(product_key)) {
                                    String key = ds.getKey();
                                    String name = ds.child("name").getValue().toString();    // name
                                    String detail_name = ds.child("colorName").child(detail_key).getValue().toString(); // 상세 제품명
                                    String price = ds.child("price").getValue().toString();   // price
                                    String Img = ds.child("titleImg").getValue().toString();   // titleImg
                                    //String Img = ds.child("image").child(detail_key).getValue().toString();   // detailImg
                                    lipAdapter.addItem(new product_SingleItem(name +"\n"+ detail_name, price, Img, key));
                                    lipAdapter.notifyDataSetChanged();
                                }
                                //singerAdapter.addItem(new MyStudy_SingerItem("test","period","time","zp"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("EyeColor").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shadowAdapter.resetItem(); // 중복 방지
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    StringTokenizer stringTokenizer = new StringTokenizer(ds.getKey(), "\\");
                    final String product_key = stringTokenizer.nextToken();
                    final String detail_key = stringTokenizer.nextToken();
                    shadow_list.add(product_key);

                    //제품 정보 가져오기
                    ref.child("etude").child("shadows").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.getKey().equals(product_key)) {
                                    String key = ds.getKey();
                                    String name = ds.child("name").getValue().toString();    // name
                                    String detail_name = ds.child("colorName").child(detail_key).getValue().toString(); // 상세 제품명
                                    String price = ds.child("price").getValue().toString();   // price
                                    String Img = ds.child("titleImg").getValue().toString();   // titleImg
                                    //String Img = ds.child("image").child(detail_key).getValue().toString();   // detailImg
                                    shadowAdapter.addItem(new product_SingleItem(name +"\n"+ detail_name, price, Img, key));
                                    shadowAdapter.notifyDataSetChanged();
                                }
                                //singerAdapter.addItem(new MyStudy_SingerItem("test","period","time","zp"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}