package com.example.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.common.base.Objects;

import java.util.Date;

@Entity(tableName = "pictures")
public final class Picture {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pictureid")
    private final String mId;

    @ColumnInfo(name = "filename")
    private final String mFileName;

    @ColumnInfo(name = "date")
    private final Date mDate;

    @ColumnInfo(name = "caption")
    private final String mCaption;

    public Picture(@NonNull String id,
                   @Nullable String filename,
                   @Nullable Date date,
                   @Nullable String caption) {
        mId = id;
        mFileName = filename;
        mDate = date;
        mCaption = caption;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getFileName() {
        return mFileName;
    }

    @Nullable
    public Date getDate() {
        return mDate;
    }

    @Nullable
    public String getCaption() {
        return mCaption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equal(mId, picture.mId) &&
                Objects.equal(mFileName, picture.mFileName) &&
                Objects.equal(mDate, picture.mDate) &&
                Objects.equal(mCaption, picture.mCaption);
    }

}
