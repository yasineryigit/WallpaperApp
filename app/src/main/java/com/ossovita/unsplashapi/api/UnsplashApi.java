package com.ossovita.unsplashapi.api;

import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.model.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashApi {

    //https://api.unsplash.com/search/photos?query=dog&client_id=6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ
    //https://api.unsplash.com/search/photos?query=nature&page=1,2,3&per_page=20&client_id=6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ
    @GET("search/photos")
    Call<SearchResult> getPhotos(
            @Query("query") String queryTerm,
            @Query("per_page") int perPage,
            @Query("client_id") String key
            );

}
