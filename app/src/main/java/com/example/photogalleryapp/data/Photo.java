package com.example.photogalleryapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "comment")
    public String comment;

    @ColumnInfo(name = "time_stamp")
    public String time_stamp;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;
}
