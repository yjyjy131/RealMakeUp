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

                                        double[] CVS = new double[3];
                                        int r = Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
                                        int g = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
                                        int b = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
                                        CVS = cmyk(r, g, b);
                                        int personal_code = color_test((int)CVS[0], (int)CVS[1], (int)CVS[2]);
                                        Ref.child(Integer.toString(personal_code)).child(key+"\\"+detail_key).setValue(key+"#"+detail_key);
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

                                        double[] CVS = new double[3];
                                        int r = Integer.valueOf( colorStr.substring( 1, 3 ), 16 );
                                        int g = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
                                        int b = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );
                                        CVS = cmyk(r, g, b);
                                        int personal_code = color_test((int)CVS[0], (int)CVS[1], (int)CVS[2]);
                                        Ref2.child(Integer.toString(personal_code)).child(key+"\\"+detail_key).setValue(key+"#"+detail_key);
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


    // 퍼스널 칼라 진단을 위해 rgb -> C, V, S
    public double[] cmyk(int R, int G, int B){
        double c = 1 - R / 255;
        double m = 1 - G / 255;
        double y = 1 - B / 255;
        double min_c = Math.min(c, m);
        double max_c = Math.max(c, m);
        min_c = Math.min(min_c, y);
        max_c = Math.max(max_c, y);
        c = (c-min_c) / (1-min_c);
        double v = max_c;
        double s = (max_c-min_c) / max_c;

        double[] result = new double[3];
        result[0] = c*100;
        result[1] = v*100;
        result[2] = s*100;
        return result;
    }

    int color_test(int C, int V, int S){
        if (C<20){
            // Warm 톤
            if (V < 30){
                // Spring
                if (S < 50){
                    // Spring Light
                    return 0;
                }
                else {
                    // Spring Bright
                    return 1;
                }
            }
            else{
                // Autumn
                if (S < 50){
                    // Autumn mute
                    return 2;
                }
                else {
                    // Autumn deep
                    return 3;
                }
            }
        }
        else{
            // Cool 톤
            if (V < 70){
                // Spring
                if (S < 50){
                    if (V < 30){
                        // Summer Bright
                        return 4;
                    }
                    else{
                        // Summer mute
                        return 5;
                    }
                }
                else {
                    // Summer Light
                    return 6;
                }
            }
            else{
                // Winter
                if (S < 50){
                    // winter mute
                    return 7;
                }
                else {
                    // winter deep
                    return 8;
                }
            }
        }
    }
}