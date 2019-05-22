package com.example.photogalleryapp.browse;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.data.Photo;
import com.example.data.PhotoDataSource;

import java.util.List;

public interface BrowseActivityContract {
    interface Model{

    }
    interface View{
        android.view.View getListItemView(Photo photo);

        void launchGoogleMaps(String coordinates);
    }
    interface Presenter{

        void getPhotos(PhotoDataSource.GetPhotoCallback callback);

        void listItemOnItemClick(AdapterView<?> parent, android.view.View view, int position, long id);
    }
}
