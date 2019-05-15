package com.example.photogalleryapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Photo.class}, version = 1)
public abstract class PhotoGalleryDatabase extends RoomDatabase {

    private static PhotoGalleryDatabase INSTANCE;

    public abstract PhotoDao photoDao();

    private static final Object sLock = new Object();

    public static PhotoGalleryDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PhotoGalleryDatabase.class, "Photos.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
