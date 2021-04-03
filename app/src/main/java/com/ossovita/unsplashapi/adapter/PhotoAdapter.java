package com.ossovita.unsplashapi.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ossovita.unsplashapi.R;
import com.ossovita.unsplashapi.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private static final String TAG = "PhotoAdapter";
    private List<Photo> photoList;

    public PhotoAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: cevap bekliyor");
        //glide
        Glide.with(holder.imageView.getContext())
                .load(photoList.get(position).getUrls().getFull())
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

}
