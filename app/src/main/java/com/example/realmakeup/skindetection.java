package com.example.realmakeup;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Ref;
import java.util.StringTokenizer;


public class skindetection extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    static {
        System.loadLibrary("dlib");
        System.loadLibrary("native-lib");
    }
    private static final String TAG = "skin-detection";

    LinearLayout skinlayout;
    LinearLayout liplayout;
    LinearLayout animationlayout;

    ImageView filteringimage;
    ImageView skinimage;
    ImageView lipimage;

    TextView SkinHex;
    TextView LipHex;
    TextView loading;

    Bitmap filtering_bitmap;
    String env;
    Mat filter_image;
    Mat temp_image;
    Mat right_cheek = new Mat();
    Mat left_cheek = new Mat();
    Mat top_lip = new Mat();
    Mat bottom_lip = new Mat();

    BackgroundTask task;

    double[] skinresult = new double[3];
    double[] lipresult = new double[3];

    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d( TAG, "copyFile :: 다음 경로로 파일복사 "+ pathDir);
            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            Log.d(TAG, "copyFile :: 파일 복사 중 예외 발생 "+e.toString() );
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        copyFile("shape_predictor_68_face_landmarks.dat");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skindetection);
        // 환경 상태 받아오기
        Intent intent = getIntent();
        env = intent.getExtras().getString("environment");
        Log.d("intent env :: ",env);

        filteringimage = (ImageView)findViewById(R.id.filteringimage);
        skinimage = (ImageView)findViewById(R.id.skinimage);
        lipimage = (ImageView)findViewById(R.id.lipimage);

        skinlayout = (LinearLayout)findViewById(R.id.skin);
        liplayout = (LinearLayout)findViewById(R.id.lip);

        animationlayout = (LinearLayout)findViewById(R.id.animation);
        loading = (TextView)findViewById(R.id.loading);


        // 피부색 보정 결과 이미지 가져오기
        Bitmap image;
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        filtering_bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        filter_image = Imgcodecs.imdecode(new MatOfByte(byteArray), Imgcodecs.IMREAD_UNCHANGED);
        temp_image = filter_image.clone();
        // filtering된 이미지 가져와서 imageview에 넣기
        filteringimage.setImageBitmap(filtering_bitmap);


        final LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.animationView);


        SkinHex = (TextView)findViewById(R.id.skinHex);
        LipHex = (TextView)findViewById(R.id.lipHex);



        Button extract = (Button)findViewById(R.id.extraction);
        extract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                task = new BackgroundTask();
                task.execute();




                String skin = String.format("#%02X%02X%02X",(int)skinresult[2],(int)skinresult[1],(int)skinresult[0]);
                String lip = String.format("#%02X%02X%02X",(int)lipresult[2],(int)lipresult[1],(int)lipresult[0]);
                // 사용자 정보 등록
                register_user_info(skin, lip);
            }
        });
    }

    //새로운 TASK정의 (AsyncTask)
    // < >안에 들은 자료형은 순서대로 doInBackground, onProgressUpdate, onPostExecute의 매개변수 자료형을 뜻한다.(내가 사용할 매개변수타입을 설정하면된다)
    class BackgroundTask extends AsyncTask<Void , Void , String> {
        //초기화 단계에서 사용한다. 초기화관련 코드를 작성했다.
        protected void onPreExecute() {
            loading.setVisibility(View.INVISIBLE);
        }

        //스레드의 백그라운드 작업 구현
        //여기서 매개변수 Intger ... values란 values란 이름의 Integer배열이라 생각하면된다.
        //배열이라 여러개를 받을 수 도 있다. ex) excute(100, 10, 20, 30); 이런식으로 전달 받으면 된다.
        protected String doInBackground(Void ... values) {
            //isCancelled()=> Task가 취소되었을때 즉 cancel당할때까지 반복
            publishProgress();

            skincolor_extraction();

            SkinHex.setText(String.format("#%02X%02X%02X",(int)skinresult[2],(int)skinresult[1],(int)skinresult[0]));
            LipHex.setText(String.format("#%02X%02X%02X",(int)lipresult[2],(int)lipresult[1],(int)lipresult[0]));

            Bitmap bitmapOutput;
            bitmapOutput = Bitmap.createBitmap(filter_image.cols(), filter_image.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(filter_image, bitmapOutput);
            skinimage.setImageBitmap(bitmapOutput);

            //lip이미지에 넣기
            bitmapOutput = Bitmap.createBitmap(temp_image.cols(), temp_image.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(temp_image, bitmapOutput);
            lipimage.setImageBitmap(bitmapOutput);
            return "finish";
        }
        //UI작업 관련 작업 (백그라운드 실행중 이 메소드를 통해 UI작업을 할 수 있다)
        //publishProgress(value)의 value를 값으로 받는다.values는 배열이라 여러개 받기가능
        protected void onProgressUpdate(Void ... values) {
            loading.setVisibility(View.VISIBLE);
            loading.setText("피부색 추출 중입니다...");
        }


        //이 Task에서(즉 이 스레드에서) 수행되던 작업이 종료되었을 때 호출됨
        protected void onPostExecute(String result) {
            skinlayout.setVisibility(View.VISIBLE);
            liplayout.setVisibility(View.VISIBLE);
        }

    }


    public native void Detect(long faceimage,long right,long left,int num);
    public native double[] avgBGR(long cheek);
    public native void createskin(long output, double result[]);

    public void skincolor_extraction(){
        double[] avg_right = new double[3];
        double[] avg_left = new double[3];
        double[] avg_top = new double[3];
        double[] avg_bottom = new double[3];

        Detect(filter_image.getNativeObjAddr() ,right_cheek.getNativeObjAddr(),left_cheek.getNativeObjAddr(),1);

        Detect(temp_image.getNativeObjAddr(),top_lip.getNativeObjAddr(),bottom_lip.getNativeObjAddr(),0);
        //각 볼의 평균 lab값 구하기
        avg_right = avgBGR(right_cheek.getNativeObjAddr());
        avg_left = avgBGR(left_cheek.getNativeObjAddr());
        //두 볼의 평균 lab값 구하기
        skinresult[0] = (avg_left[0] + avg_right[0]) / 2; //R
        skinresult[1] = (avg_left[1] + avg_right[1]) / 2; //G
        skinresult[2] = (avg_left[2] + avg_right[2]) / 2; //B
        Log.d("native-lib ::: skinresult ","" + skinresult[0]+ " "+skinresult[1]+ " " +skinresult[2]);
        createskin(filter_image.getNativeObjAddr(),skinresult);



        //각 볼의 평균 lab값 구하기
        //avg_top = avgBGR(top_lip.getNativeObjAddr());
        avg_bottom = avgBGR(bottom_lip.getNativeObjAddr());
        //두 볼의 평균 lab값 구하기
        lipresult[0] = avg_bottom[0]; //R
        lipresult[1] = avg_bottom[1]; //G
        lipresult[2] = avg_bottom[2]; //B
        Log.d("native-lib ::: lipresult ","" + lipresult[0]+ " "+lipresult[1]+ " " +lipresult[2]);
        createskin(temp_image.getNativeObjAddr(),lipresult);

    }

// 이 함수 쓰는곳 어디임?
    public void register_user_info(String skinRGB, String lipRGB){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = user.getEmail();
        StringTokenizer stringTokenizer = new StringTokenizer(email, "@");
        String id = stringTokenizer.nextToken(); //@ 분리

        DatabaseReference colorRef = database.getReference("User").child(id).child("skinColor").child(env);
        ColorModel colorinfo = new ColorModel(skinRGB, lipRGB);
        colorRef.setValue(colorinfo);
    }
}
