package com.ossovita.unsplashapi.di;

import android.app.Application;
import android.content.Context;

import com.ossovita.unsplashapi.db.CustomSharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {


    @Provides
    @Singleton
    public static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static CustomSharedPreferences getCustomSharedPreferencesInstance(Application application) {
        return CustomSharedPreferences.getInstance(application);
    }

}
