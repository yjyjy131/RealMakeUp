package com.example.realmakeup.ui.ItemList;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.realmakeup.R;
import com.google.android.gms.tasks.OnSuccessListener;
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
    StringTokenizer stringTokenizer = new StringTokenizer(userAuth.getEmail(), "@");
    String user_id = stringTokenizer.nextToken();

    public Dialog_Item(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String brand, final String item, final String product_name, final String product_key) {

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


        // 제품 정보 입력
        final TextView message = (TextView)dlg.findViewById(R.id.itemTextView);
        message.setText("제품명: " +product_name);
        Log.d("브랜드, 종류, 이름, 키값 순", brand+" "+item+" "+product_name+" "+product_key);
        paletteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팔레트 추가 클릭시
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                PaletteModel paletteModel = new PaletteModel(brand, product_name, product_key);
                if (item.equals("shadows")) {
                    databaseReference.child(user_id).child("paletteEyes").child(product_key).setValue(paletteModel);
                    Toast.makeText(context, "\"" + product_name+ "\" 을 나의 팔레트에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                } else if (item.equals("lips")) {
                    databaseReference.child(user_id).child("paletteLips").child(product_key).setValue(paletteModel);
                    Toast.makeText(context, "\"" + product_name+ "\" 을 나의 팔레트에 추가하였습니다.", Toast.LENGTH_SHORT).show();
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
