package com.example.realmakeup.ui.UserSetting;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.realmakeup.LoginActivity;
import com.example.realmakeup.MainActivity;
import com.example.realmakeup.R;
import com.example.realmakeup.ui.ItemList.Dialog_Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.StringTokenizer;

import static android.content.Context.ACTIVITY_SERVICE;


public class UserSettingFragment extends Fragment {

    private UserSettingViewModel userSettingViewModel;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser userAuth = null;
    TextView loginInfo;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference datebaseReference = firebaseDatabase.getReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userSettingViewModel =
                ViewModelProviders.of(this).get(UserSettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user_setting, container, false);

        loginInfo = (TextView) root.findViewById(R.id.textView7);
        drawerSkinCode();

        Button userLogout = (Button) root.findViewById(R.id.logOutBtn);
        userLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                // 캐시 삭제
                clearApplicationData(getContext());
                getActivity().finishAffinity();

//                Intent i = new Intent(getActivity(), LoginActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);

            }
        });


        Button userDelte = (Button) root.findViewById(R.id.userDelteBtn);
        userDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAuth.getCurrentUser().delete();
//                clearApplicationData(getContext());
//                getActivity().finish();

                //dialog.show(getActivity().getSupportFragmentManager(),"tag");
                if(getActivity() != null){
                    DeleteDialog dialog = new DeleteDialog();
                    dialog.show(getFragmentManager(), "Dialog");
                }
            }
        });

        return root;
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                deleteDir(new File(appDir, s));
                Log.d("test", "File /data/data/"+context.getPackageName()+"/" + s + " DELETED");
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public void drawerSkinCode(){
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();

        StringTokenizer strToken = new StringTokenizer(userAuth.getEmail(), "@");
        String user_id = strToken.nextToken();
        loginInfo.setText(user_id);
    }
}
