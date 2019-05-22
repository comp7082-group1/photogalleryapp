package com.example.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Insert
    void addPhoto(Photo photo);

    @Query("select * from photo where dateTime between :minDate and :maxDate and comment like :keyword")
    List<Photo> getPhotos(String minDate, String maxDate, String keyword);

    @Query("select * from photo")
    List<Photo> getPhotos();
}
