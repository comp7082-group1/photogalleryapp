package com.example.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface PicturesDataSource {

    interface LoadPicturesCallback {

        void onPicturesLoaded(List<Picture> pictures);

        void onDataNotAvailable();
    }

    interface GetPictureCallback {

        void onPictureLoaded(Picture picture);

        void onDataNotAvailable();
    }

    void getPictures(@NonNull LoadPicturesCallback callback);

    void getPicture(@NonNull String pictureId, @NonNull GetPictureCallback callback);

    void savePicture(@NonNull Picture task);

    void refreshPictures();

    void deletePicture(@NonNull String pictureId);
}
