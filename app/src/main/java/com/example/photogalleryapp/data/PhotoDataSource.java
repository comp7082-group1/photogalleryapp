package com.example.photogalleryapp.data;

import com.example.photogalleryapp.data.Photo;

import java.util.List;

public interface PhotoDataSource {
    interface GetPhotoCallback{
        void onPhotosLoaded(List<Photo> photos);
    }
}
