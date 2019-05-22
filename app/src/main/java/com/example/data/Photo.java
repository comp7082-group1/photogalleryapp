package com.example.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Blob;
import java.util.UUID;

@Entity(tableName = "photos")
public final class Photo {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "photoId")
    private final int mId;

    @Nullable
    @ColumnInfo(name = "photoData")
    public Blob mPhotoData;

    @Nullable
    @ColumnInfo(name = "date")
    public String mDate;

    @Nullable
    @ColumnInfo(name = "location")
    public Location mLocation;

    @Nullable
    @ColumnInfo(name = "caption")
    public String mCaption;

    /**
     * Use this constructor to specify a completed Task if the Task already has an id (copy of
     * another Task).
     *
     * @param photoData     data of the photo
     * @param date          date of the photo
     * @param location      geolocation of the photo
     * @param caption       photo caption
     */
    public Photo(@Nullable Blob photoData, @Nullable String date,
                 @Nullable Location location, @Nullable String caption) {
        mPhotoData = photoData;
        mDate = date;
        mLocation = location;
        mCaption = caption;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public Blob getPhotoData() {
        return mPhotoData;
    }

    @Nullable
    public String getDate() {
        return mDate;
    }

    @Nullable
    public String getCaption() { return mCaption; }

    @Nullable
    public Location getLocation() { return mLocation; }


    public void setPhotoData(@Nullable Blob mPhotoData) {
        this.mPhotoData = mPhotoData;
    }

    public void setDate(@Nullable String mDate) {
        this.mDate = mDate;
    }

    public void setLocation(@Nullable Location mLocation) {
        this.mLocation = mLocation;
    }

    public void setCaption(@Nullable String mCaption) {
        this.mCaption = mCaption;
    }

    @Override
    public String toString() {
        return "Photo with caption " + mCaption;
    }
}
