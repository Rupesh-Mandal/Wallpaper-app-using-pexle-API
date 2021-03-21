package com.kali.wallpaper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kali.wallpaper.R;
import com.kali.wallpaper.adapter.RecyclerViewAdapter;
import com.kali.wallpaper.model.WallpaperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    List<WallpaperModel> wallpaperModel;
    int pageNumber = 1;
    Boolean isScrolling=false;
    int currentItem,totalItem,scrollOutItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }


    private void initView() {
        recyclerView=findViewById(R.id.recycler);
        wallpaperModel =new ArrayList<>();
        recyclerViewAdapter=new RecyclerViewAdapter(this,wallpaperModel,MainActivity.this);
        recyclerView.setAdapter(recyclerViewAdapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem=gridLayoutManager.getChildCount();
                totalItem=gridLayoutManager.getItemCount();
                scrollOutItem=gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (isScrolling&&(currentItem+scrollOutItem==totalItem)){
                    fetchWallpaper();
                }
            }
        });
        fetchWallpaper();
    }

    private void fetchWallpaper() {
        StringRequest request=new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=80",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray photoArry=jsonObject.getJSONArray("photos");
                            int length=photoArry.length();
                            for (int i=0;i<length;i++){
                                JSONObject photoObject=photoArry.getJSONObject(i);
                                int id=photoObject.getInt("id");
                                int width=photoObject.getInt("width");
                                int height=photoObject.getInt("height");
                                String url=photoObject.getString("url");
                                String photographer=photoObject.getString("photographer");
                                String avg_color=photoObject.getString("avg_color");

                                JSONObject photoQuilityObject=photoObject.getJSONObject("src");
                                String medium=photoQuilityObject.getString("medium");
                                String small=photoQuilityObject.getString("small");
                                String portrait=photoQuilityObject.getString("portrait");
                                String original=photoQuilityObject.getString("original");
                                String tiny=photoQuilityObject.getString("tiny");

                                WallpaperModel wallpaperModel2=new WallpaperModel(id,width,height,url,photographer,avg_color,
                                        medium,small,portrait,original,tiny);

                                wallpaperModel.add(wallpaperModel2);


                            }

                            recyclerViewAdapter.notifyDataSetChanged();
                            pageNumber++;


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001adc43ff7d7a840089c6f7c215dc4c195");

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }


}