package com.example.realmakeup.ui.ItemList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.realmakeup.R;
import com.example.realmakeup.ui.ItemList.product_SingleItem;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class product_SingleViewer extends LinearLayout {
    TextView name;
    TextView price;
    String imgUrl;
    Bitmap bitmap;
    ImageView imageView;

    public product_SingleViewer(Context context) {
        super(context);
        init(context);
    }

    public product_SingleViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_product,this,true);
        name = (TextView)findViewById(R.id.name);
        price = (TextView)findViewById(R.id.price);
        imageView = (ImageView)findViewById(R.id.img);
    }

    public void setItem(product_SingleItem singleItem){
        name.setText(singleItem.getName());
        price.setText(singleItem.getPrice() + "원");
        this.imgUrl = singleItem.getImage();
        this.bitmap = downloadUrl();
        imageView.setImageBitmap(bitmap);
    }

    public Bitmap downloadUrl(){
        Thread uThread = new Thread() {
            @Override
            public void run(){
                try{
                    URL url = new URL(imgUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                    conn.connect(); //connect() 호출로 실제 통신
                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 반환
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        uThread.start();
        try{
            uThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            return bitmap;
        }
    }
}
