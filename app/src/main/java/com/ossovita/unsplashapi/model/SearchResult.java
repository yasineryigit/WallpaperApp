package com.ossovita.unsplashapi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    @Expose
    private int total;
    @Expose
    private int total_pages;
    @Expose
    @SerializedName("results")
    List<Photo> photoList;

    public int getTotal() {
        return total;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
