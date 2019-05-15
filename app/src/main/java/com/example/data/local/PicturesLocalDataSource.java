package com.example.data.local;

import androidx.annotation.NonNull;

import com.example.data.Picture;
import com.example.data.PicturesDataSource;
import com.example.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class PicturesLocalDataSource implements PicturesDataSource {

    private static volatile PicturesLocalDataSource INSTANCE;
    private PicturesDAO mPictureDAO;
    private AppExecutors mAppExecutors;

    private PicturesLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull PicturesDAO picturesDAO) {
        mAppExecutors = appExecutors;
        mPictureDAO = picturesDAO;
    }

    public static PicturesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull PicturesDAO tasksDao) {
        if (INSTANCE == null) {
            synchronized (PicturesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PicturesLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getPictures(@NonNull final LoadPicturesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Picture> pictures = mPictureDAO.getPictures();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (pictures.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onPicturesLoaded(pictures);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getPicture(@NonNull final String pictureId, @NonNull final GetPictureCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Picture picture = mPictureDAO.getPictureById(pictureId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (picture != null) {
                            callback.onPictureLoaded(picture);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void savePicture(@NonNull final Picture picture) {
        checkNotNull(picture);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mPictureDAO.insertPicture(picture);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshPictures() {

    }

    @Override
    public void deletePicture(@NonNull final String pictureId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mPictureDAO.deletePictureById(pictureId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
