package com.example.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;

import java.util.UUID;

@Entity(tableName = "pictures")
public final class Picture {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pictureid")
    private String mId;

    @ColumnInfo(name = "filename")
    private String mFileName;

    @ColumnInfo(name = "date")
    private Long mDate;

    @ColumnInfo(name = "caption")
    private String mCaption;

    @ColumnInfo(name = "coordinate")
    private String mCoordinate;

    @Ignore
    public Picture(@NonNull String filename, @NonNull Long date) {
        this(UUID.randomUUID().toString(), filename, date, "", "");
    }

    public Picture() {
        mId = UUID.randomUUID().toString();
        mFileName = "";
        mDate = (long)0;
        mCaption = "";
        mCoordinate = "";
    }

    public Picture(@NonNull String id,
                    @NonNull String filename,
                    @NonNull Long date,
                    @Nullable String caption,
                    @Nullable String coordinate) {
        mId = id;
        mFileName = filename;
        mDate = date;
        mCaption = caption;
        mCoordinate = coordinate;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getFileName() {
        return mFileName;
    }

    @NonNull
    public Long getDate() {
        return mDate;
    }

    @Nullable
    public String getCaption() {
        return mCaption;
    }

    @Nullable
    public String getCoordinate() {
        return mCoordinate;
    }

    public void setId(@NonNull String mId) {
        this.mId = mId;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public void setDate(Long mDate) {
        this.mDate = mDate;
    }

    public void setCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public void setCoordinate(String mCoordinate) {
        this.mCoordinate = mCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equal(mId, picture.mId) &&
                Objects.equal(mFileName, picture.mFileName) &&
                Objects.equal(mDate, picture.mDate) &&
                Objects.equal(mCaption, picture.mCaption) &&
                Objects.equal(mCoordinate, picture.mCoordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mFileName);
    }

    @Override
    public String toString() {
        return "picture filename " + mFileName;
    }
}
