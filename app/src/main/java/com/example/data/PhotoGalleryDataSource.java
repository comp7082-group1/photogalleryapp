package com.example.data;

import android.support.annotation.NonNull;

import com.example.util.AppExecutors;

import java.util.Date;
import java.util.List;

public class PhotoGalleryDataSource {
    private static volatile PhotoGalleryDataSource INSTANCE;
    private PhotoDao mPhotoDao;
    private AppExecutors mAppExecutors;

    private PhotoGalleryDataSource(@NonNull AppExecutors appExecutors, @NonNull PhotoDao photoDao){
        mAppExecutors = appExecutors;
        mPhotoDao = photoDao;
    }

    public static PhotoGalleryDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull PhotoDao photoDao){
        if(INSTANCE == null){
            synchronized(PhotoGalleryDataSource.class){
                if(INSTANCE == null){
                    INSTANCE = new PhotoGalleryDataSource(appExecutors, photoDao);
                }
            }
        }
        return INSTANCE;
    }

    public void getPhotos(final Date minDate, final Date maxDate, final String keyword, @NonNull final PhotoDataSource.GetPhotoCallback callback){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Photo> photos = mPhotoDao.getPhotos("", "", keyword);
                callback.onPhotosLoaded(photos);
            }
        });
    }

    public void getPhotos(@NonNull final PhotoDataSource.GetPhotoCallback callback){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Photo> photos = mPhotoDao.getPhotos();
                callback.onPhotosLoaded(photos);
            }
        });
    }

    public void savePhoto(final Photo photo){
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPhotoDao.addPhoto(photo);
            }
        });
    }
}
