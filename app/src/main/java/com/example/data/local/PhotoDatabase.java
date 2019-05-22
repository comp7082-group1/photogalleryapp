package com.example.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.data.Photo;

@Database(entities = {Photo.class}, version = 1, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {

    private static PhotoDatabase INSTANCE;

    public abstract PhotoDao photoDao();

    private static final Object sLock = new Object();

    public static PhotoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PhotoDatabase.class, "photos_db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }
}
