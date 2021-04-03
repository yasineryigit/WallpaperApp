package com.ossovita.unsplashapi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ossovita.unsplashapi.R;
import com.ossovita.unsplashapi.adapter.PhotoAdapter;
import com.ossovita.unsplashapi.api.UnsplashApi;
import com.ossovita.unsplashapi.api.UnsplashService;
import com.ossovita.unsplashapi.db.CustomSharedPreferences;
import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.model.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    UnsplashApi unsplashApi;
    List<Photo> photoList = new ArrayList<>();
    RecyclerView recyclerView;
    PhotoAdapter adapter;
    private CustomSharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = CustomSharedPreferences.getInstance(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ
        unsplashApi = UnsplashService.getInstance().create(UnsplashApi.class);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(preferences.getSearchTerm());
    }

    private void getData(String searchKey) {
        unsplashApi.getPhotos(searchKey,100,"6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ").enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                photoList.clear();
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: hata: " + response.code());
                }
                photoList = response.body().getPhotoList();
                Log.d(TAG, "onResponse: cevap: " + response.body().getTotal());
                for(Photo photo : photoList){
                    Log.d(TAG, "onResponse: cevap: " + photo.getUrls().getFull());
                }
                sortByLikes(photoList);
                adapter = new PhotoAdapter(photoList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d(TAG, "onFailure: hata: " + t.getMessage());
            }
        });
    }

    private void sortByLikes(List<Photo> photoList){
        Collections.sort(photoList, new Comparator<Photo>() {
            @Override
            public int compare(Photo t1, Photo t2) {
                return t2.getLikes().compareTo(t1.getLikes());
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.search_menu){
            finish();
            startActivity(new Intent(this,SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}