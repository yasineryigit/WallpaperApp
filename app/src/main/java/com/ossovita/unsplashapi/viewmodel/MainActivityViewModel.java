package com.ossovita.unsplashapi.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ossovita.unsplashapi.api.UnsplashApi;
import com.ossovita.unsplashapi.api.UnsplashService;
import com.ossovita.unsplashapi.db.CustomSharedPreferences;
import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.model.SearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@HiltViewModel
public class MainActivityViewModel extends ViewModel {

    private static final String TAG = "MainActivityViewModel";
    private MutableLiveData<List<Photo>> photoList;
    private final MutableLiveData<String> searchKey = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    Retrofit retrofit;
    CustomSharedPreferences preferences;
    Context context;
    UnsplashApi unsplashApi;

    @Inject
    public MainActivityViewModel(@ApplicationContext Context context, Retrofit retrofit, CustomSharedPreferences preferences) {
        this.context=context;
        this.retrofit = retrofit;
        this.preferences=preferences;

    }

    public MutableLiveData<List<Photo>> getPhotoList() {
        if (photoList == null) {
            photoList = new MutableLiveData<>();
            loadPhotos(preferences.getSearchTerm());
        }
        return photoList;
    }


    private void loadPhotos(String searchKey) {
        unsplashApi = retrofit.create(UnsplashApi.class);
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

    public MutableLiveData<String> getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String key) {
        preferences.setSearchTerm(key);
        searchKey.setValue(key);
        loadPhotos(key);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(MutableLiveData<Boolean> isLoading) {
        this.isLoading = isLoading;
    }
}
