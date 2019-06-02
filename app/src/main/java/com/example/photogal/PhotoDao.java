package com.example.photogal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert
    void insert(PhotoEntity photo);

    @Query("DELETE FROM photo_table")
    void deleteAll();

    @Query("SELECT * FROM photo_table ORDER BY path ASC")
    LiveData<List<PhotoEntity>> getAllPhotos();
}
