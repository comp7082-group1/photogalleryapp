package com.example.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.data.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert
    void insertPhoto(Photo p);

    @Insert
    void insertMultiplePhotos(List<Photo> photos);

    @Query ("SELECT * FROM photos")
    List<Photo> selectAllPhotos();

    @Query ("SELECT * FROM photos WHERE photoId = :id")
    Photo selectPhotoById(String id);

//    @Update
//    String updateTask(Photo photo);
//    @Query ("SELECT * FROM photos")
//    List<Photo> getAllPhotos();
//
//    @Query ("SELECT * FROM photos")
//    List<Photo> getAllPhotos();
}
