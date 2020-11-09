package com.example.realmakeup.ui.ItemList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realmakeup.MakeupActivity;
import com.example.realmakeup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Dialog_Item {
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference datebaseReference = firebaseDatabase.getReference();

    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();

    public Dialog_Item(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String brand, final String item, final String product_name, final String product_key, final int product_Num) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.item_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button paletteButton = (Button) dlg.findViewById(R.id.paletteButton);
        final Button cameraButton = (Button) dlg.findViewById(R.id.cameraButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);
        final Spinner detail_prod_spinner = (Spinner) dlg.findViewById(R.id.detail_prod);

        // 스피너 정보를 가져온다.
        ArrayList<String> colorRGBList = new ArrayList<String>();
        ArrayList<String> colorNameList = new ArrayList<String>();
        ArrayList<String> colorKeyList = new ArrayList<String>();

        ArrayAdapter<String> adapter_detail = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, colorNameList);
        adapter_detail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        detail_prod_spinner.setAdapter(adapter_detail);

        datebaseReference.child(brand).child(item).child(product_key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("colorCode").getChildren()) {
                    String prod_color = ds.getValue().toString();    // RGB 색상값
                    colorRGBList.add(prod_color);
                }
                for (DataSnapshot ds : dataSnapshot.child("colorName").getChildren()) {
                    String detail_key = ds.getKey();
                    String prod_name = ds.getValue().toString();    // 세부 제품명
                    colorNameList.add(prod_name);
                    colorKeyList.add(detail_key);
                }
                adapter_detail.notifyDataSetChanged();
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });

        //adapter_detail.notifyDataSetChanged();

        detail_prod_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // 제품 정보 입력
        final TextView message = (TextView)dlg.findViewById(R.id.itemTextView);
        message.setText("제품명: " +product_name);

        // AR 카메라 버튼 클릭시
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 스피너 아이템 셀렉 오류
                int num = (int) detail_prod_spinner.getSelectedItemId();
                // String detail_RGB = colorRGBList.get(num);  // RGB 색상 값
                Log.d("Spinner Color Select :  ", String.valueOf(num));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                if (item.equals("shadows")) {
                    String textureInfo = "eye" +  product_Num + "n" + num;
                    int textureId = context.getResources().getIdentifier(textureInfo, "drawable", context.getPackageName());
                    Intent intent = new Intent(context, MakeupActivity.class);
                    intent.putExtra("textureid", textureId);
                    context.startActivity(intent);
                } else if (item.equals("lips")) {
                    String textureInfo = "lip" +  product_Num + "n" + num;
                    int textureId = context.getResources().getIdentifier(textureInfo, "drawable", context.getPackageName());
                    Intent intent = new Intent(context, MakeupActivity.class);
                    intent.putExtra("textureid", textureId);
                    context.startActivity(intent);
                }
            }
        });

        // 팔레트 추가 클릭시
        paletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                int num = (int) detail_prod_spinner.getSelectedItemId();
                String detail_RGB = colorRGBList.get(num);
                String detail_key = colorKeyList.get(num);
                String detail_name = colorNameList.get(num);

                PaletteModel paletteModel = new PaletteModel(brand, product_key, detail_key, detail_RGB);
                if (item.equals("shadows")) {
                    databaseReference.child(user_id).child("paletteEyes").child(product_key+"\\"+detail_key).setValue(paletteModel);
                    Toast.makeText(context, "\"" + product_name+ " - " + detail_name+ "\" 을 나의 팔레트에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                } else if (item.equals("lips")) {
                    databaseReference.child(user_id).child("paletteLips").child(product_key+"\\"+detail_key).setValue(paletteModel);
                    Toast.makeText(context, "\"" + product_name+ " - " + detail_name+ "\" 을 나의 팔레트에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}








