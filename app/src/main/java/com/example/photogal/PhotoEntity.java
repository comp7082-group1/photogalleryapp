package com.example.photogal;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class PhotoEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public int getId() {return this.id;}

    public void setId(int iD) {id = iD;}

    @ColumnInfo(name = "path")
    private String mPath;

    public String getPath(){return this.mPath;}

    public PhotoEntity(String path) {
        this.mPath = path;
    }

}
