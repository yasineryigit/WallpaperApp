package com.ossovita.unsplashapi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ossovita.unsplashapi.R;
import com.ossovita.unsplashapi.model.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> implements Filterable {
    private static final String TAG = "PhotoAdapter";
    private List<Photo> photoList;
    private Context context;
    private MutableLiveData<String> searchKey = new MutableLiveData<>();

    public PhotoAdapter(List<Photo> photoList, Context context) {
        this.context = context;
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
        holder.textViewLikes.setText(photoList.get(position).getLikes().toString() + " Likes");
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Photo> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(photoList);
            } else {
                String searchKey = charSequence.toString().toLowerCase().trim();
                Log.d(TAG, "performFiltering: arama yapılacak string:" + searchKey);
                setSearchKey(searchKey);

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imageViewPhoto, imageViewProfilePicture;
        TextView textViewUsername, textViewLikes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
            textViewLikes = itemView.findViewById(R.id.text_view_likes);
            imageViewProfilePicture = itemView.findViewById(R.id.image_view_profile_picture);
            imageViewPhoto.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Operation");
            contextMenu.add(getAdapterPosition(), 101, 0, "Make Wallpaper");
            contextMenu.add(getAdapterPosition(), 102, 0, "Download");



        }
    }

    public MutableLiveData<String> getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String key) {
        searchKey.postValue(key);
    }

    public void setData(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    public Photo getPhotoAt(int position) {
        return photoList.get(position);
    }

}
