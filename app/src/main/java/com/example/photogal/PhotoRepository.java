package com.example.photogal;

import android.app.Application;
import android.media.ExifInterface;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhotoRepository {
    private PhotoDao mPhotoDao;
    private LiveData<List<PhotoEntity>> mAllPhotos;


    PhotoRepository(Application application) {
        PhotoRoomDatabase db = PhotoRoomDatabase.getDatabase(application);
        mPhotoDao = db.photoDao();
        mAllPhotos = mPhotoDao.getAllPhotos();

    }

    LiveData<List<PhotoEntity>> getAllPhotos() {
        return mAllPhotos;
    }

    public void insert (PhotoEntity photo) {
        new insertAsyncTask(mPhotoDao).execute(photo);
    }

    private static class insertAsyncTask extends AsyncTask<PhotoEntity, Void, Void> {

        private PhotoDao mAsyncTaskDao;

        insertAsyncTask(PhotoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected  Void doInBackground(final PhotoEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
