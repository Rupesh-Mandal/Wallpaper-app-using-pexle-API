package com.kali.wallpaper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kali.wallpaper.R;
import com.kali.wallpaper.activity.FullScreenActivity;
import com.kali.wallpaper.model.WallpaperModel;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private Context context;
    private List<WallpaperModel> wallpaperList;
    private static String height;
    private static String width;
    Activity activity;

    public RecyclerViewAdapter(Context context, List<WallpaperModel> wallpaperList,Activity activity) {
        this.context = context;
        this.wallpaperList = wallpaperList;
        this.activity=activity;
        gettingDisplaySize();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        WallpaperModel wallpaperModel=wallpaperList.get(position);
        String title=wallpaperModel.getUrl().replace("https://www.pexels.com/photo/","");
        String fTitle=title.replace(String.valueOf(wallpaperModel.getId()),"");
        String fullFinletitle=fTitle.replace("/","");
        holder.imageTitle.setText(fullFinletitle.replace("-"," "));
        holder.photographerName.setText(wallpaperModel.getPhotographer());
        String url=wallpaperModel.getPortrait().replace("1200&w=800",height+"&w="+width);
        Glide.with(context).load(url).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, FullScreenActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("avg_color",wallpaperModel.getAvg_color());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }
    private void gettingDisplaySize() {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height= String.valueOf(metrics.heightPixels);
        width= String.valueOf(metrics.widthPixels);
       // Toast.makeText(context, height+" "+width, Toast.LENGTH_SHORT).show();
    }

}


class RecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView imageTitle,photographerName;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.imageView);
        imageTitle=itemView.findViewById(R.id.image_title);
        photographerName=itemView.findViewById(R.id.photographer_name);
    }

}
