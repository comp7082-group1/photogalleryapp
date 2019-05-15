package com.example.photogalleryapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo")
    List<Photo> getAll();

    @Query("SELECT * FROM photo WHERE id IN (:userIds)")
    List<Photo> loadAllByIds(int[] userIds);

    @Delete
    void delete(Photo photo);
}
