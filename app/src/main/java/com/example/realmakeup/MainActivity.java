package com.example.realmakeup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.example.realmakeup.ui.SkinSetting.SkinSettingFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public SharedPreferences prefs;

    private FirebaseAuth mAuth = null;
    private FirebaseUser userAuth = null;
    private DatabaseReference mDatabase;

    // Main Drawer 정보
    TextView loginInfo;
    TextView skinInfo;
    ImageView skinInfoImg;
    Spinner spinner3;
    String env;
    ArrayAdapter<CharSequence> adapter_env;
    Button userDelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefs = getSharedPreferences("Pref", MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_palette, R.id.nav_item, R.id.nav_skin, R.id.nav_color, R.id.nav_user)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        loginInfo = (TextView) headerView.findViewById(R.id.loginInfo);
        skinInfo = (TextView) headerView.findViewById(R.id.mySkinCode);
        skinInfoImg = (ImageView) headerView.findViewById(R.id.mySkinImg);
        spinner3 = (Spinner) headerView.findViewById(R.id.spinner3);
        adapter_env = ArrayAdapter.createFromResource(this, R.array.spinner_env, android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter_env);
        adapter_env.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                checkFirstRun();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds item_product to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*
    //DB의 화장품 색상 rgb값으로 봄/여름/가을/겨울 톤 구분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 메뉴를 클릭했을 때의 이벤트 처리
        switch (item.getItemId()) {
            case R.id.action_db_settings :
                Intent intent = new Intent(MainActivity.this, setColorDBActivity.class);
                startActivity(intent);
                break;
        }
        return true; // 내가 이벤트 처리를 완료함
    }
    */

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 앱 처음 실행시 피부 등록
    public void checkFirstRun() {
        // db 체크 없이 실행
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            moveToSkinSetting();
            prefs.edit().putBoolean("isFirstRun", false).apply();
        } else {
            drawerSkinCode();
        }
    }

    public void moveToSkinSetting(){
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        navController.navigate(R.id.action_nav_home_to_nav_skin);

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_home, true)
                .build();

        Bundle bundle = new Bundle();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_home_to_nav_skin, bundle, navOptions);
    }

    public void drawerSkinCode(){
        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();

        
        String spinner_text = spinner3.getSelectedItem().toString();
        if(spinner_text.equals("어두운 실내")){
            env = "dark_inside";
        }
        else if (spinner_text.equals("실외")){
            env = "outside";
        }
        else{
            env = "bright_inside";
        }


        StringTokenizer strToken = new StringTokenizer(userAuth.getEmail(), "@");
        String user_id = strToken.nextToken();
        loginInfo.setText(user_id);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(user_id).child("skinColor").child(env).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equals("skinCode")) {
                                String mySkinCode = ds.getValue().toString();
                                skinInfo.setText(mySkinCode);
                                skinInfoImg.setColorFilter(Color.parseColor(mySkinCode), PorterDuff.Mode.SRC);
                                Log.d("피부코드 존재", mySkinCode);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );

    }

}
