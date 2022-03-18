package com.ossovita.unsplashapi.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ossovita.unsplashapi.api.UnsplashApi;
import com.ossovita.unsplashapi.api.UnsplashService;
import com.ossovita.unsplashapi.db.CustomSharedPreferences;
import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.model.SearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = "MainActivityViewModel";
    private MutableLiveData<List<Photo>> photoList;
    private MutableLiveData<String> searchKey = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private CustomSharedPreferences preferences;
    UnsplashApi unsplashApi;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        preferences = CustomSharedPreferences.getInstance(application.getApplicationContext());
        unsplashApi = UnsplashService.getInstance().create(UnsplashApi.class);
    }

    public MutableLiveData<List<Photo>> getPhotoList() {
        if (photoList == null) {
            photoList = new MutableLiveData<>();
            loadPhotos(preferences.getSearchTerm());
        }
        return photoList;
    }

    public MutableLiveData<String> getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String key) {
        preferences.setSearchTerm(key);
        searchKey.setValue(key);
        loadPhotos(key);
    }

    private void loadPhotos(String searchKey) {
        isLoading.setValue(true);
        unsplashApi.getPhotos(searchKey, 100, "6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ").enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: hata: " + response.code());
                }
                List<Photo> photos = response.body().getPhotoList();
                sortByLikes(photos);
                photoList.setValue(response.body().getPhotoList());
                isLoading.setValue(false);
                Log.d(TAG, "onResponse: cevap: " + response.body().getTotal());

            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d(TAG, "onFailure: hata: " + t.getMessage());
            }
        });
    }

    private void sortByLikes(List<Photo> photoList) {
        Collections.sort(photoList, new Comparator<Photo>() {
            @Override
            public int compare(Photo t1, Photo t2) {
                return t2.getLikes().compareTo(t1.getLikes());
            }
        });
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(MutableLiveData<Boolean> isLoading) {
        this.isLoading = isLoading;
    }
}
