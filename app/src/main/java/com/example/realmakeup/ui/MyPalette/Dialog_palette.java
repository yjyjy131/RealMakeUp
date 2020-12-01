package com.example.realmakeup.ui.MyPalette;

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
import com.example.realmakeup.ui.ItemList.PaletteModel;
import com.example.realmakeup.ui.ItemList.product_Adapter;
import com.example.realmakeup.ui.home.home_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Dialog_palette {
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference datebaseReference = firebaseDatabase.getReference();

    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();

    public Dialog_palette(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String brand, final String item, final String product_name, final String product_key, final int product_Num, final home_Adapter pd) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.palette_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button paletteButton = (Button) dlg.findViewById(R.id.paletteButton);
        final Button cameraButton = (Button) dlg.findViewById(R.id.cameraButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);


        // 제품 정보 입력
        final TextView message = (TextView)dlg.findViewById(R.id.itemTextView);
        message.setText("제품명: " +product_name);

        // AR 카메라 버튼 클릭시
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.equals("shadows")) {
                int textureId = 0;
                try{
                    String textureInfo = "eye0n" + product_Num;
                    textureId = context.getResources().getIdentifier(textureInfo, "drawable", context.getPackageName());

                }catch (Exception e){
                    e.printStackTrace(); //오류 출력
                }finally {
                    if(textureId == 0){
                        textureId = context.getResources().getIdentifier("eye", "drawable", context.getPackageName());
                    }
                    Intent intent = new Intent(context, MakeupActivity.class);
                    intent.putExtra("textureid", textureId);
                    context.startActivity(intent);
                }

            } else if (item.equals("lips")) {
                int textureId = 0;
                try{
                    String textureInfo = "lip0n" + product_Num;
                    textureId = context.getResources().getIdentifier(textureInfo, "drawable", context.getPackageName());

                }catch (Exception e){
                    e.printStackTrace(); //오류 출력
                }finally {
                    if(textureId == 0){
                        textureId = context.getResources().getIdentifier("lip", "drawable", context.getPackageName());
                    }
                    Intent intent = new Intent(context, MakeupActivity.class);
                    intent.putExtra("textureid", textureId);
                    context.startActivity(intent);
                }
            }
        }
        });

        // 팔레트 삭제 클릭시
        paletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                if(item.equals("lips")){
                    databaseReference.child(user_id).child("paletteLips").child(product_key).removeValue();
                    Toast.makeText(context, product_name+"팔레트에서 제품을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    // 커스텀 다이얼로그를 종료한다.
                    pd.removeItem(product_Num);
                    pd.notifyDataSetChanged();
                    dlg.dismiss();

                }
                else if (item.equals("shadows")){

                    databaseReference.child(user_id).child("paletteEyes").child(product_key).removeValue();
                    Toast.makeText(context, product_name+"을 팔레트에서 제품을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    // 커스텀 다이얼로그를 종료한다.
                    pd.removeItem(product_Num);
                    pd.notifyDataSetChanged();
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








