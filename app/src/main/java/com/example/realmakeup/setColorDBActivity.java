package com.example.realmakeup;

import android.app.Activity;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.realmakeup.ui.ItemList.product_SingleItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class setColorDBActivity extends Activity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_color_db);

        TextView tv = (TextView)findViewById(R.id.text_current);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference itemRef = database.getReference("etude");
                DatabaseReference Ref = database.getReference("LipColor");
                DatabaseReference Ref2 = database.getReference("EyeColor");
                itemRef.child("lips").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String key = ds.getKey();

                            itemRef.child("lips").child(key).child("colorCode").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        String colorStr = ds.getValue().toString();    // get hex
                                        String detail_key = ds.getKey();
                                        tv.setText("상세 키값: "+detail_key);

                                        int r = Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
                                        int g = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
                                        int b = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
                                        int code_idx = lip_color_test(r, g, b);
                                        char personal_code = getcode(code_idx);
                                        Ref.child(String.valueOf(personal_code)).child(key+"\\"+detail_key).setValue(key+"\\"+detail_key);
                                    }
                                }
                                @Override
                                public void onCancelled (@NonNull DatabaseError databaseError){
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError databaseError){
                    }
                });


                itemRef.child("shadows").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String key = ds.getKey();

                            itemRef.child("shadows").child(key).child("colorCode").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        String colorStr = ds.getValue().toString();    // get hex
                                        String detail_key = ds.getKey();
                                        tv.setText("상세 키값: "+detail_key);

                                        int r = Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
                                        int g = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
                                        int b = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
                                        int code_idx = eye_color_test(r, g, b);
                                        char personal_code = getcode(code_idx);
                                        Ref2.child(String.valueOf(personal_code)).child(key+"\\"+detail_key).setValue(key+"\\"+detail_key);
                                    }
                                }
                                @Override
                                public void onCancelled (@NonNull DatabaseError databaseError){
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError databaseError){
                    }
                });
            }
        });
    }

    char getcode(int idx){
        if (idx < 4){ // 봄 라이트
            return '0';
        }
        else if (idx < 8){ // 봄 브라이트
            return '1';
        }
        else if (idx < 12){ // 가을 뮤트
            return '2';
        }
        else if (idx <= 16){ // 가을 딥
            return '3';
        }
        else if (idx <= 20){ // 여름 브라이트
            return '4';
        }
        else if (idx <= 24){ // 여름 뮤트
            return '5';
        }
        else if (idx <= 28){ // 여름 라이트
            return '6';
        }
        else if (idx < 32){ // 겨울 뮤트
            return '7';
        }
        else if (idx <= 36){ // 겨울 딥
            return '8';
        }
        else{
            Log.d("error", "error");
            return 0;
        }
    }

    double cost(int R, int G, int B, int R_, int G_, int B_){
        return Math.sqrt((R-R_)*(R-R_)) + Math.sqrt((G-G_)*(G-G_)) + Math.sqrt((B-B_)*(B-B_));
    }

    int lip_color_test(int R, int G, int B){
        ArrayList<Double> listLip = new ArrayList<>();

        // Lip
        listLip.add(cost(R, G, B, 241, 56, 51));
        listLip.add(cost(R, G, B, 242, 83, 64));
        listLip.add(cost(R, G, B, 255, 134, 117));
        listLip.add(cost(R, G, B, 252, 147, 115));

        // Lip 21~24
        listLip.add(cost(R, G, B, 231, 0, 44));
        listLip.add(cost(R, G, B, 246, 54, 77));
        listLip.add(cost(R, G, B, 254, 87, 105));
        listLip.add(cost(R, G, B, 255, 99, 74));

        // Lip 31~34 - 가을 뮤트
        listLip.add(cost(R, G, B, 176, 90, 103));
        listLip.add(cost(R, G, B, 157, 81, 94));
        listLip.add(cost(R, G, B, 150, 69, 66));
        listLip.add(cost(R, G, B, 141, 40, 56));

        // Lip 41~44 - 가을 딥
        listLip.add(cost(R, G, B, 219, 80, 77));
        listLip.add(cost(R, G, B, 206, 93, 66));
        listLip.add(cost(R, G, B, 152, 41, 51));
        listLip.add(cost(R, G, B, 119, 47, 48));

        // Lip 51~54 - 여름 브라이트
        listLip.add(cost(R, G, B, 252, 0, 139));
        listLip.add(cost(R, G, B, 255, 90, 150));
        listLip.add(cost(R, G, B, 245, 119, 201));
        listLip.add(cost(R, G, B, 255, 72, 178));

        // Lip 61~64 - 여름 뮤트
        listLip.add(cost(R, G, B, 166, 87, 116));
        listLip.add(cost(R, G, B, 162, 90, 101));
        listLip.add(cost(R, G, B, 172, 114, 136));
        listLip.add(cost(R, G, B, 169, 76, 86));

        // Lip 71~74 - 여름 라이트
        listLip.add(cost(R, G, B, 218, 78, 113));
        listLip.add(cost(R, G, B, 225, 84, 137));
        listLip.add(cost(R, G, B, 225, 90, 150));
        listLip.add(cost(R, G, B, 254, 182, 219));

        // Lip 81~84 - 겨울 브라이트
        listLip.add(cost(R, G, B, 204, 72, 145));
        listLip.add(cost(R, G, B, 217, 61, 134));
        listLip.add(cost(R, G, B, 213, 103, 137));
        listLip.add(cost(R, G, B, 223, 103, 151));

        // Lip 91~94 - 겨울 딥
        listLip.add(cost(R, G, B, 190, 76, 125));
        listLip.add(cost(R, G, B, 175, 57, 107));
        listLip.add(cost(R, G, B, 176, 43, 60));
        listLip.add(cost(R, G, B, 141, 40, 56));

        // 오차 최솟값 구하기
        int Lipidx = listLip.indexOf(Collections.min(listLip));

        return Lipidx;
    }

    int eye_color_test(int R, int G, int B){
        ArrayList<Double> listEye = new ArrayList<>();

        // Eye 11~14
        listEye.add(cost(R, G, B, 252, 193, 163));
        listEye.add(cost(R, G, B, 242, 204, 142));
        listEye.add(cost(R, G, B, 227, 144, 100));
        listEye.add(cost(R, G, B, 191, 125, 91));

        // Eye 21~24
        listEye.add(cost(R, G, B, 254, 222, 197));
        listEye.add(cost(R, G, B, 249, 165, 131));
        listEye.add(cost(R, G, B, 244, 105, 101));
        listEye.add(cost(R, G, B, 103, 74, 78));

        // Eye 31~34 - 가을 뮤트
        listEye.add(cost(R, G, B, 234, 186, 174));
        listEye.add(cost(R, G, B, 202, 137, 133));
        listEye.add(cost(R, G, B, 106, 77, 69));
        listEye.add(cost(R, G, B, 90, 66, 66));

        // Eye 41~44 - 가을 딥
        listEye.add(cost(R, G, B, 174, 137, 93));
        listEye.add(cost(R, G, B, 165, 117, 95));
        listEye.add(cost(R, G, B, 134, 100, 90));
        listEye.add(cost(R, G, B, 104, 51, 45));

        // Eye 51~54 - 여름 브라이트
        listEye.add(cost(R, G, B, 232, 172, 157));
        listEye.add(cost(R, G, B, 232, 98, 135));
        listEye.add(cost(R, G, B, 136, 84, 97));
        listEye.add(cost(R, G, B, 202, 62, 107));

        // Eye 61~64 - 여름 뮤트
        listEye.add(cost(R, G, B, 196, 139, 146));
        listEye.add(cost(R, G, B, 198, 159, 152));
        listEye.add(cost(R, G, B, 205, 169, 173));
        listEye.add(cost(R, G, B, 228, 137, 144));

        // Eye 71~74 - 여름 라이트
        listEye.add(cost(R, G, B, 244, 211, 218));
        listEye.add(cost(R, G, B, 232, 164, 185));
        listEye.add(cost(R, G, B, 234, 186, 174));
        listEye.add(cost(R, G, B, 244, 146, 161));

        // Eye 81~84 - 겨울 브라이트
        listEye.add(cost(R, G, B, 96, 71, 77));
        listEye.add(cost(R, G, B, 136, 84, 97));
        listEye.add(cost(R, G, B, 237, 183, 111));
        listEye.add(cost(R, G, B, 135, 73, 88));

        // Eye 91~94 - 겨울 딥
        listEye.add(cost(R, G, B, 163, 99, 55));
        listEye.add(cost(R, G, B, 103, 74, 78));
        listEye.add(cost(R, G, B, 136, 71, 87));
        listEye.add(cost(R, G, B, 64, 37, 56));

        // 오차 최솟값 구하기
        int Eyeidx = listEye.indexOf(Collections.min(listEye));

        return Eyeidx;
    }


}