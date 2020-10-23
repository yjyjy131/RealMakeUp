package com.example.realmakeup.ui.ItemList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.R;
import com.example.realmakeup.ui.MyPalette.MyPaletteFragment;
import com.example.realmakeup.ui.home.HomeFragment;
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

public class ItemListFragment extends Fragment {

    private ItemListViewModel itemListViewModel;

    GridView gridView;
    String brand, item;
    // Spinner
    Spinner brand_cate, item_cate;
    ArrayAdapter<CharSequence> adapter_brand, adapter_item;
    Button search_btn;
    product_Adapter singerAdapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference datebaseReference = firebaseDatabase.getReference();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itemListViewModel =
                ViewModelProviders.of(this).get(ItemListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        gridView = (GridView)root.findViewById(R.id.gridView);
        search_btn = (Button)root.findViewById(R.id.btn);
        brand_cate =(Spinner)root.findViewById(R.id.large_category);
        item_cate =(Spinner)root.findViewById(R.id.small_category);

        adapter_brand = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_brand, android.R.layout.simple_spinner_dropdown_item);
        adapter_item = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_product, android.R.layout.simple_spinner_dropdown_item);
        brand_cate.setAdapter(adapter_brand);
        item_cate.setAdapter(adapter_item);
        //adapter_item.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brand_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brand = adapter_item.getItem(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        item_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapter_item.getItem(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_product_info();
            }
        });

        // 제품 클릭시
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        return root;
    }

    void get_product_info(){
        singerAdapter = new product_Adapter();
        gridView.setAdapter(singerAdapter);

        datebaseReference.child("etude").child("shadows").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();    // name
                    String price = ds.child("price").getValue().toString();   // price
                    String titleImg = ds.child("titleImg").getValue().toString();   // titleImg
                    singerAdapter.addItem(new product_SingleItem(name, price, titleImg));
                    singerAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });
    }
}