package com.example.photogalleryapp.browse;

import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.data.Photo;
import com.example.data.PhotoDataSource;
import com.example.data.PhotoGalleryDataSource;
import com.example.data.PhotoGalleryDatabase;
import com.example.photogalleryapp.R;
import com.example.util.AppExecutors;

import java.util.List;

public class BrowseActivityPresenter implements BrowseActivityContract.Presenter {

    private final BrowseActivityContract.View mBrowseActivityView;
    private PhotoGalleryDataSource mDataSource;
    private List<Photo> mPhotos = null;

    public BrowseActivityPresenter(BrowseActivity activity){
        mBrowseActivityView = activity;
        mDataSource = PhotoGalleryDataSource.getInstance(
                        new AppExecutors(),
                        PhotoGalleryDatabase.getInstance(activity.getBaseContext()).photoDao());
        mDataSource.getPhotos(getPhotoCallback);
    }

    PhotoDataSource.GetPhotoCallback getPhotoCallback = new PhotoDataSource.GetPhotoCallback() {
        @Override
        public void onPhotosLoaded(List<Photo> photos) {
            mPhotos = photos;
        }
    };

    @Override
    public void getPhotos(PhotoDataSource.GetPhotoCallback callback) {
        callback.onPhotosLoaded(mPhotos);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mPhotos == null) {
                return 0;
            }
            return mPhotos.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mPhotos.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Photo photo = mPhotos.get(position);
            return mBrowseActivityView.getListItemView(photo);
        }
    }
}
