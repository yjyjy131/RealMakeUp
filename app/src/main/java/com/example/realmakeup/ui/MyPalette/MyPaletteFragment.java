package com.example.realmakeup.ui.MyPalette;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.MakeupActivity;
import com.example.realmakeup.R;
import com.example.realmakeup.ui.ItemList.Dialog_Item;
import com.example.realmakeup.ui.ItemList.product_Adapter;
import com.example.realmakeup.ui.ItemList.product_SingleItem;
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

public class MyPaletteFragment extends Fragment {

    private MyPaletteViewModel myPaletteViewModel;
    GridView shadow_grid;
    GridView lip_grid;
    product_Adapter lipAdapter;
    product_Adapter shadowAdapter;

    List<String> shadow_list = new ArrayList<>();
    List<String> lip_list = new ArrayList<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();
    String detail_key;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPaletteViewModel =
                ViewModelProviders.of(this).get(MyPaletteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_palette, container, false);

        shadow_grid = (GridView)root.findViewById(R.id.palette_shadow);
        shadowAdapter = new product_Adapter();

        lip_grid = (GridView)root.findViewById(R.id.palette_lip);
        lipAdapter = new product_Adapter();

        // 나의 palette 제품 키값 받아오기
        get_palette_eye();
        get_palette_lip();

        lip_grid.setAdapter(lipAdapter);
        shadow_grid.setAdapter(shadowAdapter);

        // 제품 클릭시
        lip_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textureInfo = "lip0n" + detail_key;
                Log.d("texture",textureInfo);
                int textureId = getActivity().getResources().getIdentifier(textureInfo, "drawable", getActivity().getPackageName());
                Intent intent = new Intent(getActivity(), MakeupActivity.class);
                intent.putExtra("textureid", textureId);
                startActivity(intent);
            }
        });
        shadow_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textureInfo = "eye0n" + detail_key;
                int textureId = getActivity().getResources().getIdentifier(textureInfo, "drawable", getActivity().getPackageName());
                Intent intent = new Intent(getActivity(), MakeupActivity.class);
                intent.putExtra("textureid", textureId);
                startActivity(intent);
            }
        });

        return root;

    }
    void get_palette_lip(){
        if (userAuth != null) {
            ref.child("User").child(user_id).child("paletteLips").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lipAdapter.resetItem(); // 중복 방지
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        StringTokenizer stringTokenizer = new StringTokenizer(ds.getKey(), "\\");
                        final String product_key = stringTokenizer.nextToken();
                        String brand = ds.child("brand").getValue().toString();
                        final String detail_key = ds.child("colorKey").getValue().toString();
                        lip_list.add(product_key);

                        //제품 정보 가져오기
                        ref.child(brand).child("lips").addListenerForSingleValueEvent(new ValueEventListener() {
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
        }
    }

    void get_palette_eye() {
        if (userAuth != null) {
            // 내가 팔레트에 담은 섀도우 목록 탐색
            ref.child("User").child(user_id).child("paletteEyes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    shadowAdapter.resetItem();
                    //lipAdapter.resetItem(); // 중복 방지
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        StringTokenizer stringTokenizer = new StringTokenizer(ds.getKey(), "\\");
                        final String product_key = stringTokenizer.nextToken();
                        detail_key = ds.child("colorKey").getValue().toString();
                        String brand = ds.child("brand").getValue().toString();
                        Log.d("brand: ", brand);
                        shadow_list.add(product_key);

                        //제품 정보 가져오기
                        ref.child(brand).child("shadows").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getKey().equals(product_key)) {
                                        String key = ds.getKey();
                                        String name = ds.child("name").getValue().toString();    // name
                                        String detail_name = ds.child("colorName").child(detail_key).getValue().toString(); // 상세 제품명
                                        String price = ds.child("price").getValue().toString();   // price
                                        String titleImg = ds.child("titleImg").getValue().toString();   // titleImg
                                        shadowAdapter.addItem(new product_SingleItem(name +"\n"+ detail_name, price, titleImg, key));
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
}