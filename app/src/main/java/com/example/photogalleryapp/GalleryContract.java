package com.example.photogalleryapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.View;

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

        void refreshVisibility(android.view.View view);
    }

    interface View extends BaseView<Presenter> {

        void startActivity();

        void refreshVisibility();

        void setImageBitmap(Bitmap decodeFile);

        void setComment(String comment);

        void setCoordinates(String s);

        void setTimestamp(String timestamp);

        String getComment();
        
        void snapPhoto(android.view.View view);
    }
}
