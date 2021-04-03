package com.ossovita.unsplashapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ossovita.unsplashapi.api.UnsplashService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 6qcJq0xrIOg45maYtJsGfP9NtekiatBjX9gikdu4zgQ

        UnsplashApi unsplashApi = UnsplashService.getInstance().create();
    }
}