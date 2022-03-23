package com.ossovita.unsplashapi.view;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ossovita.unsplashapi.R;
import com.ossovita.unsplashapi.adapter.PhotoAdapter;
import com.ossovita.unsplashapi.model.Photo;
import com.ossovita.unsplashapi.viewmodel.MainActivityViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    MainActivityViewModel viewModel;
    List<Photo> photoList = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    PhotoAdapter photoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photoAdapter = new PhotoAdapter(photoList, getApplicationContext());
        recyclerView.setAdapter(photoAdapter);
        setTitle(viewModel.getSearchKey().getValue());

    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getPhotoList().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                photoAdapter.setData(photos);
                photoAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: loading state: " + aBoolean);
                //add progress bar while loading
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

        //observe search term in recyclerview
        photoAdapter.getSearchKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                viewModel.setSearchKey(s);
                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(0);//scroll to top after new search
                Objects.requireNonNull(getSupportActionBar()).setTitle(s);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                photoAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 101) {
            Toast.makeText(MainActivity.this, "Changing Wallpaper..", Toast.LENGTH_LONG).show();
            new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(photoAdapter.getPhotoAt(item.getGroupId()).getUrls().getFull());
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        wallpaperManager.setBitmap(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            return true;
        }

        if (item.getItemId() == 102) {
            //Toast.makeText(this, "Download", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onContextItemSelected: download: " + photoAdapter.getPhotoAt(item.getGroupId()).getUrls().getFull());
            //new DownloadPhoto(this).execute(photoAdapter.getPhotoAt(item.getGroupId()).getUrls().getFull());
            viewModel.downloadImage(photoAdapter.getPhotoAt(item.getGroupId()).getUrls().getFull());
            Toast.makeText(this, "Downloading Started.", Toast.LENGTH_SHORT).show();

            return true;
        }

        return false;
    }


}