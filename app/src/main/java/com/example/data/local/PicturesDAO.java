package com.example.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.data.Picture;

import java.util.List;

@Dao
public interface PicturesDAO {
    /**
     * Select all pictures from the picture table.
     *
     * @return all pictures.
     */
    @Query("SELECT * FROM Pictures")
    List<Picture> getPictures();

    /**
     * Select a picture by id.
     *
     * @param pictureId the picture id.
     * @return the picture with pictureId.
     */
    @Query("SELECT * FROM Pictures WHERE pictureId = :pictureId")
    Picture getPictureById(String pictureId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param picture the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPicture(Picture picture);

    /**
     * Update a picture.
     *
     * @param picture task to be updated
     * @return the number of pictures updated. This should always be 1.
     */
    @Update
    int updatePicture(Picture picture);

    /**
     * Update the complete status of a task
     *
     * @param pictureId    id of the picture
     * @param caption       new caption
     */
    @Query("UPDATE pictures SET caption = :caption WHERE pictureid = :pictureId")
    void updateCaption(String pictureId, String caption);

    /**
     * Delete a picture by id.
     *
     * @return the number of picture deleted. This should always be 1.
     */
    @Query("DELETE FROM Pictures WHERE pictureid = :pictureId")
    int deleteTaskById(String pictureId);

}
