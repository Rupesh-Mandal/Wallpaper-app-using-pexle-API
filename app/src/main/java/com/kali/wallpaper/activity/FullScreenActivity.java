package com.kali.wallpaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.kali.wallpaper.R;

import java.io.IOException;

public class FullScreenActivity extends AppCompatActivity {

    String url;
    PhotoView photoView;
    Button setWallpaperBtn;
    String avgColor;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullSceen();
        setContentView(R.layout.activity_full_screen);


        initView();
        Glide.with(this).load(url).into(photoView);
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpapers();
            }
        });

    }

    private void setWallpapers() {


        WallpaperManager wallpaperManager=WallpaperManager.getInstance(this);
        Bitmap bitmap=((BitmapDrawable)photoView.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);

            Toast.makeText(this, "Wallpaper set complete", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        photoView=findViewById(R.id.photo_view);
        setWallpaperBtn=findViewById(R.id.set_wallpaper);
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        avgColor=intent.getStringExtra("avg_color");
//        Toast.makeText(this, avgColor, Toast.LENGTH_SHORT).show();
//        setWallpaperBtn.setCardBackgroundColor(Integer.parseInt(avgColor));

    }
    private void setFullSceen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}