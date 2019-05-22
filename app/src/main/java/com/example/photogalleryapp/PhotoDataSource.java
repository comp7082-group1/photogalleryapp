package com.example.photogalleryapp;

import com.example.photogalleryapp.data.Photo;

import java.util.List;

public interface PhotoDataSource {
    public interface GetPhotoCallback{
        void onDataNotAvailable();

        void onPhotosLoaded(List<Photo> photos);

        void onPhotoSaved(Photo photo);
    }
}
