package com.example.photogal;

import android.app.Application;
import android.media.ExifInterface;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhotoViewModel extends AndroidViewModel {

    private PhotoRepository mRepository;

    private LiveData<List<PhotoEntity>> mAllPhotos;

    private Date filterMinDate;
    private Date filterMaxDate;
    private String filterKeyword;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PhotoRepository(application);
        mAllPhotos = mRepository.getAllPhotos();
    }

    LiveData<List<PhotoEntity>> getAllPhotos() { return mAllPhotos; }

    public void insert(PhotoEntity photo) { mRepository.insert(photo); }

    public void refresh() {
        this.onCleared();
    }

}
