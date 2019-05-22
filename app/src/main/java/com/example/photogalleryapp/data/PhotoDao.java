package com.example.photogalleryapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    public void addPhoto(Photo photo);

    @Query("select * from photo")
    public List<Photo> getPhotos();
}
