package com.ossovita.unsplashapi.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.textViewUsername.setText(photoList.get(position).getUser().getUsername());
        holder.textViewLikes.setText(photoList.get(position).getLikes().toString() +" Likes");
        //Photo
        Glide.with(holder.imageViewPhoto.getContext())
                .load(photoList.get(position).getUrls().getSmall())
                .centerCrop()
                .into(holder.imageViewPhoto);
        //Profile Picture
        Glide.with(holder.imageViewProfilePicture.getContext())
                .load(photoList.get(position).getUser().getProfileImage().getSmall())
                .centerCrop()
                .into(holder.imageViewProfilePicture);

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto,imageViewProfilePicture;
        TextView textViewUsername,textViewLikes;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
            textViewLikes = itemView.findViewById(R.id.text_view_likes);
            imageViewProfilePicture=itemView.findViewById(R.id.image_view_profile_picture);
        }
    }

}
