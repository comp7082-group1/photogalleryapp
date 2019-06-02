package com.example.photogal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PhotoEntity.class}, version = 1)
public abstract class PhotoRoomDatabase extends RoomDatabase {

    public abstract PhotoDao photoDao();

    private static volatile PhotoRoomDatabase INSTANCE;

    static PhotoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PhotoRoomDatabase.class, "photo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
