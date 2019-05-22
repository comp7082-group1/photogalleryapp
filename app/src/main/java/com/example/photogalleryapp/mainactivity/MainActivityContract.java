package com.example.photogalleryapp.mainactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public interface MainActivityContract {
    interface View{
        void initView();
        void refreshVisibility(boolean isGalleryEmpty);
        <T extends android.view.View> T findViewById(int id);
        void startActivityForResult(int requestCode);
        Object getSystemService(String locationService);
        Activity getActivity();
    }
    interface Presenter{
        void initPresenter();
        void onClick(android.view.View view);
        void filteredOnClick(android.view.View view);
        void onActivityResult(int requestCode, int resultCode, Intent data);
        void setCurrentLocation(Location location);
    }
}
