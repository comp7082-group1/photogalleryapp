package com.example.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.data.Picture;

@Database(entities = {Picture.class}, version = 1, exportSchema = false)
public abstract class PhotoGalleryDatabase extends RoomDatabase {
    private static PhotoGalleryDatabase INSTANCE;
    public abstract PicturesDAO pictureDAO();
    private static final Object sLock = new Object();

    public static PhotoGalleryDatabase getInstance(Context context) {
        synchronized (sLock) {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PhotoGalleryDatabase.class, "Pictures.db")
                        .build();
            }

            return INSTANCE;
        }
    }
}
