package com.ossovita.unsplashapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ossovita.unsplashapi.api.UnsplashApi;
import com.ossovita.unsplashapi.api.UnsplashService;
import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    UnsplashApi unsplashApi;
    List<Photo> photoList = new ArrayList<>();
    SearchResult searchResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ
        unsplashApi = UnsplashService.getInstance().create(UnsplashApi.class);
        getData();

    }

    private void getData() {
        unsplashApi.getPhotos("cat","6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ").enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: hata: " + response.code());
                }
                photoList = response.body().getPhotoList();
                Log.d(TAG, "onResponse: cevap: " + response.body().getTotal());
                for(Photo photo : photoList){
                    Log.d(TAG, "onResponse: cevap: " + photo.getDescription());
                }


            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d(TAG, "onFailure: hata: " + t.getMessage());
            }
        });

    }
}