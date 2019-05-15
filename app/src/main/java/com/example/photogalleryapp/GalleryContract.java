package com.example.photogalleryapp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;

public interface GalleryContract  {

    interface Presenter extends BasePresenter {


        void initializeGallery();

        void onClick(android.view.View v);

        void nextPhoto(android.view.View v);

        void previousPhoto(android.view.View v);

        void submitLocation();

        void submitComment();

        void submitTimeStamp();

        void refreshPhoto();

        void onLocationChanged(Location location);

        void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
    }

    interface View extends BaseView<Presenter> {

    }
}
