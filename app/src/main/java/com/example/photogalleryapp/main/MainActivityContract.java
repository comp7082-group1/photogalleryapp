package com.example.photogalleryapp.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;

public interface MainActivityContract {
    interface View {
        void initView();

        void refreshVisibility(boolean isGalleryEmpty);

//        void startActivityForResult(int requestCode);

        String getCommentText();

        void setCommentText(String comment);

        void setImageBitmap(Bitmap decodeFile);

        void setLocation(String coordinates);

        void setTimeStamp(String dateTime);

        void showLongText(String text);

        void searchOnClick(int requestCode);

        void browseOnClick(int browseActivityRequestCode);

        void snapOnClick(int requestImageCapture);
    }

    interface Presenter {
        void initPresenter();

        void onClick(android.view.View view);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void setCurrentLocation(Location location);

        void searchOnClick(android.view.View v);

        void browseOnClick(android.view.View v);

        void snapOnClick(android.view.View v);
    }
}
