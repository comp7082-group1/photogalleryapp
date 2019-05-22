package com.example.data;

import java.util.List;

public interface PhotoDataSource {
    interface GetPhotoCallback{
        void onPhotosLoaded(List<Photo> photos);
    }
}
