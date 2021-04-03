package com.ossovita.unsplashapi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.ossovita.unsplashapi.R;
import com.ossovita.unsplashapi.api.UnsplashApi;
import com.ossovita.unsplashapi.api.UnsplashService;
import com.ossovita.unsplashapi.db.CustomSharedPreferences;
import com.ossovita.unsplashapi.model.SearchResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    EditText editTextSearch;
    Snackbar snackbar;
    private CustomSharedPreferences preferences;
    ConstraintLayout constraintLayout;
    UnsplashApi unsplashApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextSearch = findViewById(R.id.edit_text_search);
        constraintLayout  = findViewById(R.id.constraintLayout);
        preferences = CustomSharedPreferences.getInstance(getApplicationContext());
        unsplashApi = UnsplashService.getInstance().create(UnsplashApi.class);

    }

    public void search(View v){
        if(editTextSearch.getText().toString().equals("")){
            Snackbar.make(constraintLayout,"Please type something..",Snackbar.LENGTH_SHORT).show();
        }else{
            String searchTerm = editTextSearch.getText().toString();
            preferences.setSearchTerm(searchTerm);

            unsplashApi.getPhotos(searchTerm,100,"6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ").enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    if(!response.isSuccessful()){
                        Log.d(TAG, "onResponse: hata: " + response.code());
                    }

                    if(response.body()!=null&&response.body().getTotal()!=0){//bu arama terimine göre gelen sonuç varsa
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else{
                        Snackbar.make(constraintLayout,"Please enter a valid search term",Snackbar.LENGTH_SHORT).show();
                    }
                    
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Log.d(TAG, "onFailure: hata" + t.getMessage());
                }
            });


        }


    }
}